package br.com.milenio.vendingmachine.managedbean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.faces.application.FacesMessage;
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
import br.com.milenio.vendingmachine.exceptions.CadastroInexistenteException;
import br.com.milenio.vendingmachine.exceptions.InconsistenciaException;
import br.com.milenio.vendingmachine.repository.MaquinaStatusRepository;
import br.com.milenio.vendingmachine.security.Seguranca;
import br.com.milenio.vendingmachine.service.AlocacaoService;
import br.com.milenio.vendingmachine.service.AuditoriaService;
import br.com.milenio.vendingmachine.service.ClienteService;
import br.com.milenio.vendingmachine.service.ContratoService;
import br.com.milenio.vendingmachine.service.MaquinaService;

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
	private AuditoriaService auditoriaService;
	
	@Inject
	private HttpServletRequest request;
	
	@Inject
	private MaquinaStatusRepository maquinaStatusRepository;
	
	@Inject
	private ContratoService contratoService;
	
	private boolean carregarPagina = true;
	private Alocacao alocacao = new Alocacao();
	private Contrato contrato = new Contrato();
	private Maquina maquina = new Maquina();
	private List<Maquina> listMaquinas = new ArrayList<Maquina>();
	private List<Contrato> listContratos = new ArrayList<Contrato>();
	
	public void cadastrar() {
		logger.debug("Tentando realizar o cadastro da aloca��o da m�quina " + alocacao.getMaquina().getCodigo() + " para o cliente " + alocacao.getCliente().getNomeFantasia());
		
		try {
			alocacao.setUsuario(Seguranca.getUsuarioLogado());
			alocacaoService.cadastrar(alocacao);
			
			// Sucesso - Exibe mensagem de cadastro realizado com sucesso
			ctx.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Aloca��o da m�quina " + alocacao.getMaquina().getCodigo() + " para o cliente " + alocacao.getCliente().getNomeFantasia() + " cadastrada com sucesso.", null));
			logger.info("Aloca��o da m�quina " + alocacao.getMaquina().getCodigo() + " para o cliente " + alocacao.getCliente().getNomeFantasia() + " cadastrada com sucesso.");
			
			// Processo de auditoria de cadastro de usu�rio
			Auditoria auditoria = new Auditoria();
			auditoria.setDataAcao(new Date());
			auditoria.setTitulo("Cadastro");
			auditoria.setDescricao("Cadastrou uma aloca��o para a m�quina " + alocacao.getMaquina().getCodigo() + " para o cliente " + alocacao.getCliente().getNomeFantasia());
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
		if(carregarPagina) {
			Cliente cliente = clienteService.findById(alocacao.getCliente().getId());
			alocacao.setCliente(cliente);
		}
		
		carregarPagina = false;
	}
	
	public void selecionarMaquina(String codigo) {
		Maquina maq = maquinaService.findByCodigo(codigo);
		
		if(maq != null) {
			if(!"EM ESTOQUE".equalsIgnoreCase(maq.getMaquinaStatus().getDescricao())) {
				ctx.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "C�digo inv�lido: O c�digo " + codigo + " informado n�o corresponde a uma m�quina dispon�vel em estoque.", null));
				alocacao.setMaquina(new Maquina());
				return;
			}
			alocacao.setMaquina(maq);
		} else{
			alocacao.setMaquina(new Maquina());
			ctx.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "C�digo inv�lido: O c�digo " + codigo + " informado n�o corresponde a nenhuma m�quina cadastrada no sistema.", null));
			return;
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
	
	public void abrirDialog(String dialog) {
		maquina = new Maquina();
		contrato = new Contrato();
		listMaquinas = new ArrayList<Maquina>();
		listContratos = new ArrayList<Contrato>();
		
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
}