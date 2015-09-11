package br.com.milenio.vendingmachine.repository;

import javax.ejb.Local;

import br.com.milenio.vendingmachine.domain.Repository;
import br.com.milenio.vendingmachine.domain.model.Manutencao;

@Local
public interface ManutencaoRepository extends Repository<Manutencao, Long> {
	
}
