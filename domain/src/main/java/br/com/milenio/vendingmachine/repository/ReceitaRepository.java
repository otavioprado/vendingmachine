package br.com.milenio.vendingmachine.repository;

import javax.ejb.Local;

import br.com.milenio.vendingmachine.domain.Repository;
import br.com.milenio.vendingmachine.domain.model.Receita;

@Local
public interface ReceitaRepository extends Repository<Receita, Long> {
	
}
