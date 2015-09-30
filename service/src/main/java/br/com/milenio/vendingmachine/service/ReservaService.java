package br.com.milenio.vendingmachine.service;

import javax.ejb.Local;

import br.com.milenio.vendingmachine.domain.model.Reserva;
import br.com.milenio.vendingmachine.exceptions.InconsistenciaException;

@Local
public interface ReservaService {
	void cadastrar(Reserva reserva) throws InconsistenciaException;

	Reserva buscarComFiltro(Reserva reserva);
}
