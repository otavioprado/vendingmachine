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

import br.com.milenio.vendingmachine.domain.model.Auditoria;
import br.com.milenio.vendingmachine.domain.model.Fornecedor;
import br.com.milenio.vendingmachine.domain.model.Produto;
import br.com.milenio.vendingmachine.exceptions.CadastroInexistenteException;
import br.com.milenio.vendingmachine.exceptions.ConteudoJaExistenteNoBancoDeDadosException;
import br.com.milenio.vendingmachine.security.Seguranca;
import br.com.milenio.vendingmachine.service.AuditoriaService;
import br.com.milenio.vendingmachine.service.FornecedorService;
import br.com.milenio.vendingmachine.service.ProdutoService;

@Named
@ViewScoped
public class ProdutoMB implements Serializable {
	private static final long serialVersionUID = -4048919661356057763L;
	
	@Inject
	private AuditoriaService auditoriaService;
	
	@Inject
	private Logger logger;
	
	@Inject
	private FornecedorService fornecedorService;
	
	@Inject
	private ProdutoService produtoService;
	
	@Inject
	private HttpServletRequest request;
	
	@Inject
	private FacesContext ctx;
	
	private Produto produto = new Produto();
	private Produto prodConsParam = new Produto();
	private Fornecedor fornecedor = new Fornecedor();
	
	private String precoVenda;
	private String valorUnitario;
	
	private List<Fornecedor> listFornecedores = new ArrayList<Fornecedor>();
	
	public void cadastrar() {
		String codigo = produto.getCodigo() != null ? produto.getCodigo().trim() : "";
		String descricao = produto.getDescricao() != null ? produto.getDescricao().trim() : "";
		
		if(codigo.isEmpty() || descricao.isEmpty()) {
			ctx.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Os campos código e descrição não podem conter apenas espaços em branco.", null));
			return;
		}
		
		try {
			// Retira a máscara R$ dos valores
			precoVenda = precoVenda.replace("R$", "").trim();
			produto.setPrecoVenda(Double.parseDouble(precoVenda));
			
			valorUnitario = valorUnitario.replace("R$", "").trim();
			produto.setValorUnitario(Double.parseDouble(valorUnitario));
			
			produtoService.cadastrar(produto);
			
			// Sucesso - Exibe mensagem de cadastro realizado com sucesso
			ctx.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Produto " + produto.getCodigo() + " cadastrado com sucesso.", null));
			logger.info("Produto " + produto.getCodigo() + " foi cadastrado no sistema com sucesso.");
			
			// Processo de auditoria de cadastro de produtos
			Auditoria auditoria = new Auditoria();
			auditoria.setDataAcao(new Date());
			auditoria.setTitulo("Cadastro");
			auditoria.setDescricao("Cadastrou o produto " + produto.getCodigo());
			auditoria.setUsuario(Seguranca.getUsuarioLogado());
			auditoria.setIp(request.getRemoteAddr());
			auditoriaService.cadastrarNovaAcao(auditoria);
		} catch(ConteudoJaExistenteNoBancoDeDadosException e) {
			ctx.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, e.getMessage(), null));
			logger.info(e.getMessage());
		}
		
		produto = new Produto();
		precoVenda = null;
		valorUnitario = null;
	}
	
	public void selecionarFornecedor(String codigo) {
		Fornecedor fornecedor = fornecedorService.findByCodigo(codigo);
		
		if(fornecedor != null) {
			produto.setFornecedor(fornecedor);
		} else{
			produto.setFornecedor(new Fornecedor());
			ctx.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Código inválido: O código " + codigo + " informado não corresponde a nenhum fornecedor cadastrado no sistema.", null));
			return;
		}
		
		RequestContext context = RequestContext.getCurrentInstance();
		context.execute("PF('dlgConsultaFornecedor').hide();");
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
	
	public void abrirDialog() {
		RequestContext context = RequestContext.getCurrentInstance();
		context.execute("PF('dlgConsultaFornecedor').show();");
	}
	
	public void fecharDialog() {
		listFornecedores.clear();
		
		RequestContext context = RequestContext.getCurrentInstance();
		context.execute("PF('dlgConsultaFornecedor').hide();");
	}

	public Produto getProduto() {
		return produto;
	}

	public void setProduto(Produto produto) {
		this.produto = produto;
	}

	public Produto getProdConsParam() {
		return prodConsParam;
	}

	public void setProdConsParam(Produto prodConsParam) {
		this.prodConsParam = prodConsParam;
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

	public String getPrecoVenda() {
		return precoVenda;
	}

	public void setPrecoVenda(String precoVenda) {
		this.precoVenda = precoVenda;
	}

	public String getValorUnitario() {
		return valorUnitario;
	}

	public void setValorUnitario(String valorUnitario) {
		this.valorUnitario = valorUnitario;
	}
}
