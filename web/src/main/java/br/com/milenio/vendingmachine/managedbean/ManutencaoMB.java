package br.com.milenio.vendingmachine.managedbean;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.ejb.EJBTransactionRolledbackException;
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

import br.com.milenio.vendingmachine.domain.model.Auditoria;
import br.com.milenio.vendingmachine.domain.model.Fornecedor;
import br.com.milenio.vendingmachine.domain.model.Manutencao;
import br.com.milenio.vendingmachine.domain.model.Maquina;
import br.com.milenio.vendingmachine.domain.model.MaquinaStatus;
import br.com.milenio.vendingmachine.exceptions.CadastroInexistenteException;
import br.com.milenio.vendingmachine.exceptions.InconsistenciaException;
import br.com.milenio.vendingmachine.repository.MaquinaStatusRepository;
import br.com.milenio.vendingmachine.security.Seguranca;
import br.com.milenio.vendingmachine.service.AuditoriaService;
import br.com.milenio.vendingmachine.service.FornecedorService;
import br.com.milenio.vendingmachine.service.ManutencaoService;
import br.com.milenio.vendingmachine.service.MaquinaService;

@Named
@ViewScoped
public class ManutencaoMB implements Serializable {
	private static final long serialVersionUID = 8664939655416903668L;
	
	@Inject
	private FornecedorService fornecedorService;
	
	@Inject
	private MaquinaService maquinaService;
	
	@Inject
	private MaquinaStatusRepository maquinaStatusRepository;
	
	@Inject
	private ManutencaoService manutencaoService;
	
	@Inject
	private AuditoriaService auditoriaService;
	
	@Inject
	private HttpServletRequest request;
	
	@Inject
	private ExternalContext external;
	
	@Inject
	private FacesContext ctx;
	
	@Inject
	private Logger logger;
	
	private Fornecedor fornecedor = new Fornecedor();
	private Maquina maquina = new Maquina();
	private List<Fornecedor> listFornecedores = new ArrayList<Fornecedor>();
	private List<Maquina> listMaquinas = new ArrayList<Maquina>();
	private Manutencao manutencao = new Manutencao();
	private List<Manutencao> listManutencao = new ArrayList<Manutencao>();
	private Manutencao manuConsParam = new Manutencao();
	private boolean carregarPagina = true;
	
	public void cadastrar() {
		logger.debug("Tentando realizar o cadastro da manuten��o para a m�quina " + manutencao.getMaquina().getCodigo());
		
		String motivo = manutencao.getMotivo() != null ? manutencao.getMotivo().trim() : "";
		
		if(motivo.isEmpty() || motivo.isEmpty()) {
			ctx.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "O campo motivo n�o pode conter apenas espa�os em branco.", null));
			return;
		} else if (manutencao.getMaquina().getCodigo().trim().isEmpty()) {
			// Se entrou aqui, ent�o o c�digo da m�quina estava v�zio
			ctx.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "O campo c�digo da m�quina n�o pode conter apenas espa�os em branco", null));
			return;
		} else if (manutencao.getFornecedor().getCodigo().trim().isEmpty()) {
			// Se entrou aqui, ent�o o c�digo do fornecedor estava v�zio
			ctx.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "O campo c�digo do fornecedor n�o pode conter apenas espa�os em branco", null));
			return;
		}
		
		try {
			manutencaoService.cadastrar(manutencao);

			// Sucesso - Exibe mensagem de cadastro realizado com sucesso
			ctx.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Manuten��o para a m�quina " + manutencao.getMaquina().getCodigo() + " cadastrada com sucesso. Status da m�quina agora � 'EM MANUTEN��O'.", null));
			logger.info("Manuten��o para a m�quina " + manutencao.getMaquina().getCodigo() + " cadastrada no sistema com sucesso. Status da m�quina agora � 'EM MANUTEN��O'.");
			
			// Processo de auditoria de cadastro de manuten��o
			Auditoria auditoria = new Auditoria();
			auditoria.setDataAcao(new Date());
			auditoria.setTitulo("Cadastro");
			auditoria.setDescricao("Cadastrou a manuten��o para a m�quina " + manutencao.getMaquina().getCodigo());
			auditoria.setUsuario(Seguranca.getUsuarioLogado());
			auditoria.setIp(request.getRemoteAddr());
			auditoriaService.cadastrarNovaAcao(auditoria);
		} catch(InconsistenciaException e) {
			ctx.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, e.getMessage(), null));
			logger.info(e.getMessage());
		}
		
		manutencao = new Manutencao();
	}
	
	public void editar() {
		logger.debug("Tentando realizar a edi��o da manuten��o para a m�quina " + manutencao.getMaquina().getCodigo());
		
		String motivo = manutencao.getMotivo() != null ? manutencao.getMotivo().trim() : "";
		
		if(motivo.isEmpty() || motivo.isEmpty()) {
			ctx.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "O campo motivo n�o pode conter apenas espa�os em branco.", null));
			return;
		} else if (manutencao.getMaquina().getCodigo().trim().isEmpty()) {
			// Se entrou aqui, ent�o o c�digo da m�quina estava v�zio
			ctx.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "O campo c�digo da m�quina n�o pode conter apenas espa�os em branco", null));
			return;
		} else if (manutencao.getFornecedor().getCodigo().trim().isEmpty()) {
			// Se entrou aqui, ent�o o c�digo do fornecedor estava v�zio
			ctx.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "O campo c�digo do fornecedor n�o pode conter apenas espa�os em branco", null));
			return;
		}
		
		try {
			manutencaoService.editar(manutencao);

			// Sucesso - Exibe mensagem de cadastro realizado com sucesso
			ctx.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Manuten��o para a m�quina " + manutencao.getMaquina().getCodigo() + " editada com sucesso.", null));
			logger.info("Manuten��o para a m�quina " + manutencao.getMaquina().getCodigo() + " editada no sistema com sucesso.");
			
			// Processo de auditoria de edi��o de manuten��o
			Auditoria auditoria = new Auditoria();
			auditoria.setDataAcao(new Date());
			auditoria.setTitulo("Edi��o");
			auditoria.setDescricao("Editou a manuten��o para a m�quina " + manutencao.getMaquina().getCodigo());
			auditoria.setUsuario(Seguranca.getUsuarioLogado());
			auditoria.setIp(request.getRemoteAddr());
			auditoriaService.cadastrarNovaAcao(auditoria);
		} catch(InconsistenciaException e) {
			ctx.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, e.getMessage(), null));
			logger.info(e.getMessage());
		}
	}
	
	public void consultarFornecedor() {
		try {
			listFornecedores = fornecedorService.buscarFornecedoresComFiltro(fornecedor);
			RequestContext context = RequestContext.getCurrentInstance();
			context.execute("PF('dlgConsultaFornecedor').show();");
		} catch (CadastroInexistenteException e) {
			listFornecedores.clear();
			ctx.addMessage("Message2", new FacesMessage(FacesMessage.SEVERITY_WARN, e.getMessage(), null));
			RequestContext context = RequestContext.getCurrentInstance();
			context.execute("PF('dlgConsultaFornecedor').show();");
		}
	}
	
	public void consultarMaquina() {
		try {
			// Busca apenas as m�quinas que estejam em estoque
			MaquinaStatus maquinaStatus = maquinaStatusRepository.findByDescricao("EM ESTOQUE");
			maquina.setMaquinaStatus(maquinaStatus);
			listMaquinas = maquinaService.buscarComFiltro(maquina);
			RequestContext context = RequestContext.getCurrentInstance();
			context.execute("PF('dlgConsultaMaquina').show();");
		} catch (CadastroInexistenteException e) {
			listMaquinas.clear();
			ctx.addMessage("Message2", new FacesMessage(FacesMessage.SEVERITY_WARN, e.getMessage(), null));
			RequestContext context = RequestContext.getCurrentInstance();
			context.execute("PF('dlgConsultaMaquina').show();");
		}
	}
	
	public void excluir() {
		Manutencao man;
		try {
			man = manutencaoService.excluir(manutencao.getId());
		} catch (InconsistenciaException e) {
			ctx.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, e.getMessage(), null));
			return;
		} catch(EJBTransactionRolledbackException e) {
			ctx.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Essa manuten��o n�o pode ser exclu�da.", null));
			logger.warn("Tentativa de excluir uma manuten��o vinculada a alguma outra entidade");
			return;
		}
		
		ctx.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Manuten��o para a m�quina " + man.getMaquina().getCodigo() + " exclu�da com sucesso", null));
		
		// Processo de auditoria de exclus�o de manuten��o
		Auditoria auditoria = new Auditoria();
		auditoria.setDataAcao(new Date());
		auditoria.setTitulo("Exclus�o");
		auditoria.setDescricao("Excluiu a manuten��o para a m�quina " + manutencao.getMaquina().getCodigo());
		auditoria.setUsuario(Seguranca.getUsuarioLogado());
		auditoria.setIp(request.getRemoteAddr());
		auditoriaService.cadastrarNovaAcao(auditoria);
		
		// Recarrega a listagem de atividades
		consultar(false);
	}
	
	public void excluirPelaEdicao() {
		Manutencao man;
		
		try {
			man = manutencaoService.excluir(manutencao.getId());
		} catch (InconsistenciaException e) {
			ctx.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, e.getMessage(), null));
			return;
		}
		
		ctx.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Manuten��o para a m�quina " + man.getMaquina().getCodigo() + " exclu�da com sucesso", null));
		
		// Processo de auditoria de exclus�o de manuten��o
		Auditoria auditoria = new Auditoria();
		auditoria.setDataAcao(new Date());
		auditoria.setTitulo("Exclus�o");
		auditoria.setDescricao("Excluiu a manuten��o para a m�quina " + manutencao.getMaquina().getCodigo());
		auditoria.setUsuario(Seguranca.getUsuarioLogado());
		auditoria.setIp(request.getRemoteAddr());
		auditoriaService.cadastrarNovaAcao(auditoria);
		
		try {
			external.getFlash().setKeepMessages(true);
			external.redirect(request.getContextPath() + "/admin/consultaManutencao.xhtml");
		} catch (IOException e) {
			e.printStackTrace();
			logger.error("Erro ao tentar redirecionar para a p�gina " + request.getContextPath() + "/admin/consultaManutencao.xhtml");
		}
	}
	
	public void carregarDadosParaEdicao() {
		if(carregarPagina) {
			manutencao = manutencaoService.findById(manutencao.getId());
		}
		
		carregarPagina = false;
	}
	
	public void consultar(boolean exibirMensagem) {
		try {
			if(manuConsParam.getDataCadastro() != null) {
				// Valida se a data informada n�o � menor que a data atual
				DateTime hoje = new DateTime(new Date());
				DateTime dataInformada = new DateTime(manuConsParam.getDataCadastro());
				Days daysBetween = Days.daysBetween(hoje, dataInformada);
				
				if(daysBetween.getDays() > 0) {
					ctx.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Apenas datas passadas podem ser informadas.", null));
					return;
				}
			}
			
			listManutencao = manutencaoService.buscarComFiltro(manuConsParam);
		} catch (CadastroInexistenteException e) {
			if(listManutencao != null && !listManutencao.isEmpty()) {
				listManutencao.clear();
			}
			
			if(exibirMensagem) {
				ctx.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, e.getMessage(), null));
			}
		}
	}
	
	public void efetivarRetorno() {
		manutencaoService.efetivarRetorno(manutencao);
		
		StringBuilder sb = new StringBuilder();
		sb.append("Retorno da manuten��o salvo com sucesso. M�quina ");
		sb.append(manutencao.getMaquina().getCodigo());
		sb.append(" agora est� com o status ");
		sb.append(manutencao.getMaquina().getMaquinaStatus().getDescricao());
		sb.append(".");
		
		// Processo de auditoria de exclus�o de manuten��o
		Auditoria auditoria = new Auditoria();
		auditoria.setDataAcao(new Date());
		auditoria.setTitulo("Retorno");
		auditoria.setDescricao("Informou o retorno da manuten��o para a m�quina " + manutencao.getMaquina().getCodigo());
		auditoria.setUsuario(Seguranca.getUsuarioLogado());
		auditoria.setIp(request.getRemoteAddr());
		auditoriaService.cadastrarNovaAcao(auditoria);
		
		ctx.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, sb.toString(), null));
	}
	
	public void selecionarFornecedor(String codigo) {
		Fornecedor forn = fornecedorService.findByCodigo(codigo);
		
		if(forn != null) {
			manutencao.setFornecedor(forn);
		} else{
			manutencao.setFornecedor(new Fornecedor());
			ctx.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "C�digo inv�lido: O c�digo " + codigo + " informado n�o corresponde a nenhum fornecedor cadastrado no sistema.", null));
			return;
		}
		
		RequestContext context = RequestContext.getCurrentInstance();
		context.execute("PF('dlgConsultaFornecedor').hide();");
	}
	
	public void selecionarMaquina(String codigo, boolean telaEdicao) {
		Maquina maq = maquinaService.findByCodigo(codigo);
		
		if(maq != null) {
			if(!"EM ESTOQUE".equalsIgnoreCase(maq.getMaquinaStatus().getDescricao()) && telaEdicao == false) {
				ctx.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "C�digo inv�lido: O c�digo " + codigo + " informado n�o corresponde a uma m�quina dispon�vel em estoque.", null));
				manutencao.setMaquina(new Maquina());
				return;
			}
			manutencao.setMaquina(maq);
		} else{
			manutencao.setMaquina(new Maquina());
			ctx.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "C�digo inv�lido: O c�digo " + codigo + " informado n�o corresponde a nenhuma m�quina cadastrada no sistema.", null));
			return;
		}
		
		RequestContext context = RequestContext.getCurrentInstance();
		context.execute("PF('dlgConsultaMaquina').hide();");
	}
	
	public void abrirDialog(String dialog) {
		fornecedor = new Fornecedor();
		maquina = new Maquina();
		listMaquinas = new ArrayList<Maquina>();
		listFornecedores = new ArrayList<Fornecedor>();
		
		RequestContext context = RequestContext.getCurrentInstance();
		context.execute("PF('" + dialog + "').show();");
	}
	
	public void fecharDialog(String dialog) {
		listFornecedores.clear();
		RequestContext context = RequestContext.getCurrentInstance();
		context.execute("PF('" + dialog + "').hide();");
	}
	
	public Fornecedor getFornecedor() {
		return fornecedor;
	}
	public void setFornecedor(Fornecedor fornecedor) {
		this.fornecedor = fornecedor;
	}
	public List<Fornecedor> getListFornecedores() {
		return listFornecedores;
	}
	public void setListFornecedores(List<Fornecedor> listFornecedores) {
		this.listFornecedores = listFornecedores;
	}
	public Manutencao getManutencao() {
		return manutencao;
	}
	public void setManutencao(Manutencao manutencao) {
		this.manutencao = manutencao;
	}

	public Maquina getMaquina() {
		return maquina;
	}

	public void setMaquina(Maquina maquina) {
		this.maquina = maquina;
	}

	public List<Maquina> getListMaquinas() {
		return listMaquinas;
	}

	public void setListMaquinas(List<Maquina> listMaquinas) {
		this.listMaquinas = listMaquinas;
	}

	public List<Manutencao> getListManutencao() {
		return listManutencao;
	}

	public void setListManutencao(List<Manutencao> listManutencao) {
		this.listManutencao = listManutencao;
	}

	public Manutencao getManuConsParam() {
		return manuConsParam;
	}

	public void setManuConsParam(Manutencao manuConsParam) {
		this.manuConsParam = manuConsParam;
	}
}
