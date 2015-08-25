package br.com.milenio.vendingmachine.managedbean;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.component.html.HtmlInputText;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;

import org.apache.logging.log4j.Logger;

import br.com.milenio.vendingmachine.domain.model.Auditoria;
import br.com.milenio.vendingmachine.domain.model.Cliente;
import br.com.milenio.vendingmachine.domain.model.Endereco;
import br.com.milenio.vendingmachine.exceptions.CadastroInexistenteException;
import br.com.milenio.vendingmachine.exceptions.ConteudoJaExistenteNoBancoDeDadosException;
import br.com.milenio.vendingmachine.exceptions.InconsistenciaException;
import br.com.milenio.vendingmachine.security.Seguranca;
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
	private ClienteService clienteService;
	
	@Inject
	private HttpServletRequest request;
	
	@Inject
	private FacesContext ctx;
	
	private HtmlInputText inputTextCep = new HtmlInputText();
	
	private List<Cliente> listClientes;

	private Cliente cliente = new Cliente();
	private boolean indManual = false;

	public void cadastrar() {
		logger.debug("Tentando realizar o cadastro do cliente " + cliente.getNomeFantasia());
		
		try {
			clienteService.cadastrar(cliente);
			
			// Sucesso - Exibe mensagem de cadastro realizado com sucesso
			ctx.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Cliente " + cliente.getNomeFantasia() + " cadastrado com sucesso.", null));
			logger.info("Cliente " + cliente.getNomeFantasia() + " foi cadastrado no sistema com sucesso.");
		} catch (ConteudoJaExistenteNoBancoDeDadosException e) {
			ctx.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, e.getMessage(), null));
			logger.info(e.getMessage());
		}
		
		cliente = new Cliente(); 
	}
	
	public void consultarCEP() {
		try {
			indManual = false;
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
	
	public void consultarCliente() {
		try {
			listClientes = clienteService.buscarClientesComFiltro(cliente);
		} catch (CadastroInexistenteException e) {
			if(listClientes != null && !listClientes.isEmpty()) {
				listClientes.clear();
			}
			
			ctx.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, e.getMessage(), null));
		}
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
}
