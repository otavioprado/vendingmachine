package br.com.milenio.vendingmachine.repository;

import java.util.Date;
import java.util.List;

import javax.ejb.Local;

import br.com.milenio.vendingmachine.domain.Repository;
import br.com.milenio.vendingmachine.domain.model.Despesa;

@Local
public interface DespesaRepository extends Repository<Despesa, Long> {

	List<Despesa> buscarComFiltro(Despesa despesa, Date dataFim);
}
