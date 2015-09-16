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
import br.com.milenio.vendingmachine.domain.model.Maquina;
import br.com.milenio.vendingmachine.domain.model.NaturezaFinanceira;
import br.com.milenio.vendingmachine.domain.model.Receita;
import br.com.milenio.vendingmachine.exceptions.CadastroInexistenteException;
import br.com.milenio.vendingmachine.security.Seguranca;
import br.com.milenio.vendingmachine.service.AuditoriaService;
import br.com.milenio.vendingmachine.service.MaquinaService;
import br.com.milenio.vendingmachine.service.NaturezaFinanceiraService;
import br.com.milenio.vendingmachine.service.ReceitaService;

@Named
@ViewScoped
public class ReceitaMB implements Serializable {
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
	private ReceitaService receitaService;
	
	@Inject
	private NaturezaFinanceiraService naturezaFinanceiraService;
	
	@Inject
	private HttpServletRequest request;
	
	private Receita receita = new Receita();
	private List<Maquina> listMaquinas = new ArrayList<Maquina>();
	private Maquina maquina = new Maquina();
	private Receita receitaConsParam = new Receita();
	private List<Receita> listReceitas = new ArrayList<Receita>();
	
	public void cadastrar() {
		String codigoMaquina = receita.getMaquina().getCodigo() != null ? receita.getMaquina().getCodigo().trim() : "";
		
		if(codigoMaquina.isEmpty()) {
			ctx.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "O campo código não pode conter apenas espaços em branco.", null));
			return;
		}
		
		try {
			receitaService.cadastrar(receita);
			
			// Sucesso - Exibe mensagem de cadastro realizado com sucesso
			ctx.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Receita R$" + receita.getValor() + " cadastrada para a máquina " + receita.getMaquina().getCodigo() + " com sucesso.", null));
			logger.info("Receita R$" + receita.getValor() + " cadastrada para a máquina " + receita.getMaquina().getCodigo() + " com sucesso.");
			
			// Processo de auditoria de cadastro de produtos
			Auditoria auditoria = new Auditoria();
			auditoria.setDataAcao(new Date());
			auditoria.setTitulo("Cadastro");
			auditoria.setDescricao("Cadastrou uma receita de " + receita.getValor());
			auditoria.setUsuario(Seguranca.getUsuarioLogado());
			auditoria.setIp(request.getRemoteAddr());
			auditoriaService.cadastrarNovaAcao(auditoria);
		} catch(Exception e) {
			ctx.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, e.getMessage(), null));
			logger.info(e.getMessage());
		}
		
		receita = new Receita();
	}
	
	public void selecionarMaquina(String codigo) {
		Maquina maq = maquinaService.findByCodigo(codigo);
		
		if(maq != null) {
			receita.setMaquina(maq);
		} else{
			receita.setMaquina(new Maquina());
			ctx.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Código inválido: O código " + codigo + " informado não corresponde a nenhuma máquina cadastrada no sistema.", null));
			return;
		}
		
		RequestContext context = RequestContext.getCurrentInstance();
		context.execute("PF('dlgConsultaMaquina').hide();");
	}
	
	public void abrirDialog(String dialog) {
		listMaquinas = new ArrayList<Maquina>();
		
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
				if("INATIVADA".equalsIgnoreCase(listMaquinas.get(i).getMaquinaStatus().getDescricao())) {
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
			listReceitas = receitaService.buscarComFiltro(receita);
		} catch (CadastroInexistenteException e) {
			if(listReceitas != null && !listReceitas.isEmpty()) {
				listReceitas.clear();
			}
			
			if(exibirMensagem) {
				ctx.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, e.getMessage(), null));
			}
		}
	}
	
	public List<NaturezaFinanceira> getListNaturezaFinanceira() {
		NaturezaFinanceira nf = new NaturezaFinanceira();
		nf.setTipoNaturezaFinanceira("RECEITA");
		
		try {
			List<NaturezaFinanceira> listNf = naturezaFinanceiraService.buscarNaturezasComFiltro(nf);
			return listNf;
		} catch (CadastroInexistenteException e) {
			return new ArrayList<NaturezaFinanceira>();
		}
	}

	public Receita getReceita() {
		return receita;
	}

	public void setReceita(Receita receita) {
		this.receita = receita;
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

	public Receita getReceitaConsParam() {
		return receitaConsParam;
	}

	public void setReceitaConsParam(Receita receitaConsParam) {
		this.receitaConsParam = receitaConsParam;
	}

	public List<Receita> getListReceitas() {
		return listReceitas;
	}

	public void setListReceitas(List<Receita> listReceitas) {
		this.listReceitas = listReceitas;
	}
}
