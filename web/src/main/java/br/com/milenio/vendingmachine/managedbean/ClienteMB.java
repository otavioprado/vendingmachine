package br.com.milenio.vendingmachine.managedbean;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.faces.application.FacesMessage;
import javax.faces.component.html.HtmlInputText;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.validator.routines.EmailValidator;
import org.apache.logging.log4j.Logger;

import br.com.milenio.vendingmachine.domain.model.Alocacao;
import br.com.milenio.vendingmachine.domain.model.Auditoria;
import br.com.milenio.vendingmachine.domain.model.Cliente;
import br.com.milenio.vendingmachine.domain.model.Endereco;
import br.com.milenio.vendingmachine.exceptions.CadastroInexistenteException;
import br.com.milenio.vendingmachine.exceptions.ConteudoJaExistenteNoBancoDeDadosException;
import br.com.milenio.vendingmachine.exceptions.InconsistenciaException;
import br.com.milenio.vendingmachine.security.Seguranca;
import br.com.milenio.vendingmachine.service.AlocacaoService;
import br.com.milenio.vendingmachine.service.AuditoriaService;
import br.com.milenio.vendingmachine.service.ClienteService;
import br.com.milenio.vendingmachine.service.EnderecoService;

@Named
@ViewScoped
public class ClienteMB implements Serializable {
	private static final long serialVersionUID = -4048919661356057763L;
	
	@Inject
	private AuditoriaService auditoriaService;
	
	@Inject
	private Logger logger;
	
	@Inject
	private EnderecoService enderecoService;
	
	@Inject
	private AlocacaoService alocacaoService;
	
	@Inject
	private ClienteService clienteService;
	
	@Inject
	private HttpServletRequest request;
	
	@Inject
	private FacesContext ctx;
	
	private HtmlInputText inputTextCep = new HtmlInputText();
	
	private List<Cliente> listClientes;

	private Cliente cliente = new Cliente();
	private Cliente cliConsParam = new Cliente();
	private boolean indManual = false;

	public void cadastrar() {
		logger.debug("Tentando realizar o cadastro do cliente " + cliente.getNomeFantasia());
		
		EmailValidator validator = EmailValidator.getInstance();
		if (!validator.isValid(cliente.getEmail())) {
			ctx.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "O e-mail informado não é válido.", null));
			return;
		}
		
		String campoEmBranco = buscarCamposEmBranco();
		
		if(campoEmBranco != null) {
			ctx.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "O campo " + campoEmBranco +" não pode conter apenas espaços em branco.", null));
			return;
		}
		
		try {
			clienteService.cadastrar(cliente);
			
			// Sucesso - Exibe mensagem de cadastro realizado com sucesso
			ctx.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Cliente " + cliente.getNomeFantasia() + " cadastrado com sucesso.", null));
			logger.info("Cliente " + cliente.getNomeFantasia() + " foi cadastrado no sistema com sucesso.");
			
			// Processo de auditoria de cadastro de usuário
			Auditoria auditoria = new Auditoria();
			auditoria.setDataAcao(new Date());
			auditoria.setTitulo("Cadastro");
			auditoria.setDescricao("Cadastrou o cliente " + cliente.getNomeFantasia());
			auditoria.setUsuario(Seguranca.getUsuarioLogado());
			auditoria.setIp(request.getRemoteAddr());
			auditoriaService.cadastrarNovaAcao(auditoria);
		} catch (ConteudoJaExistenteNoBancoDeDadosException e) {
			ctx.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, e.getMessage(), null));
			logger.info(e.getMessage());
		}
		
		cliente = new Cliente(); 
	}
	
	private String buscarCamposEmBranco() {
		Map<String, String> mapParam = new HashMap<String, String>();
		
		mapParam.put("codigo", cliente.getCodigo() != null ? cliente.getCodigo().trim() : "");
		mapParam.put("nome fantasia", cliente.getNomeFantasia() != null ? cliente.getNomeFantasia().trim() : "");
		mapParam.put("logradouro", cliente.getEndereco().getLogradouro() != null ? cliente.getEndereco().getLogradouro().trim() : "");
		mapParam.put("bairro", cliente.getEndereco().getBairro() != null ? cliente.getEndereco().getBairro().trim() : "");
		mapParam.put("cidade", cliente.getEndereco().getCidade() != null ? cliente.getEndereco().getCidade().trim() : "");
		mapParam.put("estado", cliente.getEndereco().getEstado() != null ? cliente.getEndereco().getEstado().trim() : "");
		
		Set<String> keySet = mapParam.keySet();
		
		for(String key : keySet) {
			String valor = mapParam.get(key);
			
			if(valor.isEmpty()) {
				return key;
			}
		}
		
		return null;
	}
	
	public void consultarCEP() {
		try {
			String cep = (String) inputTextCep.getSubmittedValue();
			
			Endereco endereco = enderecoService.consultarCepWebService(cep);
			
			if(endereco == null) {
				// Abrir para digitação manual
				indManual = true;
				ctx.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Impossível consultar o CEP, favor informe os dados de endereço manualmente", null));
				return;
			}

			cliente.setEndereco(endereco);
			
		} catch (InconsistenciaException e) {
			ctx.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, e.getMessage(), null));
		}
	}
	
	public void bloquearCliente() {
		String motivoBloqueio = cliente.getMotivoBloqueio().trim();
		
		if(motivoBloqueio != null && motivoBloqueio.isEmpty()) {
			ctx.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "O campo motivo bloqueio não pode conter apenas espaços em branco.", null));
			return;
		}
		
		if (motivoBloqueio != null && motivoBloqueio.length() < 10) {
			ctx.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "O campo motivo bloqueio deve conter pelo menos 10 caracteres.", null));
			return;
		}
		
		if(clienteService.bloquearUsuario(cliente.getId(), motivoBloqueio)) {
			ctx.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Cliente " + cliente.getNomeFantasia() + " bloqueado com sucesso.", null));
			// Após bloqueio, carrega a lista de clientes para atualizar na view de consulta
			consultarCliente();
			
			// Processo de auditoria
			Auditoria auditoria = new Auditoria();
			auditoria.setDataAcao(new Date());
			auditoria.setTitulo("Bloqueio");
			auditoria.setDescricao("Bloqueou o cliente " + cliente.getNomeFantasia());
			auditoria.setUsuario(Seguranca.getUsuarioLogado());
			auditoria.setIp(request.getRemoteAddr());
			auditoriaService.cadastrarNovaAcao(auditoria);
		}
		
		motivoBloqueio = null;
	}
	
	public void desbloquearCliente() {
		if(clienteService.desbloquearUsuario(cliente.getId())) {
			ctx.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Cliente " + cliente.getNomeFantasia() + " desbloqueado com sucesso.", null));
			// Após desbloqueio, carrega a lista de clientes para atualizar na view de consulta
			consultarCliente();
			
			// Processo de auditoria
			Auditoria auditoria = new Auditoria();
			auditoria.setDataAcao(new Date());
			auditoria.setTitulo("Desbloqueio");
			auditoria.setDescricao("Desbloqueou o cliente " + cliente.getNomeFantasia());
			auditoria.setUsuario(Seguranca.getUsuarioLogado());
			auditoria.setIp(request.getRemoteAddr());
			auditoriaService.cadastrarNovaAcao(auditoria);
		}
	}
	
	public void editar() {
		logger.debug("Tentando realizar a edição do cliente " + cliente.getNomeFantasia());
		
		EmailValidator validator = EmailValidator.getInstance();
		if (!validator.isValid(cliente.getEmail())) {
			ctx.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "O e-mail informado não é válido.", null));
			return;
		}
		
		String campoEmBranco = buscarCamposEmBranco();
		
		if(campoEmBranco != null) {
			ctx.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "O campo " + campoEmBranco +" não pode conter apenas espaços em branco.", null));
			return;
		}
		
		try {
			clienteService.editar(cliente);
		
			// Sucesso - Exibe mensagem de edição realizado com sucesso
			ctx.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Cliente " + cliente.getNomeFantasia() + " editado com sucesso.", null));
			logger.info("Cliente " + cliente.getNomeFantasia() + " foi editado no sistema com sucesso.");
			
			// Processo de auditoria de cadastro de usuário
			Auditoria auditoria = new Auditoria();
			auditoria.setDataAcao(new Date());
			auditoria.setTitulo("Edição");
			auditoria.setDescricao("Editou o cliente " + cliente.getNomeFantasia());
			auditoria.setUsuario(Seguranca.getUsuarioLogado());
			auditoria.setIp(request.getRemoteAddr());
			auditoriaService.cadastrarNovaAcao(auditoria);
		} catch(ConteudoJaExistenteNoBancoDeDadosException e) {
			ctx.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, e.getMessage(), null));
		}
	}
	
	public void carregarDadosClienteParaEdicao() {
		cliente = clienteService.findById(cliente.getId());
	}
	
	public void consultarCliente() {
		try {
			listClientes = clienteService.buscarClientesComFiltro(cliConsParam);
		} catch (CadastroInexistenteException e) {
			if(listClientes != null && !listClientes.isEmpty()) {
				listClientes.clear();
			}
			
			ctx.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, e.getMessage(), null));
		}
	}
	
	public List<Alocacao> getListAlocacoes() {
		return alocacaoService.findByCliente(cliente.getId());
	}
	
	public Cliente getCliente() {
		return cliente;
	}

	public void setCliente(Cliente cliente) {
		this.cliente = cliente;
	}
	
	public boolean isIndManual() {
		return indManual;
	}

	public void setIndManual(boolean indManual) {
		this.indManual = indManual;
	}
	
	public HtmlInputText getInputTextCep() {
		return inputTextCep;
	}

	public void setInputTextCep(HtmlInputText inputTextCep) {
		this.inputTextCep = inputTextCep;
	}

	public List<Cliente> getListClientes() {
		return listClientes;
	}

	public void setListClientes(List<Cliente> listClientes) {
		this.listClientes = listClientes;
	}

	public Cliente getCliConsParam() {
		return cliConsParam;
	}

	public void setCliConsParam(Cliente cliConsParam) {
		this.cliConsParam = cliConsParam;
	}
}
