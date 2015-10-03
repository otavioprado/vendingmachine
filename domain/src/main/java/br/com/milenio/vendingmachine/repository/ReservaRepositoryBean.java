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
	public List<Reserva> buscarComFiltro(Reserva reserva) {
		UaiCriteria<Reserva> uaiCriteria = UaiCriteriaFactory.createQueryCriteria(getEntityManager(), Reserva.class);

		Maquina maquina = reserva.getMaquina();
		Cliente cliente = reserva.getCliente();
		
		if(maquina != null && maquina.getCodigo() != null && !maquina.getCodigo().isEmpty()) {
			uaiCriteria.innerJoin("maquina");
			uaiCriteria.andEquals("maquina.codigo", maquina.getCodigo());
		}
		
		if(cliente != null && cliente.getCodigo() != null && !cliente.getCodigo().isEmpty()) {
			uaiCriteria.innerJoin("cliente");
			uaiCriteria.andEquals("cliente.codigo", cliente.getCodigo());
		}
		
		if(reserva.getData() != null) {
			uaiCriteria.andEquals("data", reserva.getData());
		}
		
		List<Reserva> reservas = (List<Reserva>) uaiCriteria.getResultList();
		
		return reservas;
	}
}
