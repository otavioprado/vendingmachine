package br.com.milenio.vendingmachine.repository;

import java.util.Date;
import java.util.List;

import javax.ejb.Stateless;

import com.uaihebert.uaicriteria.UaiCriteria;
import com.uaihebert.uaicriteria.UaiCriteriaFactory;

import br.com.milenio.vendingmachine.domain.AbstractVendingMachineRepositoryBean;
import br.com.milenio.vendingmachine.domain.model.Despesa;

@Stateless(name = "DespesaRepository")
public class DespesaRepositoryBean extends AbstractVendingMachineRepositoryBean<Despesa, Long> 
	implements DespesaRepository {

	public DespesaRepositoryBean() {
		super(Despesa.class);
	}

	@Override
	public List<Despesa> buscarComFiltro(Despesa despesa, Date dataFim) {
		UaiCriteria<Despesa> uaiCriteria = UaiCriteriaFactory.createQueryCriteria(getEntityManager(), Despesa.class);
		
		if(despesa.getMaquina().getCodigo() != null && !despesa.getMaquina().getCodigo().isEmpty()) {
			uaiCriteria.innerJoin("maquina");
			uaiCriteria.andEquals("maquina.codigo", despesa.getMaquina().getCodigo());
		}
		
		if(despesa.getValor() != null) {
			uaiCriteria.andEquals("valor", despesa.getValor());
		}
		
		Date dataInicio = despesa.getData();
		if(dataInicio != null && dataFim == null) {
			uaiCriteria.andGreaterOrEqualTo("data", dataInicio);
		} else if(dataFim != null && dataInicio == null) {
			uaiCriteria.andLessOrEqualTo("data", dataFim);
		} else if(dataInicio != null && dataFim != null) {
			uaiCriteria.andBetween("data", dataInicio, dataFim);
		}
		
		if(despesa.getNaturezaFinanceira().getId() != null) {
			uaiCriteria.innerJoin("naturezaFinanceira");
			uaiCriteria.andEquals("naturezaFinanceira.id", despesa.getNaturezaFinanceira().getId());
		}
		
		List<Despesa> despesas = (List<Despesa>) uaiCriteria.getResultList();
		
		return despesas;
	}
}
