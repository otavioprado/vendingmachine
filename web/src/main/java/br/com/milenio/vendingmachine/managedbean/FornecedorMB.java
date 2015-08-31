package br.com.milenio.vendingmachine.managedbean;

import java.io.IOException;
import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.faces.application.FacesMessage;
import javax.faces.component.html.HtmlInputText;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.validator.routines.EmailValidator;
import org.apache.logging.log4j.Logger;
import org.primefaces.context.RequestContext;

import br.com.milenio.vendingmachine.domain.model.Auditoria;
import br.com.milenio.vendingmachine.domain.model.Endereco;
import br.com.milenio.vendingmachine.domain.model.Fornecedor;
import br.com.milenio.vendingmachine.exceptions.CadastroInexistenteException;
import br.com.milenio.vendingmachine.exceptions.ConteudoJaExistenteNoBancoDeDadosException;
import br.com.milenio.vendingmachine.exceptions.InconsistenciaException;
import br.com.milenio.vendingmachine.security.Seguranca;
import br.com.milenio.vendingmachine.service.AuditoriaService;
import br.com.milenio.vendingmachine.service.EnderecoService;
import br.com.milenio.vendingmachine.service.FornecedorService;

@Named
@ViewScoped
public class FornecedorMB implements Serializable {
	private static final long serialVersionUID = -4048919661356057763L;
	
	@Inject
	private AuditoriaService auditoriaService;
	
	@Inject
	private Logger logger;
	
	@Inject
	private EnderecoService enderecoService;
	
	@Inject
	private FornecedorService fornecedorService;
	
	@Inject
	private HttpServletRequest request;
	
	@Inject
	private ExternalContext external;
	
	@Inject
	private FacesContext ctx;
	
	private HtmlInputText inputTextCep = new HtmlInputText();
	
	private List<Fornecedor> listFornecedores;

	private Fornecedor fornecedor = new Fornecedor();
	private Fornecedor fornConsParam = new Fornecedor();
	private boolean indManual = false;

	public void cadastrar() {
		logger.debug("Tentando realizar o cadastro do fornecedor " + fornecedor.getNomeFantasia());
		
		EmailValidator validator = EmailValidator.getInstance();
		if (!validator.isValid(fornecedor.getEmail())) {
			ctx.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "O e-mail informado não é válido.", null));
			return;
		}
		
		String campoEmBranco = buscarCamposEmBranco();
		
		if(campoEmBranco != null) {
			ctx.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "O campo " + campoEmBranco +" não pode conter apenas espaços em branco.", null));
			return;
		}
		
		try {
			fornecedorService.cadastrar(fornecedor);
			
			// Sucesso - Exibe mensagem de cadastro realizado com sucesso
			ctx.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Fornecedor " + fornecedor.getNomeFantasia() + " cadastrado com sucesso.", null));
			logger.info("Fornecedor " + fornecedor.getNomeFantasia() + " foi cadastrado no sistema com sucesso.");
			
			// Processo de auditoria de cadastro de usuário
			Auditoria auditoria = new Auditoria();
			auditoria.setDataAcao(new Date());
			auditoria.setTitulo("Cadastro");
			auditoria.setDescricao("Cadastrou o fornecedor " + fornecedor.getNomeFantasia());
			auditoria.setUsuario(Seguranca.getUsuarioLogado());
			auditoria.setIp(request.getRemoteAddr());
			auditoriaService.cadastrarNovaAcao(auditoria);
		} catch (ConteudoJaExistenteNoBancoDeDadosException e) {
			ctx.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, e.getMessage(), null));
			logger.info(e.getMessage());
		}
		
		fornecedor = new Fornecedor(); 
	}
	
	private String buscarCamposEmBranco() {
		Map<String, String> mapParam = new HashMap<String, String>();
		
		mapParam.put("codigo", fornecedor.getCodigo() != null ? fornecedor.getCodigo().trim() : "");
		mapParam.put("nome fantasia", fornecedor.getNomeFantasia() != null ? fornecedor.getNomeFantasia().trim() : "");
		mapParam.put("logradouro", fornecedor.getEndereco().getLogradouro() != null ? fornecedor.getEndereco().getLogradouro().trim() : "");
		mapParam.put("bairro", fornecedor.getEndereco().getBairro() != null ? fornecedor.getEndereco().getBairro().trim() : "");
		mapParam.put("cidade", fornecedor.getEndereco().getCidade() != null ? fornecedor.getEndereco().getCidade().trim() : "");
		mapParam.put("estado", fornecedor.getEndereco().getEstado() != null ? fornecedor.getEndereco().getEstado().trim() : "");
		
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

			fornecedor.setEndereco(endereco);
			
		} catch (InconsistenciaException e) {
			ctx.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, e.getMessage(), null));
		}
	}
	
	public void excluirPelaEdicao() {
		Fornecedor fornec;
		
		try {
			fornec = fornecedorService.excluir(fornecedor.getId());
		} catch (InconsistenciaException e) {
			ctx.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, e.getMessage(), null));
			return;
		}
		
		ctx.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Fornecedor " + fornec.getNomeFantasia() + " excluído com sucesso", null));
		
		try {
			external.getFlash().setKeepMessages(true);
			external.redirect(request.getContextPath() + "/admin/consultaFornecedor.xhtml");
		} catch (IOException e) {
			e.printStackTrace();
			logger.error("Erro ao tentar redirecionar para a página " + request.getContextPath() + "/consultaFornecedor.xhtml");
		}
	}
	
	public void abrirDialogExcluir() {
		RequestContext context = RequestContext.getCurrentInstance();
		context.execute("PF('dlgExcluir').show();");
	}
	
	public void excluir() {
		Fornecedor fornec;
		try {
			fornec = fornecedorService.excluir(fornecedor.getId());
		} catch (InconsistenciaException e) {
			ctx.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, e.getMessage(), null));
			return;
		}
		
		ctx.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Fornecedor " + fornec.getNomeFantasia() + " excluído com sucesso", null));
		
		// Recarrega a listagem de atividades
		consultar(false);
	}
	
	public void editar() {
		logger.debug("Tentando realizar a edição do fornecedor " + fornecedor.getNomeFantasia());
		
		EmailValidator validator = EmailValidator.getInstance();
		if (!validator.isValid(fornecedor.getEmail())) {
			ctx.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "O e-mail informado não é válido.", null));
			return;
		}
		
		String campoEmBranco = buscarCamposEmBranco();
		
		if(campoEmBranco != null) {
			ctx.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "O campo " + campoEmBranco +" não pode conter apenas espaços em branco.", null));
			return;
		}
		
		try {
			fornecedorService.editar(fornecedor);
		
			// Sucesso - Exibe mensagem de edição realizado com sucesso
			ctx.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Fornecedor " + fornecedor.getNomeFantasia() + " editado com sucesso.", null));
			logger.info("Fornecedor " + fornecedor.getNomeFantasia() + " foi editado no sistema com sucesso.");
			
			// Processo de auditoria de cadastro de usuário
			Auditoria auditoria = new Auditoria();
			auditoria.setDataAcao(new Date());
			auditoria.setTitulo("Edição");
			auditoria.setDescricao("Editou o fornecedor " + fornecedor.getNomeFantasia());
			auditoria.setUsuario(Seguranca.getUsuarioLogado());
			auditoria.setIp(request.getRemoteAddr());
			auditoriaService.cadastrarNovaAcao(auditoria);
		} catch (ConteudoJaExistenteNoBancoDeDadosException e) {
			ctx.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, e.getMessage(), null));
		}
	}
	
	public void carregarDadosFornecedorParaEdicao() {
		fornecedor = fornecedorService.findById(fornecedor.getId());
	}
	
	public void consultar(boolean exibirMensagem) {
		try {
			listFornecedores = fornecedorService.buscarFornecedoresComFiltro(fornConsParam);
		} catch (CadastroInexistenteException e) {
			if(listFornecedores != null && !listFornecedores.isEmpty()) {
				listFornecedores.clear();
			}
			
			if(exibirMensagem) {
				ctx.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, e.getMessage(), null));
			}
		}
	}

	// Getters and Setters
	public HtmlInputText getInputTextCep() {
		return inputTextCep;
	}

	public void setInputTextCep(HtmlInputText inputTextCep) {
		this.inputTextCep = inputTextCep;
	}

	public List<Fornecedor> getListFornecedores() {
		return listFornecedores;
	}

	public void setListFornecedores(List<Fornecedor> listFornecedores) {
		this.listFornecedores = listFornecedores;
	}

	public Fornecedor getFornecedor() {
		return fornecedor;
	}

	public void setFornecedor(Fornecedor fornecedor) {
		this.fornecedor = fornecedor;
	}

	public Fornecedor getFornConsParam() {
		return fornConsParam;
	}

	public void setFornConsParam(Fornecedor fornConsParam) {
		this.fornConsParam = fornConsParam;
	}

	public boolean isIndManual() {
		return indManual;
	}

	public void setIndManual(boolean indManual) {
		this.indManual = indManual;
	}
}
