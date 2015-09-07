package br.com.milenio.vendingmachine.repository;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.TypedQuery;

import br.com.milenio.vendingmachine.domain.AbstractVendingMachineRepositoryBean;
import br.com.milenio.vendingmachine.domain.model.Maquina;

@Stateless(name = "MaquinaRepository")
public class MaquinaRepositoryBean extends AbstractVendingMachineRepositoryBean<Maquina, Long> 
	implements MaquinaRepository {

	public MaquinaRepositoryBean() {
		super(Maquina.class);
	}

	@Override
	public Maquina findByCodigo(String codigo) {
		StringBuilder sb = new StringBuilder();
		sb.append("SELECT m FROM Maquina m WHERE ");
		sb.append(" m.codigo = :codigo ");

		TypedQuery<Maquina> query = getEntityManager().createQuery(sb.toString(), Maquina.class);

		query.setParameter("codigo", codigo);

		List<Maquina> maquinas = (List<Maquina>) query.getResultList();
		
		if(!maquinas.isEmpty()) {
			return maquinas.get(0);
		}
		
		return null;
	}
}
