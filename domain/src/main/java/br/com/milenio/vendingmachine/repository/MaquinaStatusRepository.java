package br.com.milenio.vendingmachine.repository;

import javax.ejb.Local;

import br.com.milenio.vendingmachine.domain.Repository;
import br.com.milenio.vendingmachine.domain.model.MaquinaStatus;

@Local
public interface MaquinaStatusRepository extends Repository<MaquinaStatus, Long> {

	MaquinaStatus findByDescricao(String descricao);
	
}
