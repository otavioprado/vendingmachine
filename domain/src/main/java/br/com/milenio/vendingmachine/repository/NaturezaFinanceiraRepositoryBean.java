package br.com.milenio.vendingmachine.repository;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.TypedQuery;

import com.uaihebert.uaicriteria.UaiCriteria;
import com.uaihebert.uaicriteria.UaiCriteriaFactory;

import br.com.milenio.vendingmachine.domain.AbstractVendingMachineRepositoryBean;
import br.com.milenio.vendingmachine.domain.model.NaturezaFinanceira;

@Stateless(name = "NaturezaFinanceiraRepository")
public class NaturezaFinanceiraRepositoryBean extends AbstractVendingMachineRepositoryBean<NaturezaFinanceira, Long>
		implements NaturezaFinanceiraRepository {

	public NaturezaFinanceiraRepositoryBean() {
		super(NaturezaFinanceira.class);
	}

	@Override
	public NaturezaFinanceira findNaturezaFinanceiraByCodigoDescricao(NaturezaFinanceira naturezaFinanceira) {
		StringBuilder sb = new StringBuilder();
		sb.append("SELECT c FROM NaturezaFinanceira c WHERE ");
		sb.append(" c.codigo = :codigo OR");
		sb.append(" c.descricao = :descricao ");

		TypedQuery<NaturezaFinanceira> query = getEntityManager().createQuery(
				sb.toString(), NaturezaFinanceira.class);

		query.setParameter("codigo", naturezaFinanceira.getCodigo());
		query.setParameter("descricao", naturezaFinanceira.getDescricao());

		List<NaturezaFinanceira> naturezaFinanceirasCadastrada = query
				.getResultList();

		if (!naturezaFinanceirasCadastrada.isEmpty()) {
			return naturezaFinanceirasCadastrada.get(0);
		}

		return null;
	}

	@Override
	public List<NaturezaFinanceira> buscarNaturezasComFiltro(NaturezaFinanceira naturezaFinanceira) {
		
		String codigo = naturezaFinanceira.getCodigo();
		String descricao = naturezaFinanceira.getDescricao();
		String tipoNaturezaFinanceira = naturezaFinanceira.getTipoNaturezaFinanceira();
		
		UaiCriteria<NaturezaFinanceira> uaiCriteria = UaiCriteriaFactory.createQueryCriteria(getEntityManager(), NaturezaFinanceira.class);
		
		if(codigo != null && !codigo.isEmpty()) {
			uaiCriteria.andEquals("codigo", codigo);
		}
		
		if(descricao != null && !descricao.isEmpty()) {
			uaiCriteria.andEquals("descricao", descricao);
		}
		
		if(tipoNaturezaFinanceira != null && !tipoNaturezaFinanceira.isEmpty()) {
			uaiCriteria.andEquals("tipoNaturezaFinanceira", tipoNaturezaFinanceira);
		}
		
		List<NaturezaFinanceira> naturezasFinanceiras = (List<NaturezaFinanceira>) uaiCriteria.getResultList();
		
		return naturezasFinanceiras;

	}

	@Override
	public NaturezaFinanceira findByDescricao(String descricao) {
		StringBuilder sb = new StringBuilder();
		sb.append("SELECT nf FROM NaturezaFinanceira nf WHERE ");
		sb.append(" nf.descricao = :descricao ");

		TypedQuery<NaturezaFinanceira> query = getEntityManager().createQuery(sb.toString(), NaturezaFinanceira.class);

		query.setParameter("descricao", descricao);

		List<NaturezaFinanceira> naturezaFinanceirasCadastrada = query.getResultList();

		if (!naturezaFinanceirasCadastrada.isEmpty()) {
			return naturezaFinanceirasCadastrada.get(0);
		}

		return null;
	}
}
