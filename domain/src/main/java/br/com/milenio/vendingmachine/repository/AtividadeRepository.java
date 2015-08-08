package br.com.milenio.vendingmachine.repository;

import javax.ejb.Local;

import br.com.milenio.vendingmachine.domain.Repository;
import br.com.milenio.vendingmachine.domain.model.Atividade;

@Local
public interface AtividadeRepository extends Repository<Atividade, Long> {

}
