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
import org.joda.time.DateTime;
import org.joda.time.Days;
import org.primefaces.context.RequestContext;

import br.com.milenio.vendingmachine.domain.model.Auditoria;
import br.com.milenio.vendingmachine.domain.model.Fornecedor;
import br.com.milenio.vendingmachine.domain.model.Maquina;
import br.com.milenio.vendingmachine.domain.model.MaquinaStatus;
import br.com.milenio.vendingmachine.domain.model.Produto;
import br.com.milenio.vendingmachine.exceptions.CadastroInexistenteException;
import br.com.milenio.vendingmachine.exceptions.ConteudoJaExistenteNoBancoDeDadosException;
import br.com.milenio.vendingmachine.exceptions.InconsistenciaException;
import br.com.milenio.vendingmachine.repository.MaquinaStatusRepository;
import br.com.milenio.vendingmachine.security.Seguranca;
import br.com.milenio.vendingmachine.service.AuditoriaService;
import br.com.milenio.vendingmachine.service.FornecedorService;
import br.com.milenio.vendingmachine.service.MaquinaService;
import br.com.milenio.vendingmachine.service.ProdutoService;

@Named
@ViewScoped
public class MaquinaMB implements Serializable {
	private static final long serialVersionUID = -8888730192386668748L;
	
	@Inject
	private FornecedorService fornecedorService;
	
	@Inject
	private Logger logger;
	
	@Inject
	private ProdutoService produtoService;
	
	@Inject
	private MaquinaStatusRepository maquinaStatusRepository;
	
	@Inject
	private AuditoriaService auditoriaService;
	
	@Inject
	private MaquinaService maquinaService;
	
	@Inject
	private HttpServletRequest request;
	
	@Inject
	private FacesContext ctx;
	
	private List<Maquina> listMaquinas = new ArrayList<Maquina>();
	private Maquina maquina = new Maquina();
	private List<Fornecedor> listFornecedores = new ArrayList<Fornecedor>();
	private List<Produto> listProdutos = new ArrayList<Produto>();
	private Fornecedor fornecedor = new Fornecedor();
	private Produto produtoSelecionado = new Produto();
	private Produto produto = new Produto();
	private String custoAquisicao;
	private Maquina maqConsParam = new Maquina();
	private boolean carregarPagina = true;
	
	public void cadastrar() {
		logger.debug("Tentando realizar o cadastro da máquina " + maquina.getCodigo());
		
		String codigo = maquina.getCodigo() != null ? maquina.getCodigo().trim() : "";
		String modelo = maquina.getModelo() != null ? maquina.getModelo().trim() : "";
		
		if(codigo.isEmpty() || modelo.isEmpty()) {
			ctx.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Os campos código e modelo não podem conter apenas espaços em branco.", null));
			return;
		}
		
		// Valida se a data informada não é menor que a data atual
		DateTime hoje = new DateTime(new Date());
		DateTime dataInformada = new DateTime(maquina.getDataAquisicao());
		Days daysBetween = Days.daysBetween(hoje, dataInformada);
		
		if(daysBetween.getDays() > 0) {
			ctx.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Apenas datas de aquisição passadas podem ser informadas.", null));
			return;
		}
		
		try{
			maquinaService.cadastrar(maquina);
			
			// Sucesso - Exibe mensagem de cadastro realizado com sucesso
			ctx.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Máquina " + maquina.getCodigo() + " cadastrada com sucesso.", null));
			logger.info("Máquina " + maquina.getCodigo() + " cadastrada no sistema com sucesso.");
			
			// Processo de auditoria de cadastro de usuário
			Auditoria auditoria = new Auditoria();
			auditoria.setDataAcao(new Date());
			auditoria.setTitulo("Cadastro");
			auditoria.setDescricao("Cadastrou a máquina " + maquina.getCodigo());
			auditoria.setUsuario(Seguranca.getUsuarioLogado());
			auditoria.setIp(request.getRemoteAddr());
			auditoriaService.cadastrarNovaAcao(auditoria);
			
		} catch(ConteudoJaExistenteNoBancoDeDadosException | InconsistenciaException e) {
			ctx.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, e.getMessage(), null));
			logger.info(e.getMessage());
		}
		
		maquina = new Maquina();
	}
	
	public List<String> getStatus() {
		List<MaquinaStatus> allStatus = maquinaStatusRepository.getAll();
		
		List<String> status = new ArrayList<String>();
		
		for(MaquinaStatus ms : allStatus) {
			status.add(ms.getDescricao());
		}
		
		return status;
	}
	
	public void editar() {
		String codigo = maquina.getCodigo() != null ? maquina.getCodigo().trim() : "";
		String modelo = maquina.getModelo() != null ? maquina.getModelo().trim() : "";
		
		if(codigo.isEmpty() || modelo.isEmpty()) {
			ctx.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Os campos código e modelo não podem conter apenas espaços em branco.", null));
			return;
		}
		
		// Valida se a data informada não é menor que a data atual
		DateTime hoje = new DateTime(new Date());
		DateTime dataInformada = new DateTime(maquina.getDataAquisicao());
		Days daysBetween = Days.daysBetween(hoje, dataInformada);
		
		if(daysBetween.getDays() > 0) {
			ctx.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Apenas datas de aquisição passadas podem ser informadas.", null));
			return;
		}

		try {
			maquinaService.editar(maquina);
		} catch (ConteudoJaExistenteNoBancoDeDadosException e) {
			ctx.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, e.getMessage(), null));
			return;
		}
		
		// Sucesso - Exibe mensagem de cadastro realizado com sucesso
		ctx.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Máquina " + maquina.getCodigo() + " editada com sucesso.", null));
		logger.info("Máquina " + maquina.getCodigo() + " foi editada com sucesso.");
		
		// Processo de auditoria de cadastro de usuário
		Auditoria auditoria = new Auditoria();
		auditoria.setDataAcao(new Date());
		auditoria.setTitulo("Edição");
		auditoria.setDescricao("Editou a máquina " + maquina.getCodigo());
		auditoria.setUsuario(Seguranca.getUsuarioLogado());
		auditoria.setIp(request.getRemoteAddr());
		auditoriaService.cadastrarNovaAcao(auditoria);
	}
	
	public void carregarDadosParaEdicao() {
		if(carregarPagina) {
			maquina = maquinaService.findById(maquina.getId());
		}
		
		carregarPagina = false;
	}
	
	public void inativar() {
		try {
			maquina = maquinaService.inativar(maquina.getId());
			ctx.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Máquina " + maquina.getCodigo() + " inativada com sucesso.", null));
			
			// Após inativação, carrega a lista de máquinas para atualizar na view de consulta
			consultar(true);
			
			// Processo de auditoria
			Auditoria auditoria = new Auditoria();
			auditoria.setDataAcao(new Date());
			auditoria.setTitulo("Inativação");
			auditoria.setDescricao("Inativou a máquina " + maquina.getCodigo());
			auditoria.setUsuario(Seguranca.getUsuarioLogado());
			auditoria.setIp(request.getRemoteAddr());
			auditoriaService.cadastrarNovaAcao(auditoria);
		} catch (InconsistenciaException e) {
			ctx.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, e.getMessage(), null));
			return;
		}
	}
	
	public void consultar(boolean exibirMensagem) {
		try {
			if(maqConsParam.getDataAquisicao() != null) {
				// Valida se a data informada não é menor que a data atual
				DateTime hoje = new DateTime(new Date());
				DateTime dataInformada = new DateTime(maqConsParam.getDataAquisicao());
				Days daysBetween = Days.daysBetween(hoje, dataInformada);
				
				if(daysBetween.getDays() > 0) {
					ctx.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Apenas datas de aquisição passadas podem ser informadas.", null));
					return;
				}
			}
			
			listMaquinas = maquinaService.buscarComFiltro(maqConsParam);
		} catch (CadastroInexistenteException e) {
			if(listMaquinas != null && !listMaquinas.isEmpty()) {
				listMaquinas.clear();
			}
			
			if(exibirMensagem) {
				ctx.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, e.getMessage(), null));
			}
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
		
		if(maquina.getQtdMaxTipoProdutos() <= maquina.getProdutos().size()) {
			ctx.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "O limite máximo de produtos já foi atingido.", null));
			RequestContext context = RequestContext.getCurrentInstance();
			context.execute("PF('dlgAdicionarProduto').hide();");
			return;
		}
		
		Produto newProduto = produtoService.findById(id);
		
		if(newProduto != null) {
			List<Produto> produtos = maquina.getProdutos();
			
			if(produtos.contains(newProduto)) {
				ctx.addMessage("Message3", new FacesMessage(FacesMessage.SEVERITY_WARN, "O produto " + newProduto.getCodigo() + " já foi adicionado.", null));
				return;
			}
			
			maquina.getProdutos().add(newProduto);
			
			ctx.addMessage("Message3", new FacesMessage(FacesMessage.SEVERITY_INFO, "Produto " + newProduto.getCodigo() + " adicionado com sucesso.", null));
			return;
		} else{
			ctx.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Código inválido: O código " + produto.getCodigo() + " informado não corresponde a nenhum produto cadastrado no sistema.", null));
			return;
		}
	}
	
	public void excluirProduto() {
		maquina.getProdutos().remove(produtoSelecionado);
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
		fornecedor = new Fornecedor();
		listFornecedores = new ArrayList<Fornecedor>();
		
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
		fornecedor = new Fornecedor();
		
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
	
	public Date getTodayDate() {
		return new Date();
	}

	public List<Maquina> getListMaquinas() {
		return listMaquinas;
	}

	public void setListMaquinas(List<Maquina> listMaquinas) {
		this.listMaquinas = listMaquinas;
	}

	public Maquina getMaqConsParam() {
		return maqConsParam;
	}

	public void setMaqConsParam(Maquina maqConsParam) {
		this.maqConsParam = maqConsParam;
	}
}
