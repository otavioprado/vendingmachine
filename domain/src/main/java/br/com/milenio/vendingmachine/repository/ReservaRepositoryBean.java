package br.com.milenio.vendingmachine.repository;

import java.util.List;

import javax.ejb.Stateless;

import com.uaihebert.uaicriteria.UaiCriteria;
import com.uaihebert.uaicriteria.UaiCriteriaFactory;

import br.com.milenio.vendingmachine.domain.AbstractVendingMachineRepositoryBean;
import br.com.milenio.vendingmachine.domain.model.Cliente;
import br.com.milenio.vendingmachine.domain.model.Maquina;
import br.com.milenio.vendingmachine.domain.model.Reserva;

@Stateless(name = "ReservaRepository")
public class ReservaRepositoryBean extends AbstractVendingMachineRepositoryBean<Reserva, Long> 
	implements ReservaRepository {

	public ReservaRepositoryBean() {
		super(Reserva.class);
	}

	@Override
	public Reserva findByMaquina(Maquina maquina) {
		UaiCriteria<Reserva> uaiCriteria = UaiCriteriaFactory.createQueryCriteria(getEntityManager(), Reserva.class);

		uaiCriteria.innerJoin("maquina");
		uaiCriteria.andEquals("maquina.id", maquina.getId());
		
		List<Reserva> reservas = (List<Reserva>) uaiCriteria.getResultList();
		
		if(!reservas.isEmpty()) {
			return reservas.get(0);
		}
		
		return null;
	}

	@Override
	public Reserva buscarComFiltro(Reserva reserva) {
		UaiCriteria<Reserva> uaiCriteria = UaiCriteriaFactory.createQueryCriteria(getEntityManager(), Reserva.class);

		Maquina maquina = reserva.getMaquina();
		Cliente cliente = reserva.getCliente();
		
		if(maquina != null && maquina.getId() != null) {
			uaiCriteria.innerJoin("maquina");
			uaiCriteria.andEquals("maquina.id", maquina.getId());
		}
		
		if(cliente != null && cliente.getId() != null) {
			uaiCriteria.innerJoin("cliente");
			uaiCriteria.andEquals("cliente.id", cliente.getId());
		}
		
		List<Reserva> reservas = (List<Reserva>) uaiCriteria.getResultList();
		
		if(!reservas.isEmpty()) {
			return reservas.get(0);
		}
		
		return null;
	}
}
