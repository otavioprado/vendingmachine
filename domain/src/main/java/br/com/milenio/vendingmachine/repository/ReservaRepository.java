package br.com.milenio.vendingmachine.repository;

import javax.ejb.Local;

import br.com.milenio.vendingmachine.domain.Repository;
import br.com.milenio.vendingmachine.domain.model.Maquina;
import br.com.milenio.vendingmachine.domain.model.Reserva;

@Local
public interface ReservaRepository extends Repository<Reserva, Long> {

	Reserva findByMaquina(Maquina maquina);

	Reserva buscarComFiltro(Reserva reserva);
}
