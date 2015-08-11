package br.com.milenio.vendingmachine.managedbean;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.joda.time.DateTime;
import org.joda.time.Days;
import org.primefaces.context.RequestContext;

import br.com.milenio.vendingmachine.domain.model.Atividade;
import br.com.milenio.vendingmachine.domain.model.UsuarioSistema;
import br.com.milenio.vendingmachine.exceptions.CadastroInexistenteException;
import br.com.milenio.vendingmachine.security.Seguranca;
import br.com.milenio.vendingmachine.service.AtividadeService;
import br.com.milenio.vendingmachine.service.UsuarioService;

@Named
@ViewScoped
public class AtividadeMB implements Serializable {
	private static final long serialVersionUID = -7422829646714382705L;
	
	@EJB
	private UsuarioService usuarioService;
	
	@EJB
	private AtividadeService atividadeService;
	
	@Inject
	private FacesContext ctx;
	
	private List<UsuarioSistema> listUsuarios = new ArrayList<UsuarioSistema>();
	private String login;
	private Long perfilId;

	private Calendar calendar = Calendar.getInstance();

	private Atividade atividade = new Atividade();
	
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

		if(atividade.getTexto() != null & atividade.getTexto().length() > 350) {
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
		
		atividade = new Atividade();
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
	
	public List<Atividade> solicitarAtividadesAgendadas() {
		String login = Seguranca.getLoginUsuarioLogado();
		
		return atividadeService.buscarAtividadesAgendadas(login);
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
}
