package br.com.milenio.vendingmachine.managedbean;

import java.io.IOException;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;

import org.apache.logging.log4j.Logger;
import org.joda.time.DateTime;
import org.joda.time.Days;
import org.primefaces.context.RequestContext;

import br.com.milenio.vendingmachine.domain.model.Atividade;
import br.com.milenio.vendingmachine.domain.model.Auditoria;
import br.com.milenio.vendingmachine.domain.model.UsuarioSistema;
import br.com.milenio.vendingmachine.exceptions.CadastroInexistenteException;
import br.com.milenio.vendingmachine.security.Seguranca;
import br.com.milenio.vendingmachine.service.AtividadeService;
import br.com.milenio.vendingmachine.service.AuditoriaService;
import br.com.milenio.vendingmachine.service.UsuarioService;

@Named
@ViewScoped
public class AtividadeMB implements Serializable {
	private static final long serialVersionUID = -7422829646714382705L;
	
	@Inject
	private Logger logger;
	
	@EJB
	private UsuarioService usuarioService;
	
	@EJB
	private AtividadeService atividadeService;
	
	@Inject
	private FacesContext ctx;
	
	@Inject
	private ExternalContext external;
	
	@Inject
	private HttpServletRequest request;
	
	@Inject
	private AuditoriaService auditoriaService;
	
	private List<UsuarioSistema> listUsuarios = new ArrayList<UsuarioSistema>();
	private String login;
	private Long perfilId;
	private List<Atividade> lstAtividade;
	private Date data;
	private Long idAtividade;

	private Calendar calendar = Calendar.getInstance();

	private Atividade atividade = new Atividade();
	private boolean carregarPagina = true;
	
	public void consultarUsuario() {
		try {
			listUsuarios = usuarioService.buscarUsuariosComFiltro(login, null, perfilId);
			RequestContext context = RequestContext.getCurrentInstance();
			context.execute("PF('dlgConsultaUsuario').show();");
		} catch (CadastroInexistenteException e) {
			listUsuarios.clear();
			ctx.addMessage("Message2", new FacesMessage(FacesMessage.SEVERITY_WARN, e.getMessage(), null));
			RequestContext context = RequestContext.getCurrentInstance();
			context.execute("PF('dlgConsultaUsuario').show();");
		}
	}
	
	public void selecionarUsuario(String loginUsuario) {
		login = null;
		perfilId = null;
		listUsuarios.clear();
		
		UsuarioSistema usuario = usuarioService.findByLogin(loginUsuario);
		
		if(usuario != null) {
			atividade.setUsuario(usuario);
		} else{
			atividade.setUsuario(new UsuarioSistema());
			ctx.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Login inválido: O login "+ loginUsuario + " informado não corresponde a nenhum usuário cadastrado no sistema.", null));
			return;
		}
		
		RequestContext context = RequestContext.getCurrentInstance();
		context.execute("PF('dlgConsultaUsuario').hide();");
	}
	
	public void solicitarCadastroAtividade() {
		
		String titulo = atividade.getTitulo() != null ? atividade.getTitulo().trim() : "";
		String texto = atividade.getTexto() != null ? atividade.getTexto().trim() : "";
		
		if(texto.isEmpty() || titulo.isEmpty()) {
			ctx.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Os campos título e descrição não podem conter apenas espaços em branco.", null));
			return;
		}

		if(texto != null & texto.length() > 350) {
			ctx.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "A descrição da atividade não pode ser superior a 350 caracteres.", null));
			return;
		}
		
		// Valida o período de tempo entre as datas, não pode ser maior que um ano
		DateTime begin = new DateTime(new Date());
		DateTime end = new DateTime(atividade.getDataAgendamento());
		Days daysBetween = Days.daysBetween(begin, end);
		
		if(daysBetween.getDays() > 365){
			ctx.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Atividades só podem ser agendadas para datas futuras com prazo máximo de um ano.", null));
			return;
		} else if(daysBetween.getDays() < 0) {
			ctx.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Atividades não podem ser agendadas para datas passadas.", null));
			return;
		}
		
		try {
			atividadeService.cadastrarAtividade(atividade);
		} catch (CadastroInexistenteException e) {
			ctx.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Login inválido: O login informado não corresponde a nenhum usuário cadastrado no sistema.", null));
			return;
		}
	
		ctx.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Atividade cadastrada para o usuário " + atividade.getUsuario().getLogin() + " com sucesso", null));
		
		// Processo de auditoria
		Auditoria auditoria = new Auditoria();
		auditoria.setDataAcao(new Date());
		auditoria.setTitulo("Cadastro");
		auditoria.setDescricao("Cadastrou a atividade " + atividade.getTitulo() + " para o usuário " + atividade.getUsuario().getLogin());
		auditoria.setUsuario(Seguranca.getUsuarioLogado());
		auditoria.setIp(request.getRemoteAddr());
		auditoriaService.cadastrarNovaAcao(auditoria);
		
		atividade = new Atividade();
	}
	
	public void carregarDadosAtividadeParaEdicao() {
		if(carregarPagina) {
			atividade = atividadeService.findById(idAtividade);
		}
		carregarPagina = false;
	}
	
	public void fecharDialog() {
		login = null;
		perfilId = null;
		listUsuarios.clear();
		
		RequestContext context = RequestContext.getCurrentInstance();
		context.execute("PF('dlgConsultaUsuario').hide();");
	}
	
	public void abrirDialog() {
		RequestContext context = RequestContext.getCurrentInstance();
		context.execute("PF('dlgConsultaUsuario').show();");
	}
	
	public void abrirDialogExcluir() {
		RequestContext context = RequestContext.getCurrentInstance();
		context.execute("PF('dlgExcluir').show();");
	}
	
	public void solicitarAtividadesAgendadas() {
		String login = Seguranca.getLoginUsuarioLogado();
		
		try {
			lstAtividade = atividadeService.buscarAtividadesAgendadas(login, new Date());
		} catch (CadastroInexistenteException e) {
			ctx.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, e.getMessage(), null));
		}
	}
	
	public void editarAtividade() {
		logger.debug("Tentando realizar alterações no agendamento " + atividade.getTitulo());
		
		String titulo = atividade.getTitulo() != null ? atividade.getTitulo().trim() : "";
		String texto = atividade.getTexto() != null ? atividade.getTexto().trim() : "";
		
		if(texto.isEmpty() || titulo.isEmpty()) {
			ctx.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Os campos título e descrição não podem conter apenas espaços em branco.", null));
			return;
		}
		
		try {
			atividadeService.editarUsuario(atividade);
		} catch (CadastroInexistenteException e) {
			ctx.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, e.getMessage(), null));
			return;
		}
		
		// Sucesso - Exibe mensagem de edição realizada com sucesso
		ctx.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "As alterações no agendamento " + atividade.getTitulo() + " foram salvas com sucesso.", null));
		logger.info("As alterações no agendamento " + atividade.getTitulo() + " foram salvas com sucesso.");
		
		// Processo de auditoria
		Auditoria auditoria = new Auditoria();
		auditoria.setDataAcao(new Date());
		auditoria.setTitulo("Edição");
		auditoria.setDescricao("Editou a atividade " + atividade.getTitulo());
		auditoria.setUsuario(Seguranca.getUsuarioLogado());
		auditoria.setIp(request.getRemoteAddr());
		auditoriaService.cadastrarNovaAcao(auditoria);
	}
	
	public void consultarAtividades(boolean exibirMensagem) {
		try {
			lstAtividade = atividadeService.buscarAtividadesComFiltro(login, perfilId, data);
		} catch (CadastroInexistenteException e) {
			if(lstAtividade != null && !lstAtividade.isEmpty()) {
				lstAtividade.clear();
			}
			
			if(exibirMensagem) {
				ctx.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, e.getMessage(), null));
			}
		}
	}
	
	public void excluirAtividadePelaEdicao() {
		Atividade atv = atividadeService.excluirAtividade(idAtividade);
		
		ctx.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Atividade " + atv.getTitulo() + " excluída com sucesso", null));
		
		try {
			external.getFlash().setKeepMessages(true);
			external.redirect(request.getContextPath() + "/admin/consultarAtividade.xhtml");
		} catch (IOException e) {
			e.printStackTrace();
			logger.error("Erro ao tentar redirecionar para a página " + request.getContextPath() + "/consultarAtividade.xhtml");
		}
	}
	
	public void excluirAtividade() {
		Atividade atv = atividadeService.excluirAtividade(idAtividade);
		
		ctx.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Atividade " + atv.getTitulo() + " excluída com sucesso", null));
		
		// Recarrega a listagem de atividades
		consultarAtividades(false);
	}
	
	public Date getTodayDate() {
		return new Date();
	}
	
	public String getHoje() {
		SimpleDateFormat formatas = new SimpleDateFormat("dd/MM/yyyy");
        return formatas.format(new Date());
	}
	
	public Date getMaxDate() {
		calendar.setTime(new Date());
		calendar.add( Calendar.DAY_OF_MONTH , 365);  
		return calendar.getTime();
	}

	public String getLogin() {
		return login;
	}

	public void setLogin(String login) {
		this.login = login;
	}

	public Long getPerfilId() {
		return perfilId;
	}

	public void setPerfilId(Long perfilId) {
		this.perfilId = perfilId;
	}

	public List<UsuarioSistema> getListUsuarios() {
		return listUsuarios;
	}

	public void setListUsuarios(List<UsuarioSistema> listUsuarios) {
		this.listUsuarios = listUsuarios;
	}
	
	public Atividade getAtividade() {
		return atividade;
	}

	public void setAtividade(Atividade atividade) {
		this.atividade = atividade;
	}
	
	public List<Atividade> getLstAtividade() {
		
		return lstAtividade;
	}

	public void setLstAtividade(List<Atividade> lstAtividade) {
		this.lstAtividade = lstAtividade;
	}
	
	public Date getData() {
		return data;
	}

	public void setData(Date data) {
		this.data = data;
	}
	
	public Long getIdAtividade() {
		return idAtividade;
	}

	public void setIdAtividade(Long idAtividade) {
		this.idAtividade = idAtividade;
	}
}
