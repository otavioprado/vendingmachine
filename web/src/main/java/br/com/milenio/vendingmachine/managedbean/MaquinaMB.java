package br.com.milenio.vendingmachine.managedbean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.primefaces.context.RequestContext;

import br.com.milenio.vendingmachine.domain.model.Fornecedor;
import br.com.milenio.vendingmachine.domain.model.Maquina;
import br.com.milenio.vendingmachine.domain.model.Produto;
import br.com.milenio.vendingmachine.exceptions.CadastroInexistenteException;
import br.com.milenio.vendingmachine.service.FornecedorService;
import br.com.milenio.vendingmachine.service.ProdutoService;

@Named
@ViewScoped
public class MaquinaMB implements Serializable {
	private static final long serialVersionUID = -8888730192386668748L;
	
	@Inject
	private FornecedorService fornecedorService;
	
	@Inject
	private ProdutoService produtoService;
	
	@Inject
	private FacesContext ctx;
	
	private Maquina maquina = new Maquina();
	private List<Fornecedor> listFornecedores = new ArrayList<Fornecedor>();
	private List<Produto> listProdutos = new ArrayList<Produto>();
	private Fornecedor fornecedor = new Fornecedor();
	private Produto produtoSelecionado = new Produto();
	private Produto produto = new Produto();
	
	private String custoAquisicao;
	
	public void cadastrar() {
		
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
	
	public void consultarProduto() {
		try {
			listProdutos = produtoService.buscarComFiltro(produto);
			RequestContext context = RequestContext.getCurrentInstance();
			context.execute("PF('dlgAdicionarProduto').show();");
		} catch (CadastroInexistenteException e) {
			listProdutos.clear();
			ctx.addMessage("Message3", new FacesMessage(FacesMessage.SEVERITY_WARN, e.getMessage(), null));
			RequestContext context = RequestContext.getCurrentInstance();
			context.execute("PF('dlgAdicionarProduto').show();");
		}
	}
	
	public void adicionarProduto(Long id) {
		Produto newProduto = produtoService.findById(id);
		
		if(newProduto != null) {
			maquina.getProdutos().add(newProduto);
		} else{
			ctx.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Código inválido: O código " + produto.getCodigo() + " informado não corresponde a nenhum produto cadastrado no sistema.", null));
			return;
		}
		
		RequestContext context = RequestContext.getCurrentInstance();
		context.execute("PF('dlgAdicionarProduto').hide();");
	}
	
	public void selecionarFornecedor(String codigo) {
		Fornecedor forn = fornecedorService.findByCodigo(codigo);
		
		if(forn != null) {
			maquina.setFornecedor(forn);
		} else{
			maquina.setFornecedor(new Fornecedor());
			ctx.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Código inválido: O código " + codigo + " informado não corresponde a nenhum fornecedor cadastrado no sistema.", null));
			return;
		}
		
		RequestContext context = RequestContext.getCurrentInstance();
		context.execute("PF('dlgConsultaFornecedor').hide();");
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
	
	public void abrirDialogAdicionarProduto() {
		RequestContext context = RequestContext.getCurrentInstance();
		context.execute("PF('dlgAdicionarProduto').show();");
	}
	
	public void fecharDialogAdicionarProduto() {
		listProdutos.clear();
		
		RequestContext context = RequestContext.getCurrentInstance();
		context.execute("PF('dlgAdicionarProduto').hide();");
	}

	public Maquina getMaquina() {
		return maquina;
	}

	public void setMaquina(Maquina maquina) {
		this.maquina = maquina;
	}

	public List<Fornecedor> getListFornecedores() {
		return listFornecedores;
	}

	public void setListFornecedores(List<Fornecedor> listFornecedores) {
		this.listFornecedores = listFornecedores;
	}

	public String getCustoAquisicao() {
		return custoAquisicao;
	}

	public void setCustoAquisicao(String custoAquisicao) {
		this.custoAquisicao = custoAquisicao;
	}

	public Fornecedor getFornecedor() {
		return fornecedor;
	}

	public void setFornecedor(Fornecedor fornecedor) {
		this.fornecedor = fornecedor;
	}

	public Produto getProdutoSelecionado() {
		return produtoSelecionado;
	}

	public void setProdutoSelecionado(Produto produtoSelecionado) {
		this.produtoSelecionado = produtoSelecionado;
	}

	public Produto getProduto() {
		return produto;
	}

	public void setProduto(Produto produto) {
		this.produto = produto;
	}

	public List<Produto> getListProdutos() {
		return listProdutos;
	}

	public void setListProdutos(List<Produto> listProdutos) {
		this.listProdutos = listProdutos;
	}
}
