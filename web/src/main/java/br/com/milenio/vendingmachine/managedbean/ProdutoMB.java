package br.com.milenio.vendingmachine.managedbean;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.context.ExternalContext;
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
import br.com.milenio.vendingmachine.exceptions.InconsistenciaException;
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
	private ExternalContext external;
	
	@Inject
	private FacesContext ctx;
	
	private Produto produto = new Produto();
	private Produto prodConsParam = new Produto();
	private Fornecedor fornecedor = new Fornecedor();
	private List<Produto> listProdutos;
	
	private List<Fornecedor> listFornecedores = new ArrayList<Fornecedor>();
	
	private boolean carregarPagina = true;
	
	public void cadastrar() {
		String codigo = produto.getCodigo() != null ? produto.getCodigo().trim() : "";
		String descricao = produto.getDescricao() != null ? produto.getDescricao().trim() : "";
		
		if(codigo.isEmpty() || descricao.isEmpty()) {
			ctx.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Os campos código e descrição não podem conter apenas espaços em branco.", null));
			return;
		}
		
		try {
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
		} catch(ConteudoJaExistenteNoBancoDeDadosException | InconsistenciaException e) {
			ctx.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, e.getMessage(), null));
			logger.info(e.getMessage());
		}
		
		produto = new Produto();
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
	
	public void consultar(boolean exibirMensagem) {
		try {
			listProdutos = produtoService.buscarComFiltro(prodConsParam);
		} catch (CadastroInexistenteException e) {
			if(listProdutos != null && !listProdutos.isEmpty()) {
				listProdutos.clear();
			}
			
			if(exibirMensagem) {
				ctx.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, e.getMessage(), null));
			}
		}
	}
	
	public void excluir() {
		Produto prod;
		
		prod = produtoService.excluir(produto.getId());

		// Processo de auditoria de exclusão de contratos
		Auditoria auditoria = new Auditoria();
		auditoria.setDataAcao(new Date());
		auditoria.setTitulo("Exclusão");
		auditoria.setDescricao("Excluiu o produto " + prod.getDescricao());
		auditoria.setUsuario(Seguranca.getUsuarioLogado());
		auditoria.setIp(request.getRemoteAddr());
		auditoriaService.cadastrarNovaAcao(auditoria);
		
		ctx.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Produto " + prod.getDescricao() + " excluído com sucesso", null));
		
		// Recarrega a listagem de produtos
		consultar(false);
	}
	
	public void carregarDadosProdutoParaEdicao() {
		if(carregarPagina) {
			produto = produtoService.findById(produto.getId());
		}
		
		carregarPagina = false;
	}
	
	public void editar() {
		logger.debug("Tentando realizar a edição do produto " + produto.getDescricao());
		
		String codigo = produto.getCodigo() != null ? produto.getCodigo().trim() : "";
		String descricao = produto.getDescricao() != null ? produto.getDescricao().trim() : "";
		
		if(codigo.isEmpty() || descricao.isEmpty()) {
			ctx.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Os campos código e descrição não podem conter apenas espaços em branco.", null));
			return;
		}

		try {
			produtoService.editar(produto);

			// Sucesso - Exibe mensagem de edição realizada com sucesso
			ctx.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Produto " + produto.getDescricao() + " editado com sucesso.", null));
			logger.info("Produto " + produto.getDescricao() + " foi editado com sucesso.");
			
			// Processo de auditoria de cadastro de usuário
			Auditoria auditoria = new Auditoria();
			auditoria.setDataAcao(new Date());
			auditoria.setTitulo("Edição");
			auditoria.setDescricao("Editou o produto " + produto.getDescricao());
			auditoria.setUsuario(Seguranca.getUsuarioLogado());
			auditoria.setIp(request.getRemoteAddr());
			auditoriaService.cadastrarNovaAcao(auditoria);
		} catch (InconsistenciaException e) {
			ctx.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, e.getMessage(), null));
			logger.info(e.getMessage());
		}
	}
	
	public void excluirPelaEdicao() {
		Produto prod;
		
		prod = produtoService.excluir(produto.getId());

		ctx.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Produto " + prod.getDescricao() + " excluído com sucesso", null));
		
		try {
			external.getFlash().setKeepMessages(true);
			external.redirect(request.getContextPath() + "/admin/consultaProduto.xhtml");
		} catch (IOException e) {
			e.printStackTrace();
			logger.error("Erro ao tentar redirecionar para a página " + request.getContextPath() + "/consultaProduto.xhtml");
		}
	
	}
	
	public void abrirDialog() {
		RequestContext context = RequestContext.getCurrentInstance();
		context.execute("PF('dlgConsultaFornecedor').show();");
	}
	
	public void abrirDialogExcluir() {
		RequestContext context = RequestContext.getCurrentInstance();
		context.execute("PF('dlgExcluir').show();");
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

	public List<Produto> getListProdutos() {
		return listProdutos;
	}

	public void setListProdutos(List<Produto> listProdutos) {
		this.listProdutos = listProdutos;
	}
}
