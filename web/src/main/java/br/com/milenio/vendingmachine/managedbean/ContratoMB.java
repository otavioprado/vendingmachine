package br.com.milenio.vendingmachine.managedbean;

import java.io.IOException;
import java.io.Serializable;
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
import br.com.milenio.vendingmachine.domain.model.Contrato;
import br.com.milenio.vendingmachine.exceptions.CadastroInexistenteException;
import br.com.milenio.vendingmachine.exceptions.ConteudoJaExistenteNoBancoDeDadosException;
import br.com.milenio.vendingmachine.exceptions.InconsistenciaException;
import br.com.milenio.vendingmachine.security.Seguranca;
import br.com.milenio.vendingmachine.service.AuditoriaService;
import br.com.milenio.vendingmachine.service.ContratoService;

@Named
@ViewScoped
public class ContratoMB implements Serializable {
	private static final long serialVersionUID = 8986450544934820027L;
	
	@Inject
	private AuditoriaService auditoriaService;
	
	@Inject
	private Logger logger;
	
	@Inject
	private FacesContext ctx;
	
	@Inject
	private ExternalContext external;
	
	@Inject
	private ContratoService contratoService;
	
	@Inject
	private HttpServletRequest request;
	
	private Contrato contrato = new Contrato();
	private Contrato contratoConsParam = new Contrato();
	
	private String valorAluguel;
	private String valorPorcentagem;

	private List<Contrato> listContratos;
	
	private boolean carregarPagina = true;
	
	public void cadastrar() {
		logger.debug("Tentando realizar o cadastro do contrato " + contrato.getDescricao());
		
		String codigo = contrato.getCodigo() != null ? contrato.getCodigo().trim() : "";
		String descricao = contrato.getDescricao() != null ? contrato.getDescricao().trim() : "";
		
		if(codigo.isEmpty() || descricao.isEmpty()) {
			ctx.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Os campos código e descrição não podem conter apenas espaços em branco.", null));
			return;
		}
		
		String modalidade = contrato.getModalidade();
		
		if((valorAluguel == null || valorAluguel.isEmpty()) && (valorPorcentagem == null || valorPorcentagem.isEmpty())) {
			if("PORCENTAGEM".equals(modalidade)) {
				ctx.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "O campo porcentagem é obrigatório", null));
				return;
			} else if("ALUGUEL".equals(modalidade)) {
				ctx.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "O campo aluguel é obrigatório", null));
				return;
			}
		}
		
		Double value = null;
		
		if("PORCENTAGEM".equals(modalidade)) {
			valorPorcentagem = valorPorcentagem.replace("%", "").trim();
			valorPorcentagem = valorPorcentagem.replace(",", ".");
			value = Double.parseDouble(valorPorcentagem);
		} else if("ALUGUEL".equals(modalidade)) {
			valorAluguel = valorAluguel.replace("R$", "").trim();
			valorAluguel = valorAluguel.replace(",", ".");
			value = Double.parseDouble(valorAluguel);
		}
		
		try{
			contrato.setValor(value);
			contrato.setIndDisponivel(true);
			contratoService.cadastrar(contrato);
			
			// Sucesso - Exibe mensagem de cadastro realizado com sucesso
			ctx.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Contrato " + contrato.getDescricao() + " cadastrado com sucesso.", null));
			logger.info("Contrato " + contrato.getDescricao() + " foi cadastrado no sistema com sucesso.");
			
			// Processo de auditoria de cadastro de usuário
			Auditoria auditoria = new Auditoria();
			auditoria.setDataAcao(new Date());
			auditoria.setTitulo("Cadastro");
			auditoria.setDescricao("Cadastrou o contrato " + contrato.getDescricao());
			auditoria.setUsuario(Seguranca.getUsuarioLogado());
			auditoria.setIp(request.getRemoteAddr());
			auditoriaService.cadastrarNovaAcao(auditoria);
			
		} catch(ConteudoJaExistenteNoBancoDeDadosException e) {
			ctx.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, e.getMessage(), null));
			logger.info(e.getMessage());
		}
		
		contrato = new Contrato();
		valorAluguel = "";
		valorPorcentagem = "";
	}
	
	public void excluir() {
		Contrato cont;
		try {
			cont = contratoService.excluir(contrato.getId());
		} catch (InconsistenciaException e) {
			ctx.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, e.getMessage(), null));
			return;
		}
		
		// Processo de auditoria de exclusão de contratos
		Auditoria auditoria = new Auditoria();
		auditoria.setDataAcao(new Date());
		auditoria.setTitulo("Exclusão");
		auditoria.setDescricao("Excluiu o contrato " + cont.getDescricao());
		auditoria.setUsuario(Seguranca.getUsuarioLogado());
		auditoria.setIp(request.getRemoteAddr());
		auditoriaService.cadastrarNovaAcao(auditoria);
		
		ctx.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Contrato " + cont.getDescricao() + " excluído com sucesso", null));
		
		// Recarrega a listagem de contratos
		consultar(false);
	}
	
	public void excluirContratoPelaEdicao() {
		Contrato cnt;
		
		try {
			cnt = contratoService.excluir(contrato.getId());
		} catch (InconsistenciaException e) {
			ctx.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, e.getMessage(), null));
			return;
		}
		
		ctx.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Contrato " + cnt.getDescricao() + " excluído com sucesso", null));
		
		try {
			external.getFlash().setKeepMessages(true);
			external.redirect(request.getContextPath() + "/admin/consultaContrato.xhtml");
		} catch (IOException e) {
			e.printStackTrace();
			logger.error("Erro ao tentar redirecionar para a página " + request.getContextPath() + "/consultaContrato.xhtml");
		}
	}
	
	public void editar() {
		logger.debug("Tentando realizar a edição do contrato " + contrato.getDescricao());
		
		String descricao = contrato.getDescricao() != null ? contrato.getDescricao().trim() : "";
		
		if(descricao.isEmpty()) {
			ctx.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "O campo descrição não pode conter apenas espaços em branco.", null));
			return;
		}
		
		String modalidade = contrato.getModalidade();
		
		if((valorAluguel == null || valorAluguel.isEmpty()) && (valorPorcentagem == null || valorPorcentagem.isEmpty())) {
			if("PORCENTAGEM".equals(modalidade)) {
				ctx.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "O campo porcentagem é obrigatório", null));
				return;
			} else if("ALUGUEL".equals(modalidade)) {
				ctx.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "O campo aluguel é obrigatório", null));
				return;
			}
		}
		
		Double value = null;
		
		if("PORCENTAGEM".equals(modalidade)) {
			valorPorcentagem = valorPorcentagem.replace("%", "").trim();
			valorPorcentagem = valorPorcentagem.replace(",", ".");
			value = Double.parseDouble(valorPorcentagem);
		} else if("ALUGUEL".equals(modalidade)) {
			valorAluguel = valorAluguel.replace("R$", "").trim();
			valorAluguel = valorAluguel.replace(",", ".");
			value = Double.parseDouble(valorAluguel);
		}
		
		contrato.setValor(value);
		try {
			contratoService.editar(contrato);
		} catch (InconsistenciaException e) {
			ctx.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, e.getMessage(), null));
			return;
		}
		
		// Sucesso - Exibe mensagem de cadastro realizado com sucesso
		ctx.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Contrato " + contrato.getDescricao() + " editado com sucesso.", null));
		logger.info("Contrato " + contrato.getDescricao() + " foi editado com sucesso.");
		
		// Processo de auditoria de cadastro de usuário
		Auditoria auditoria = new Auditoria();
		auditoria.setDataAcao(new Date());
		auditoria.setTitulo("Edição");
		auditoria.setDescricao("Editou o contrato " + contrato.getDescricao());
		auditoria.setUsuario(Seguranca.getUsuarioLogado());
		auditoria.setIp(request.getRemoteAddr());
		auditoriaService.cadastrarNovaAcao(auditoria);
	}
	
	public void consultar(boolean exibirMensagem) {
		try {
			listContratos = contratoService.buscarContratosComFiltro(contratoConsParam);
		} catch (CadastroInexistenteException e) {
			if(listContratos != null && !listContratos.isEmpty()) {
				listContratos.clear();
			}
			
			if(exibirMensagem) {
				ctx.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, e.getMessage(), null));
			}
		}
	}
	
	public void abrirDialogExcluir() {
		RequestContext context = RequestContext.getCurrentInstance();
		context.execute("PF('dlgExcluir').show();");
	}
	
	public void carregarDadosContratoParaEdicao() {
		if(carregarPagina) {
			contrato = contratoService.findById(contrato.getId());
			
			String modalidade = contrato.getModalidade();
			
			if("PORCENTAGEM".equalsIgnoreCase(modalidade)) {
				valorPorcentagem = contrato.getValor().toString() + "%";
			} else if("ALUGUEL".equalsIgnoreCase(modalidade)) {
				valorAluguel = "R$" + contrato.getValor().toString();
			}
		}
		
		carregarPagina = false;
	}

	public Contrato getContrato() {
		return contrato;
	}

	public void setContrato(Contrato contrato) {
		this.contrato = contrato;
	}

	public String getValorAluguel() {
		return valorAluguel;
	}

	public void setValorAluguel(String valorAluguel) {
		this.valorAluguel = valorAluguel;
	}

	public String getValorPorcentagem() {
		return valorPorcentagem;
	}

	public void setValorPorcentagem(String valorPorcentagem) {
		this.valorPorcentagem = valorPorcentagem;
	}

	public Contrato getContratoConsParam() {
		return contratoConsParam;
	}

	public void setContratoConsParam(Contrato contratoConsParam) {
		this.contratoConsParam = contratoConsParam;
	}

	public List<Contrato> getListContratos() {
		return listContratos;
	}

	public void setListContratos(List<Contrato> listContratos) {
		this.listContratos = listContratos;
	}
}
