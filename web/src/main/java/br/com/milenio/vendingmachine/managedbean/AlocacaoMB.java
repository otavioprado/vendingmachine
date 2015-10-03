package br.com.milenio.vendingmachine.managedbean;

import java.io.IOException;
import java.io.Serializable;
import java.sql.Array;
import java.util.ArrayList;
import java.util.Arrays;
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
import org.primefaces.context.RequestContext;

import br.com.milenio.vendingmachine.domain.model.Alocacao;
import br.com.milenio.vendingmachine.domain.model.Auditoria;
import br.com.milenio.vendingmachine.domain.model.Cliente;
import br.com.milenio.vendingmachine.domain.model.Contrato;
import br.com.milenio.vendingmachine.domain.model.Maquina;
import br.com.milenio.vendingmachine.domain.model.MaquinaStatus;
import br.com.milenio.vendingmachine.domain.model.Reserva;
import br.com.milenio.vendingmachine.exceptions.CadastroInexistenteException;
import br.com.milenio.vendingmachine.exceptions.InconsistenciaException;
import br.com.milenio.vendingmachine.repository.MaquinaStatusRepository;
import br.com.milenio.vendingmachine.security.Seguranca;
import br.com.milenio.vendingmachine.service.AlocacaoService;
import br.com.milenio.vendingmachine.service.AuditoriaService;
import br.com.milenio.vendingmachine.service.ClienteService;
import br.com.milenio.vendingmachine.service.ContratoService;
import br.com.milenio.vendingmachine.service.MaquinaService;
import br.com.milenio.vendingmachine.service.ReservaService;

@Named
@ViewScoped
public class AlocacaoMB implements Serializable {
	private static final long serialVersionUID = -5318977749035488567L;

	@Inject
	private ClienteService clienteService;
	
	@Inject
	private AlocacaoService alocacaoService;
	
	@Inject
	private FacesContext ctx;
	
	@Inject
	private Logger logger;
	
	@Inject
	private MaquinaService maquinaService;
	
	@Inject
	private ExternalContext external;
	
	@Inject
	private AuditoriaService auditoriaService;
	
	@Inject
	private ReservaService reservaService;
	
	@Inject
	private HttpServletRequest request;
	
	@Inject
	private MaquinaStatusRepository maquinaStatusRepository;
	
	@Inject
	private ContratoService contratoService;
	
	private boolean carregarPagina = true;
	private Alocacao alocacao = new Alocacao();
	private Contrato contrato = new Contrato();
	private Cliente cliente = new Cliente();
	private Maquina maquina = new Maquina();
	private List<Maquina> listMaquinas = new ArrayList<Maquina>();
	private List<Contrato> listContratos = new ArrayList<Contrato>();
	private List<Cliente> listClientes = new ArrayList<Cliente>();
	private List<Alocacao> listAlocacoes = new ArrayList<Alocacao>();
	private Alocacao alocacaoConsParam = new Alocacao();
	
	public void cadastrar() {
		logger.debug("Tentando realizar o cadastro da aloca��o da m�quina " + alocacao.getMaquina().getCodigo() + " para o cliente " + alocacao.getCliente().getNomeFantasia());
		
		try {
			alocacao.setUsuario(Seguranca.getUsuarioLogado());
			alocacaoService.cadastrar(alocacao);
			
			// Sucesso - Exibe mensagem de cadastro realizado com sucesso
			ctx.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Solicita��o de aloca��o da m�quina " + alocacao.getMaquina().getCodigo() + " para o cliente " + alocacao.getCliente().getNomeFantasia() + " cadastrada com sucesso.", null));
			logger.info("Solicita��o de aloca��o da m�quina " + alocacao.getMaquina().getCodigo() + " para o cliente " + alocacao.getCliente().getNomeFantasia() + " cadastrada com sucesso.");
			
			// Processo de auditoria de cadastro de usu�rio
			Auditoria auditoria = new Auditoria();
			auditoria.setDataAcao(new Date());
			auditoria.setTitulo("Cadastro");
			auditoria.setDescricao("Cadastrou uma solicita��o de aloca��o para a m�quina " + alocacao.getMaquina().getCodigo() + " para o cliente " + alocacao.getCliente().getNomeFantasia());
			auditoria.setUsuario(Seguranca.getUsuarioLogado());
			auditoria.setIp(request.getRemoteAddr());
			auditoriaService.cadastrarNovaAcao(auditoria);
		} catch (InconsistenciaException e) {
			ctx.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, e.getMessage(), null));
			logger.info(e.getMessage());
		}
		
		alocacao = new Alocacao(); 
	}
	
	public void alocar() {
		logger.debug("[Conting�ncia] Tentando realizar a confirma��o de aloca��o da m�quina " + alocacao.getMaquina().getCodigo() + " para o cliente " + alocacao.getCliente().getNomeFantasia());
		
		try {
			alocacaoService.alocar(alocacao);
			
			// Sucesso - Exibe mensagem de aloca��o realizada com sucesso
			ctx.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Aloca��o da m�quina " + alocacao.getMaquina().getCodigo() + " para o cliente " + alocacao.getCliente().getNomeFantasia() + " realizada com sucesso.", null));
			logger.info("Aloca��o da m�quina " + alocacao.getMaquina().getCodigo() + " para o cliente " + alocacao.getCliente().getNomeFantasia() + " realizada com sucesso.");
			
			// Processo de auditoria de cadastro de usu�rio
			Auditoria auditoria = new Auditoria();
			auditoria.setDataAcao(new Date());
			auditoria.setTitulo("Cadastro");
			auditoria.setDescricao("Cadastrou uma confirma��o de aloca��o da m�quina " + alocacao.getMaquina().getCodigo() + " para o cliente " + alocacao.getCliente().getNomeFantasia());
			auditoria.setUsuario(Seguranca.getUsuarioLogado());
			auditoria.setIp(request.getRemoteAddr());
			auditoriaService.cadastrarNovaAcao(auditoria);
		} catch (InconsistenciaException e) {
			ctx.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, e.getMessage(), null));
			logger.info(e.getMessage());
		}
		
		alocacao = new Alocacao(); 
	}
	
	public void desalocar() {
		logger.debug("[Conting�ncia] Tentando realizar a confirma��o de desaloca��o da m�quina " + alocacao.getMaquina().getCodigo() + " para o cliente " + alocacao.getCliente().getNomeFantasia());
		
		try {
			alocacaoService.desalocar(alocacao);
			
			// Sucesso - Exibe mensagem de aloca��o realizada com sucesso
			ctx.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Desaloca��o da m�quina " + alocacao.getMaquina().getCodigo() + " para o cliente " + alocacao.getCliente().getNomeFantasia() + " realizada com sucesso.", null));
			logger.info("Desaloca��o da m�quina " + alocacao.getMaquina().getCodigo() + " para o cliente " + alocacao.getCliente().getNomeFantasia() + " realizada com sucesso.");
			
			// Processo de auditoria de cadastro de usu�rio
			Auditoria auditoria = new Auditoria();
			auditoria.setDataAcao(new Date());
			auditoria.setTitulo("Cadastro");
			auditoria.setDescricao("Cadastrou uma confirma��o de desaloca��o da m�quina " + alocacao.getMaquina().getCodigo() + " para o cliente " + alocacao.getCliente().getNomeFantasia());
			auditoria.setUsuario(Seguranca.getUsuarioLogado());
			auditoria.setIp(request.getRemoteAddr());
			auditoriaService.cadastrarNovaAcao(auditoria);
		} catch (InconsistenciaException e) {
			ctx.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, e.getMessage(), null));
			logger.info(e.getMessage());
		}
		
		alocacao = new Alocacao(); 
	}
	
	public void carregarCliente() {
		Long id = alocacao.getCliente().getId();
		if(carregarPagina && id != 0) {
			Cliente cliente = clienteService.findById(id);
			alocacao.setCliente(cliente);
		}
		
		carregarPagina = false;
	}
	
	public void solicitarDesalocacao() {
		logger.debug("Tentando realizar a solicita��o de desaloca��o da m�quina " + alocacao.getMaquina().getCodigo() + " para o cliente " + alocacao.getCliente().getNomeFantasia());
		
		try {
			alocacaoService.solicitarDesalocacao(alocacao);
			
			// Sucesso - Exibe mensagem de desaloca��o realizada com sucesso
			ctx.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Solicita��o de desaloca��o da m�quina " + alocacao.getMaquina().getCodigo() + " para o cliente " + alocacao.getCliente().getNomeFantasia() + " realizada com sucesso.", null));
			logger.info("Solicita��o de desaloca��o da m�quina " + alocacao.getMaquina().getCodigo() + " para o cliente " + alocacao.getCliente().getNomeFantasia() + " realizada com sucesso.");
			
			// Processo de auditoria de cadastro de usu�rio
			Auditoria auditoria = new Auditoria();
			auditoria.setDataAcao(new Date());
			auditoria.setTitulo("Cadastro");
			auditoria.setDescricao("Cadastrou uma solicita��o de desaloca��o para a m�quina " + alocacao.getMaquina().getCodigo() + " do cliente " + alocacao.getCliente().getNomeFantasia());
			auditoria.setUsuario(Seguranca.getUsuarioLogado());
			auditoria.setIp(request.getRemoteAddr());
			auditoriaService.cadastrarNovaAcao(auditoria);
		} catch (InconsistenciaException e) {
			ctx.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, e.getMessage(), null));
			logger.info(e.getMessage());
		}
		alocacao = new Alocacao(); 
		
		try {
			external.getFlash().setKeepMessages(true);
			external.redirect(request.getContextPath() + "/admin/consultaAlocacao.xhtml");
		} catch (IOException e) {
			e.printStackTrace();
			logger.error("Erro ao tentar redirecionar para a p�gina " + request.getContextPath() + "/consultaAlocacao.xhtml");
		}
	}
	
	public void consultar(boolean exibirMensagem) {
		try {
			listAlocacoes = alocacaoService.buscarComFiltro(alocacaoConsParam);
		} catch (CadastroInexistenteException e) {
			if(listAlocacoes != null && !listAlocacoes.isEmpty()) {
				listAlocacoes.clear();
			}

			if(exibirMensagem) {
				ctx.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, e.getMessage(), null));
			}
		}
	}
	
	public void excluir() {
		Alocacao aloc;
		try {
			aloc = alocacaoService.excluir(alocacao.getId());
		} catch (InconsistenciaException e) {
			ctx.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, e.getMessage(), null));
			return;
		} catch(EJBTransactionRolledbackException e) {
			ctx.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Essa aloca��o n�o pode ser exclu�da.", null));
			logger.warn("Tentativa inv�lida de excluir uma aloca��o.");
			return;
		}
		
		ctx.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Aloca��o para a m�quina " + aloc.getMaquina().getCodigo() + " exclu�da com sucesso", null));
		
		// Processo de auditoria de exclus�o de contratos
		Auditoria auditoria = new Auditoria();
		auditoria.setDataAcao(new Date());
		auditoria.setTitulo("Exclus�o");
		auditoria.setDescricao("Excluiu uma solicita��o de aloca��o para a m�quina" + aloc.getMaquina().getCodigo());
		auditoria.setUsuario(Seguranca.getUsuarioLogado());
		auditoria.setIp(request.getRemoteAddr());
		auditoriaService.cadastrarNovaAcao(auditoria);
		
		// Recarrega a listagem de contratos
		consultar(false);
	}
	
	public void carregarAlocacao() {
		Long id = alocacao.getId();
		if(carregarPagina && id != 0) {
			alocacao = alocacaoService.findById(id);
		}
		
		carregarPagina = false;
	}
	
	public void selecionarCliente(String codigo) {
		Cliente c = clienteService.findByCodigo(codigo);
		
		if(c != null) {
			if(!c.getIndAtivo()) {
				ctx.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "C�digo inv�lido: O c�digo " + codigo + " informado n�o corresponde a um cliente ativo.", null));
				alocacao.setCliente(new Cliente());
				return;
			}
			alocacao.setCliente(c);
		} else {
			alocacao.setCliente(new Cliente());
			ctx.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "C�digo inv�lido: O c�digo " + codigo + " informado n�o corresponde a nenhum cliente cadastrado no sistema.", null));
			return;
		}
		
		RequestContext context = RequestContext.getCurrentInstance();
		context.execute("PF('dlgConsultaCliente').hide();");
	}
	
	public void selecionarMaquina(String codigo) {
		Maquina maq = maquinaService.findByCodigo(codigo);
		
		if(maq == null) {
			alocacao.setMaquina(new Maquina());
			ctx.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "C�digo inv�lido: O c�digo " + codigo + " informado n�o corresponde a nenhuma m�quina cadastrada no sistema.", null));
			return;
		}
		
		String descricao = maq.getMaquinaStatus().getDescricao();
		
		if(!"EM ESTOQUE".equalsIgnoreCase(descricao) && !"RESERVADA".equalsIgnoreCase(descricao)) {
			ctx.addMessage("Message2", new FacesMessage(FacesMessage.SEVERITY_ERROR, "C�digo inv�lido: O c�digo " + codigo + " informado n�o corresponde a uma m�quina dispon�vel em estoque ou que esteja reservada.", null));
			alocacao.setMaquina(new Maquina());
			return;
		}
		alocacao.setMaquina(maq);
		
		// Se a m�quina selecionada tiver uma reserva cadastrada, ent�o tem que ser para o cliente especifico
		if("RESERVADA".equalsIgnoreCase(descricao)) {
			Reserva r = new Reserva();
			r.setMaquina(maq);
			Reserva resultado = reservaService.buscarComFiltro(r);
			
			if(resultado != null) {
				alocacao.setCliente(resultado.getCliente());
			}
		}
		
		RequestContext context = RequestContext.getCurrentInstance();
		context.execute("PF('dlgConsultaMaquina').hide();");
	}
	
	public void selecionarContrato(String codigo) {
		Contrato cont = contratoService.findByCodigo(codigo);
		
		if(cont != null) {
			if(!cont.getIndDisponivel()) {
				ctx.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "C�digo inv�lido: O c�digo " + codigo + " informado n�o corresponde a um contrato dispon�vel.", null));
				alocacao.setContrato(new Contrato());
				return;
			}
			alocacao.setContrato(cont);
		} else{
			alocacao.setMaquina(new Maquina());
			ctx.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "C�digo inv�lido: O c�digo " + codigo + " informado n�o corresponde a nenhum contrato cadastrado no sistema.", null));
			return;
		}
		
		RequestContext context = RequestContext.getCurrentInstance();
		context.execute("PF('dlgConsultaContrato').hide();");
	}
	
	public void consultarMaquina() {
		try {
			List<String> listMaquinaStatus = Arrays.asList("EM ESTOQUE", "RESERVADA");
			
			listMaquinas = maquinaService.buscarComFiltroComVariosStatus(maquina, listMaquinaStatus);
			
			RequestContext context = RequestContext.getCurrentInstance();
			context.execute("PF('dlgConsultaMaquina').show();");
		} catch (CadastroInexistenteException e) {
			listMaquinas.clear();
			ctx.addMessage("Message2", new FacesMessage(FacesMessage.SEVERITY_WARN, e.getMessage(), null));
			RequestContext context = RequestContext.getCurrentInstance();
			context.execute("PF('dlgConsultaMaquina').show();");
		}
	}
	
	public void consultarContrato() {
		try {
			// Busca apenas os contratos disponiveis
			contrato.setIndDisponivel(true);
			listContratos = contratoService.buscarContratosComFiltro(contrato);
			RequestContext context = RequestContext.getCurrentInstance();
			context.execute("PF('dlgConsultaContrato').show();");
		} catch (CadastroInexistenteException e) {
			listMaquinas.clear();
			ctx.addMessage("Message2", new FacesMessage(FacesMessage.SEVERITY_WARN, e.getMessage(), null));
			RequestContext context = RequestContext.getCurrentInstance();
			context.execute("PF('dlgConsultaContrato').show();");
		}
	}
	
	public void consultarCliente() {
		try {
			// Busca os clientes ativos
			cliente.setIndAtivo(true);
			listClientes = clienteService.buscarClientesComFiltro(cliente);
			RequestContext context = RequestContext.getCurrentInstance();
			context.execute("PF('dlgConsultaCliente').show();");
		} catch (CadastroInexistenteException e) {
			listMaquinas.clear();
			ctx.addMessage("Message2", new FacesMessage(FacesMessage.SEVERITY_WARN, e.getMessage(), null));
			RequestContext context = RequestContext.getCurrentInstance();
			context.execute("PF('dlgConsultaCliente').show();");
		}
	}
	
	public void abrirDialog(String dialog) {
		maquina = new Maquina();
		contrato = new Contrato();
		cliente = new Cliente();
		listMaquinas = new ArrayList<Maquina>();
		listContratos = new ArrayList<Contrato>();
		listClientes = new ArrayList<Cliente>();
		
		RequestContext context = RequestContext.getCurrentInstance();
		context.execute("PF('" + dialog + "').show();");
	}
	
	public void fecharDialog(String dialog) {
		listContratos.clear();
		listMaquinas.clear();
		RequestContext context = RequestContext.getCurrentInstance();
		context.execute("PF('" + dialog + "').hide();");
	}

	public Alocacao getAlocacao() {
		return alocacao;
	}

	public void setAlocacao(Alocacao alocacao) {
		this.alocacao = alocacao;
	}

	public List<Maquina> getListMaquinas() {
		return listMaquinas;
	}

	public void setListMaquinas(List<Maquina> listMaquinas) {
		this.listMaquinas = listMaquinas;
	}

	public Maquina getMaquina() {
		return maquina;
	}

	public void setMaquina(Maquina maquina) {
		this.maquina = maquina;
	}

	public Contrato getContrato() {
		return contrato;
	}

	public void setContrato(Contrato contrato) {
		this.contrato = contrato;
	}

	public List<Contrato> getListContratos() {
		return listContratos;
	}

	public void setListContratos(List<Contrato> listContratos) {
		this.listContratos = listContratos;
	}

	public Cliente getCliente() {
		return cliente;
	}

	public void setCliente(Cliente cliente) {
		this.cliente = cliente;
	}

	public List<Cliente> getListClientes() {
		return listClientes;
	}

	public void setListClientes(List<Cliente> listClientes) {
		this.listClientes = listClientes;
	}

	public Alocacao getAlocacaoConsParam() {
		return alocacaoConsParam;
	}

	public void setAlocacaoConsParam(Alocacao alocacaoConsParam) {
		this.alocacaoConsParam = alocacaoConsParam;
	}

	public List<Alocacao> getListAlocacoes() {
		return listAlocacoes;
	}

	public void setListAlocacoes(List<Alocacao> listAlocacoes) {
		this.listAlocacoes = listAlocacoes;
	}
}
