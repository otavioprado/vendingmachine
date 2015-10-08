package br.com.milenio.vendingmachine.repository;

import java.util.Date;
import java.util.List;

import javax.ejb.Stateless;

import com.uaihebert.uaicriteria.UaiCriteria;
import com.uaihebert.uaicriteria.UaiCriteriaFactory;

import br.com.milenio.vendingmachine.domain.AbstractVendingMachineRepositoryBean;
import br.com.milenio.vendingmachine.domain.model.HistoricoMaquina;

@Stateless(name = "HistoricoMaquinaRepository")
public class HistoricoMaquinaRepositoryBean extends AbstractVendingMachineRepositoryBean<HistoricoMaquina, Long> 
	implements HistoricoMaquinaRepository {

	public HistoricoMaquinaRepositoryBean() {
		super(HistoricoMaquina.class);
	}

	@Override
	public List<HistoricoMaquina> buscarComFiltro(HistoricoMaquina historicoConsParam) {
		UaiCriteria<HistoricoMaquina> uaiCriteria = UaiCriteriaFactory.createQueryCriteria(getEntityManager(), HistoricoMaquina.class);
		
		String codMaquina = historicoConsParam.getMaquina().getCodigo();
		String status = historicoConsParam.getStatus();
		String login = historicoConsParam.getUsuarioSistema().getLogin();
		Date data = historicoConsParam.getData();
		
		if(codMaquina != null && !codMaquina.isEmpty()) {
			uaiCriteria.innerJoin("maquina");
			uaiCriteria.andEquals("maquina.codigo", codMaquina);
		}
		
		if(status != null && !status.isEmpty()) {
			uaiCriteria.andEquals("status", status);
		}
		
		if(login != null && !login.isEmpty()) {
			uaiCriteria.innerJoin("usuarioSistema");
			uaiCriteria.andEquals("usuarioSistema.login", login);
		}
		
		if(data != null) {
			uaiCriteria.andEquals("data", data);
		}
		
		List<HistoricoMaquina> historicos = (List<HistoricoMaquina>) uaiCriteria.getResultList();
		return historicos;
	}
}
