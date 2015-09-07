package br.com.milenio.vendingmachine.repository;

import javax.ejb.Local;

import br.com.milenio.vendingmachine.domain.Repository;
import br.com.milenio.vendingmachine.domain.model.Maquina;

@Local
public interface MaquinaRepository extends Repository<Maquina, Long> {
	public Maquina findByCodigo(String codigo);
}
