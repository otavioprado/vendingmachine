package br.com.milenio.vendingmachine.repository;

import javax.ejb.Local;

import br.com.milenio.vendingmachine.domain.Repository;
import br.com.milenio.vendingmachine.domain.model.HistoricoMaquina;

@Local
public interface HistoricoMaquinaRepository extends Repository<HistoricoMaquina, Long> {

}
