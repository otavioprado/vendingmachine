package br.com.milenio.vendingmachine.repository;

import java.util.List;

import javax.ejb.Local;

import br.com.milenio.vendingmachine.domain.Repository;
import br.com.milenio.vendingmachine.domain.model.Maquina;
import br.com.milenio.vendingmachine.domain.model.Reserva;

@Local
public interface ReservaRepository extends Repository<Reserva, Long> {

	Reserva findByMaquina(Maquina maquina);

	public List<Reserva> buscarComFiltro(Reserva reserva);
}
