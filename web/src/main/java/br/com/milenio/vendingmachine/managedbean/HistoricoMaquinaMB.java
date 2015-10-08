package br.com.milenio.vendingmachine.managedbean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import br.com.milenio.vendingmachine.domain.model.HistoricoMaquina;
import br.com.milenio.vendingmachine.exceptions.CadastroInexistenteException;
import br.com.milenio.vendingmachine.service.HistoricoMaquinaService;

@Named
@ViewScoped
public class HistoricoMaquinaMB implements Serializable {
	private static final long serialVersionUID = -7229666925861618300L;
	
	@Inject
	private HistoricoMaquinaService historicoMaquinaService;
	
	@Inject
	private FacesContext ctx;
	
	private HistoricoMaquina historicoConsParam = new HistoricoMaquina();
	private List<HistoricoMaquina> listHistorico = new ArrayList<HistoricoMaquina>();
	private boolean rendered = false;
	
	public void consultar() {
		try {
			rendered = true;
			listHistorico = historicoMaquinaService.buscarComFiltro(historicoConsParam);
		} catch (CadastroInexistenteException e) {
			if(listHistorico != null && !listHistorico.isEmpty()) {
				listHistorico.clear();
			}
			
			ctx.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, e.getMessage(), null));
		}
	}
	
	public void carregarHistoricoMaquina() {
		if(historicoConsParam.getMaquina().getCodigo() != null && rendered == false) {
			try {
				rendered = true;
				listHistorico = historicoMaquinaService.buscarComFiltro(historicoConsParam);
			} catch (CadastroInexistenteException e) {
				if(listHistorico != null && !listHistorico.isEmpty()) {
					listHistorico.clear();
				}
				
				ctx.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, e.getMessage(), null));
			}
		}
	}

	public HistoricoMaquina getHistoricoConsParam() {
		return historicoConsParam;
	}

	public void setHistoricoConsParam(HistoricoMaquina historicoConsParam) {
		this.historicoConsParam = historicoConsParam;
	}

	public List<HistoricoMaquina> getListHistorico() {
		return listHistorico;
	}

	public void setListHistorico(List<HistoricoMaquina> listHistorico) {
		this.listHistorico = listHistorico;
	}
}
