package br.com.milenio.vendingmachine.service;

import java.util.List;

import javax.ejb.Local;

import br.com.milenio.vendingmachine.domain.model.Maquina;
import br.com.milenio.vendingmachine.domain.model.Reserva;
import br.com.milenio.vendingmachine.exceptions.CadastroInexistenteException;
import br.com.milenio.vendingmachine.exceptions.InconsistenciaException;

@Local
public interface ReservaService {
	void cadastrar(Reserva reserva) throws InconsistenciaException;

	public List<Reserva> buscarComFiltro(Reserva reserva) throws CadastroInexistenteException;

	Reserva findByMaquina(Maquina maquina);

	Reserva excluir(Long id) throws InconsistenciaException;
}
