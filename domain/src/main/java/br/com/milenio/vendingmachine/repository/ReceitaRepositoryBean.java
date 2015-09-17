package br.com.milenio.vendingmachine.repository;

import java.util.Date;
import java.util.List;

import javax.ejb.Stateless;

import com.uaihebert.uaicriteria.UaiCriteria;
import com.uaihebert.uaicriteria.UaiCriteriaFactory;

import br.com.milenio.vendingmachine.domain.AbstractVendingMachineRepositoryBean;
import br.com.milenio.vendingmachine.domain.model.Receita;

@Stateless(name = "ReceitaRepository")
public class ReceitaRepositoryBean extends AbstractVendingMachineRepositoryBean<Receita, Long> 
	implements ReceitaRepository {

	public ReceitaRepositoryBean() {
		super(Receita.class);
	}

	@Override
	public List<Receita> buscarComFiltro(Receita receita, Date dataFim) {
		UaiCriteria<Receita> uaiCriteria = UaiCriteriaFactory.createQueryCriteria(getEntityManager(), Receita.class);
		
		if(receita.getMaquina().getCodigo() != null && !receita.getMaquina().getCodigo().isEmpty()) {
			uaiCriteria.innerJoin("maquina");
			uaiCriteria.andEquals("maquina.codigo", receita.getMaquina().getCodigo());
		}
		
		if(receita.getValor() != null) {
			uaiCriteria.andEquals("valor", receita.getValor());
		}
		
		Date dataInicio = receita.getData();
		if(dataInicio != null && dataFim == null) {
			uaiCriteria.andGreaterOrEqualTo("data", dataInicio);
		} else if(dataFim != null && dataInicio == null) {
			uaiCriteria.andLessOrEqualTo("data", dataFim);
		} else if(dataInicio != null && dataFim != null) {
			uaiCriteria.andBetween("data", dataInicio, dataFim);
		}
		
		if(receita.getNaturezaFinanceira().getId() != null) {
			uaiCriteria.innerJoin("naturezaFinanceira");
			uaiCriteria.andEquals("naturezaFinanceira.id", receita.getNaturezaFinanceira().getId());
		}
		
		List<Receita> receitas = (List<Receita>) uaiCriteria.getResultList();
		
		return receitas;
	}
}
