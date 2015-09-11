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
import br.com.milenio.vendingmachine.domain.model.Manutencao;
import br.com.milenio.vendingmachine.domain.model.Maquina;
import br.com.milenio.vendingmachine.domain.model.MaquinaStatus;
import br.com.milenio.vendingmachine.exceptions.CadastroInexistenteException;
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
	private FacesContext ctx;
	
	@Inject
	private Logger logger;
	
	private Fornecedor fornecedor = new Fornecedor();
	private Maquina maquina = new Maquina();
	private List<Fornecedor> listFornecedores = new ArrayList<Fornecedor>();
	private List<Maquina> listMaquinas = new ArrayList<Maquina>();
	private Manutencao manutencao = new Manutencao();
	
	public void cadastrar() {
		logger.debug("Tentando realizar o cadastro da manutenção para a máquina " + manutencao.getMaquina().getCodigo());
		
		String motivo = manutencao.getMotivo() != null ? manutencao.getMotivo().trim() : "";
		
		if(motivo.isEmpty() || motivo.isEmpty()) {
			ctx.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "O campo motivo não pode conter apenas espaços em branco.", null));
			return;
		}
		manutencaoService.cadastrar(manutencao);
		
		// Sucesso - Exibe mensagem de cadastro realizado com sucesso
		ctx.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Manutencao para a máquina " + manutencao.getMaquina().getCodigo() + " cadastrada com sucesso.", null));
		logger.info("Manutenção para a máquina " + manutencao.getMaquina().getCodigo() + " cadastrada no sistema com sucesso.");
		
		// Processo de auditoria de cadastro de usuário
		Auditoria auditoria = new Auditoria();
		auditoria.setDataAcao(new Date());
		auditoria.setTitulo("Cadastro");
		auditoria.setDescricao("Cadastrou a manutenção para a máquina " + manutencao.getMaquina().getCodigo());
		auditoria.setUsuario(Seguranca.getUsuarioLogado());
		auditoria.setIp(request.getRemoteAddr());
		auditoriaService.cadastrarNovaAcao(auditoria);

		manutencao = new Manutencao();
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
			// Busca apenas as máquinas que estejam em estoque
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
	
	public void selecionarFornecedor(String codigo) {
		Fornecedor forn = fornecedorService.findByCodigo(codigo);
		
		if(forn != null) {
			manutencao.setFornecedor(forn);
		} else{
			manutencao.setFornecedor(new Fornecedor());
			ctx.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Código inválido: O código " + codigo + " informado não corresponde a nenhum fornecedor cadastrado no sistema.", null));
			return;
		}
		
		RequestContext context = RequestContext.getCurrentInstance();
		context.execute("PF('dlgConsultaFornecedor').hide();");
	}
	
	public void selecionarMaquina(String codigo) {
		Maquina maq = maquinaService.findByCodigo(codigo);
		
		if(maq != null) {
			if(!"EM ESTOQUE".equalsIgnoreCase(maq.getMaquinaStatus().getDescricao())) {
				ctx.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Código inválido: O código " + codigo + " informado não corresponde a uma máquina disponível em estoque.", null));
				manutencao.setMaquina(new Maquina());
				return;
			}
			manutencao.setMaquina(maq);
		} else{
			manutencao.setMaquina(new Maquina());
			ctx.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Código inválido: O código " + codigo + " informado não corresponde a nenhuma máquina cadastrada no sistema.", null));
			return;
		}
		
		RequestContext context = RequestContext.getCurrentInstance();
		context.execute("PF('dlgConsultaMaquina').hide();");
	}
	
	public void abrirDialog(String dialog) {
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
}
