package br.com.milenio.vendingmachine.repository;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.TypedQuery;

import br.com.milenio.vendingmachine.domain.AbstractVendingMachineRepositoryBean;
import br.com.milenio.vendingmachine.domain.model.MaquinaStatus;

@Stateless(name = "MaquinaStatusRepository")
public class MaquinaStatusRepositoryBean extends AbstractVendingMachineRepositoryBean<MaquinaStatus, Long> 
	implements MaquinaStatusRepository {

	public MaquinaStatusRepositoryBean() {
		super(MaquinaStatus.class);
	}

	@Override
	public MaquinaStatus findByDescricao(String descricao) {
		StringBuilder sb = new StringBuilder();
		sb.append("SELECT ms FROM MaquinaStatus ms WHERE ");
		sb.append(" ms.descricao = :descricao");

		TypedQuery<MaquinaStatus> query = getEntityManager().createQuery(sb.toString(), MaquinaStatus.class);

		query.setParameter("descricao", descricao);

		List<MaquinaStatus> status = (List<MaquinaStatus>) query.getResultList();
		
		if(!status.isEmpty()) {
			return status.get(0);
		}
		
		return null;
	}
}
