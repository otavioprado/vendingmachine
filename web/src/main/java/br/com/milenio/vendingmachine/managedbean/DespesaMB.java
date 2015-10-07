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
import org.joda.time.DateTime;
import org.joda.time.Days;
import org.primefaces.context.RequestContext;

import br.com.milenio.vendingmachine.domain.model.Auditoria;
import br.com.milenio.vendingmachine.domain.model.Despesa;
import br.com.milenio.vendingmachine.domain.model.Maquina;
import br.com.milenio.vendingmachine.domain.model.NaturezaFinanceira;
import br.com.milenio.vendingmachine.exceptions.CadastroInexistenteException;
import br.com.milenio.vendingmachine.exceptions.InconsistenciaException;
import br.com.milenio.vendingmachine.security.Seguranca;
import br.com.milenio.vendingmachine.service.AuditoriaService;
import br.com.milenio.vendingmachine.service.DespesaService;
import br.com.milenio.vendingmachine.service.MaquinaService;
import br.com.milenio.vendingmachine.service.NaturezaFinanceiraService;
import br.com.milenio.vendingmachine.util.Constants;

@Named
@ViewScoped
public class DespesaMB implements Serializable {
	private static final long serialVersionUID = 8402849166616207220L;
	
	@Inject
	private FacesContext ctx;
	
	@Inject
	private Logger logger;
	
	@Inject
	private AuditoriaService auditoriaService;
	
	@Inject
	private MaquinaService maquinaService;
	
	@Inject
	private ExternalContext external;
	
	@Inject
	private DespesaService despesaService;
	
	@Inject
	private NaturezaFinanceiraService naturezaFinanceiraService;
	
	@Inject
	private HttpServletRequest request;
	
	private Despesa despesa = new Despesa();
	private List<Maquina> listMaquinas = new ArrayList<Maquina>();
	private Maquina maquina = new Maquina();
	private Despesa despesaConsParam = new Despesa();
	private List<Despesa> listDespesas = new ArrayList<Despesa>();
	private Date dataFinal;

	private boolean carregarPagina = true;
	
	public void cadastrar() {
		String codigoMaquina = despesa.getMaquina().getCodigo() != null ? despesa.getMaquina().getCodigo().trim() : "";
		
		if(codigoMaquina.isEmpty()) {
			ctx.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "O campo código não pode conter apenas espaços em branco.", null));
			return;
		}
		
		// Valida se a data informada não é menor que a data atual
		DateTime hoje = new DateTime(new Date());
		DateTime dataInformada = new DateTime(despesa.getData());
		Days daysBetween = Days.daysBetween(hoje, dataInformada);
		
		if(daysBetween.getDays() > 0) {
			ctx.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Apenas datas de receita passadas podem ser informadas.", null));
			return;
		}
		
		try {
			despesaService.cadastrar(despesa);
			
			// Sucesso - Exibe mensagem de cadastro realizado com sucesso
			ctx.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Despesa R$" + despesa.getValor() + " cadastrada para a máquina " + despesa.getMaquina().getCodigo() + " com sucesso.", null));
			logger.info("Despesa R$" + despesa.getValor() + " cadastrada para a máquina " + despesa.getMaquina().getCodigo() + " com sucesso.");
			
			// Processo de auditoria de cadastro de produtos
			Auditoria auditoria = new Auditoria();
			auditoria.setDataAcao(new Date());
			auditoria.setTitulo("Cadastro");
			auditoria.setDescricao("Cadastrou uma despesa de " + despesa.getValor() + " para a máquina " + despesa.getMaquina().getCodigo());
			auditoria.setUsuario(Seguranca.getUsuarioLogado());
			auditoria.setIp(request.getRemoteAddr());
			auditoriaService.cadastrarNovaAcao(auditoria);
		} catch(Exception e) {
			ctx.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, e.getMessage(), null));
			logger.info(e.getMessage());
		}
		
		despesa = new Despesa();
	}
	
	public void editar() {
		String codigoMaquina = despesa.getMaquina().getCodigo() != null ? despesa.getMaquina().getCodigo().trim() : "";
		
		if(codigoMaquina.isEmpty()) {
			ctx.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "O campo código não pode conter apenas espaços em branco.", null));
			return;
		}
		
		// Valida se a data informada não é menor que a data atual
		DateTime hoje = new DateTime(new Date());
		DateTime dataInformada = new DateTime(despesa.getData());
		Days daysBetween = Days.daysBetween(hoje, dataInformada);
		
		if(daysBetween.getDays() > 0) {
			ctx.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Apenas datas de receita passadas podem ser informadas.", null));
			return;
		}
		
		try {
			despesaService.editar(despesa);
			
			// Sucesso - Exibe mensagem de cadastro realizado com sucesso
			ctx.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Despesa R$" + despesa.getValor() + " editada para a máquina " + despesa.getMaquina().getCodigo() + " com sucesso.", null));
			logger.info("Despesa R$" + despesa.getValor() + " editada para a máquina " + despesa.getMaquina().getCodigo() + " com sucesso.");
			
			// Processo de auditoria de cadastro de produtos
			Auditoria auditoria = new Auditoria();
			auditoria.setDataAcao(new Date());
			auditoria.setTitulo("Edição");
			auditoria.setDescricao("Editou uma despesa de " + despesa.getValor() + " para a máquina " + despesa.getMaquina().getCodigo());
			auditoria.setUsuario(Seguranca.getUsuarioLogado());
			auditoria.setIp(request.getRemoteAddr());
			auditoriaService.cadastrarNovaAcao(auditoria);
		} catch(Exception e) {
			ctx.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, e.getMessage(), null));
			logger.info(e.getMessage());
		}
	}
	
	public void selecionarMaquina(String codigo) {
		Maquina maq = maquinaService.findByCodigo(codigo);
		
		if(maq != null) {
			despesa.setMaquina(maq);
		} else{
			despesa.setMaquina(new Maquina());
			ctx.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Código inválido: O código " + codigo + " informado não corresponde a nenhuma máquina cadastrada no sistema.", null));
			return;
		}
		
		RequestContext context = RequestContext.getCurrentInstance();
		context.execute("PF('dlgConsultaMaquina').hide();");
	}
	
	public void abrirDialog(String dialog) {
		listMaquinas = new ArrayList<Maquina>();
		maquina = new Maquina();
		
		RequestContext context = RequestContext.getCurrentInstance();
		context.execute("PF('" + dialog + "').show();");
	}
	
	public void fecharDialog(String dialog) {
		listMaquinas.clear();
		RequestContext context = RequestContext.getCurrentInstance();
		context.execute("PF('" + dialog + "').hide();");
	}
	
	public void consultarMaquina() {
		try {
			listMaquinas = maquinaService.buscarComFiltro(maquina);
			
			// Remove da listagem as máquinas desativas
			for(int i = 0; i < listMaquinas.size(); i++) {
				if(Constants.INATIVADA.equalsIgnoreCase(listMaquinas.get(i).getMaquinaStatus().getDescricao())) {
					listMaquinas.remove(i);
				}
			}
			
			RequestContext context = RequestContext.getCurrentInstance();
			context.execute("PF('dlgConsultaMaquina').show();");
		} catch (CadastroInexistenteException e) {
			listMaquinas.clear();
			ctx.addMessage("Message2", new FacesMessage(FacesMessage.SEVERITY_WARN, e.getMessage(), null));
			RequestContext context = RequestContext.getCurrentInstance();
			context.execute("PF('dlgConsultaMaquina').show();");
		}
	
	}
	
	public void consultar(boolean exibirMensagem) {
		try {
			listDespesas = despesaService.buscarComFiltro(despesaConsParam, dataFinal);
		} catch (CadastroInexistenteException e) {
			if(listDespesas != null && !listDespesas.isEmpty()) {
				listDespesas.clear();
			}
			
			if(exibirMensagem) {
				ctx.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, e.getMessage(), null));
			}
		}
	}
	
	public void carregarDadosParaEdicao() {
		if(carregarPagina) {
			despesa = despesaService.findById(despesa.getId());
		}
		
		carregarPagina = false;
	}
	
	public void excluir() {
		Despesa desp;
		try {
			desp = despesaService.excluir(despesa.getId());
		} catch (InconsistenciaException e) {
			ctx.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, e.getMessage(), null));
			return;
		}
		
		// Processo de auditoria de exclusão de receitas
		Auditoria auditoria = new Auditoria();
		auditoria.setDataAcao(new Date());
		auditoria.setTitulo("Exclusão");
		auditoria.setDescricao("Excluiu uma despesa de " + desp.getValor() + " para a máquina " + desp.getMaquina().getCodigo());
		auditoria.setUsuario(Seguranca.getUsuarioLogado());
		auditoria.setIp(request.getRemoteAddr());
		auditoriaService.cadastrarNovaAcao(auditoria);
		
		ctx.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Despesa de R$" + desp.getValor() + " para a máquina " + desp.getMaquina().getCodigo() + " excluída com sucesso.", null));
		
		// Recarrega a listagem de contratos
		consultar(false);
	}
	
	public void excluirPelaEdicao() {
		Despesa desp;
		try {
			desp = despesaService.excluir(despesa.getId());
		} catch (InconsistenciaException e) {
			ctx.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, e.getMessage(), null));
			return;
		}
		
		// Processo de auditoria de exclusão de receitas
		Auditoria auditoria = new Auditoria();
		auditoria.setDataAcao(new Date());
		auditoria.setTitulo("Exclusão");
		auditoria.setDescricao("Excluiu uma despesa de " + desp.getValor() + " para a máquina " + desp.getMaquina().getCodigo());
		auditoria.setUsuario(Seguranca.getUsuarioLogado());
		auditoria.setIp(request.getRemoteAddr());
		auditoriaService.cadastrarNovaAcao(auditoria);
		
		ctx.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Despesa de R$" + desp.getValor() + " para a máquina " + desp.getMaquina().getCodigo() + " excluída com sucesso.", null));
		
		try {
			external.getFlash().setKeepMessages(true);
			external.redirect(request.getContextPath() + "/admin/consultaReceita.xhtml");
		} catch (IOException e) {
			e.printStackTrace();
			logger.error("Erro ao tentar redirecionar para a página " + request.getContextPath() + "/consultaReceita.xhtml");
		}
	
	}
	
	public List<NaturezaFinanceira> getListNaturezaFinanceira() {
		NaturezaFinanceira nf = new NaturezaFinanceira();
		nf.setTipoNaturezaFinanceira("DESPESA");
		
		try {
			List<NaturezaFinanceira> listNf = naturezaFinanceiraService.buscarNaturezasComFiltro(nf);
			return listNf;
		} catch (CadastroInexistenteException e) {
			return new ArrayList<NaturezaFinanceira>();
		}
	}

	public Despesa getDespesa() {
		return despesa;
	}

	public void setDespesa(Despesa despesa) {
		this.despesa = despesa;
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

	public Date getDataFinal() {
		return dataFinal;
	}

	public void setDataFinal(Date dataFinal) {
		this.dataFinal = dataFinal;
	}

	public Despesa getDespesaConsParam() {
		return despesaConsParam;
	}

	public void setDespesaConsParam(Despesa despesaConsParam) {
		this.despesaConsParam = despesaConsParam;
	}

	public List<Despesa> getListDespesas() {
		return listDespesas;
	}

	public void setListDespesas(List<Despesa> listDespesas) {
		this.listDespesas = listDespesas;
	}
}
