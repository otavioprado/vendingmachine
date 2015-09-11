package br.com.milenio.vendingmachine.managedbean;

import java.io.IOException;
import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.faces.application.FacesMessage;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;

import org.apache.logging.log4j.Logger;
import org.primefaces.context.RequestContext;

import br.com.milenio.vendingmachine.domain.model.Atividade;
import br.com.milenio.vendingmachine.domain.model.Auditoria;
import br.com.milenio.vendingmachine.domain.model.NaturezaFinanceira;
import br.com.milenio.vendingmachine.exceptions.CadastroInexistenteException;
import br.com.milenio.vendingmachine.exceptions.ConteudoJaExistenteNoBancoDeDadosException;
import br.com.milenio.vendingmachine.exceptions.InconsistenciaException;
import br.com.milenio.vendingmachine.security.Seguranca;
import br.com.milenio.vendingmachine.service.AuditoriaService;
import br.com.milenio.vendingmachine.service.NaturezaFinanceiraService;

@Named
@ViewScoped
public class NaturezaFinanceiraMB implements Serializable {
	
	private static final long serialVersionUID = -1377092587129613830L;

	@Inject
	private Logger logger;
	
	@Inject
	private AuditoriaService auditoriaService;
	
	@Inject
	private FacesContext ctx;
	
	@Inject
	private HttpServletRequest request;
	
	@Inject
	private NaturezaFinanceiraService naturezaFinanceiraService;
	
	@Inject
	private ExternalContext external;
	
	private NaturezaFinanceira naturezaFinanceira = new NaturezaFinanceira();
	private NaturezaFinanceira naturezaFinanceiraConsParam = new NaturezaFinanceira();
	
	private Long idNatureza;

	private List<NaturezaFinanceira> listNaturezasFinanceiras;

	public void cadastrar() {
		logger.debug("Tentando realizar o cadastro da natureza financeira " + naturezaFinanceira.getDescricao());
		
		String campoEmBranco = buscarCamposEmBranco();
		
		if(campoEmBranco != null) {
			ctx.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "O campo " + campoEmBranco +" não pode conter apenas espaços em branco.", null));
			return;
		}
		
		try {
			
			naturezaFinanceiraService.cadastrar(naturezaFinanceira);
			
			// Sucesso - Exibe mensagem de cadastro realizado com sucesso
			ctx.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Natureza financeira " + naturezaFinanceira.getDescricao() + " cadastrada com sucesso.", null));
			logger.info("Natureza financeira " + naturezaFinanceira.getDescricao() + " foi cadastrada no sistema com sucesso.");
			
			// Processo de auditoria de cadastro de natureza financeira
			Auditoria auditoria = new Auditoria();
			auditoria.setDataAcao(new Date());
			auditoria.setTitulo("Cadastro");
			auditoria.setDescricao("Cadastrou a natureza financeira " + naturezaFinanceira.getDescricao());
			auditoria.setUsuario(Seguranca.getUsuarioLogado());
			auditoria.setIp(request.getRemoteAddr());
			auditoriaService.cadastrarNovaAcao(auditoria);
		} catch (ConteudoJaExistenteNoBancoDeDadosException e) {
			ctx.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, e.getMessage(), null));
			logger.info(e.getMessage());
		}
		
		naturezaFinanceira = new NaturezaFinanceira();
	}
	
	public void consultarNaturezaFinanceira(boolean exibirMensagem) {
		try {
			listNaturezasFinanceiras = naturezaFinanceiraService.buscarNaturezasComFiltro(naturezaFinanceiraConsParam);
		} catch (CadastroInexistenteException e) {
			if(listNaturezasFinanceiras != null && !listNaturezasFinanceiras.isEmpty()) {
				listNaturezasFinanceiras.clear();
			}
			
			if(exibirMensagem) {
				ctx.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, e.getMessage(), null));
			}
			
		}
		
	}

	public void editarNaturezaFinanceira() {
		logger.debug("Tentando realizar a edição da natureza financeira " + naturezaFinanceira.getDescricao());
		
		String campoEmBranco = buscarCamposEmBranco();
		
		if(campoEmBranco != null) {
			ctx.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "O campo " + campoEmBranco +" não pode conter apenas espaços em branco.", null));
			return;
		}
		
		try{
			naturezaFinanceiraService.editar(naturezaFinanceira);
			
			// Sucesso - Exibe mensagem de edição realizado com sucesso
			ctx.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Natureza financeira " + naturezaFinanceira.getDescricao() + " editada com sucesso.", null));
			logger.info("Natureza financeira " + naturezaFinanceira.getDescricao() + " foi editada no sistema com sucesso.");
			
			// Processo de auditoria de cadastro de usuário
			Auditoria auditoria = new Auditoria();
			auditoria.setDataAcao(new Date());
			auditoria.setTitulo("Edição");
			auditoria.setDescricao("Editou a natureza financeira " + naturezaFinanceira.getDescricao());
			auditoria.setUsuario(Seguranca.getUsuarioLogado());
			auditoria.setIp(request.getRemoteAddr());
			auditoriaService.cadastrarNovaAcao(auditoria);
		} catch (ConteudoJaExistenteNoBancoDeDadosException e) {
			ctx.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, e.getMessage(), null));
			logger.info(e.getMessage());
		}
	}
	
	public void excluirNaturezaFinanceira() {
		NaturezaFinanceira nat;
		try {
			nat = naturezaFinanceiraService.excluirNaturezaFinanceira(idNatureza);
		} catch (InconsistenciaException e) {
			ctx.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, e.getMessage(), null));
			return;
		}
		
		ctx.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Natureza financeira " + nat.getDescricao() + " excluída com sucesso", null));
		
		// Recarrega a listagem de naturezas
		consultarNaturezaFinanceira(false);
	}

	public void excluirNaturezaFinanceiraPelaEdicao() {
		NaturezaFinanceira nat;
		
		try {
			nat = naturezaFinanceiraService.excluirNaturezaFinanceira(idNatureza);
		} catch (InconsistenciaException e) {
			ctx.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, e.getMessage(), null));
			return;
		}
		
		ctx.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Natureza financeira " + nat.getDescricao()+ " excluída com sucesso", null));
		
		try {
			external.getFlash().setKeepMessages(true);
			external.redirect(request.getContextPath() + "/admin/consultaNaturezaFinanceira.xhtml");
		} catch (IOException e) {
			e.printStackTrace();
			logger.error("Erro ao tentar redirecionar para a página " + request.getContextPath() + "/consultaNaturezaFinanceira.xhtml");
		}
	}
	
	private String buscarCamposEmBranco() {
		Map<String, String> mapParam = new HashMap<String, String>();
		
		mapParam.put("Código", naturezaFinanceira.getCodigo() != null ? naturezaFinanceira.getCodigo().trim() : "");
		mapParam.put("Descricao", naturezaFinanceira.getDescricao() != null ? naturezaFinanceira.getDescricao().trim() : "");
		mapParam.put("Tipo", naturezaFinanceira.getTipoNaturezaFinanceira() != null ? naturezaFinanceira.getTipoNaturezaFinanceira().trim() : "");
		
		Set<String> keySet = mapParam.keySet();
		
		for(String key : keySet) {
			String valor = mapParam.get(key);
			
			if(valor.isEmpty()) {
				return key;
			}
		}
		
		return null;
	}

	public void abrirDialogExcluir() {
		RequestContext context = RequestContext.getCurrentInstance();
		context.execute("PF('dlgExcluir').show();");
	}
	
	public void carregarDadosNaturezaFinanceiraParaEdicao() {
		naturezaFinanceira = naturezaFinanceiraService.findById(idNatureza);
	}

	public NaturezaFinanceira getNaturezaFinanceira() {
		return naturezaFinanceira;
	}

	public void setNaturezaFinanceira(NaturezaFinanceira naturezaFinanceira) {
		this.naturezaFinanceira = naturezaFinanceira;
	}
	
	public NaturezaFinanceira getNaturezaFinanceiraConsParam() {
		return naturezaFinanceiraConsParam;
	}

	public void setNaturezaFinanceiraConsParam(
			NaturezaFinanceira naturezaFinanceiraConsParam) {
		this.naturezaFinanceiraConsParam = naturezaFinanceiraConsParam;
	}

	public List<NaturezaFinanceira> getListNaturezasFinanceiras() {
		return listNaturezasFinanceiras;
	}

	public void setListNaturezasFinanceiras(
			List<NaturezaFinanceira> listNaturezasFinanceiras) {
		this.listNaturezasFinanceiras = listNaturezasFinanceiras;
	}

	public Long getIdNatureza() {
		return idNatureza;
	}

	public void setIdNatureza(Long idNatureza) {
		this.idNatureza = idNatureza;
	}

}