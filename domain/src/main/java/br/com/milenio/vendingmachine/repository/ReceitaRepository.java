package br.com.milenio.vendingmachine.repository;

import java.util.Date;
import java.util.List;

import javax.ejb.Local;

import br.com.milenio.vendingmachine.domain.Repository;
import br.com.milenio.vendingmachine.domain.model.Receita;

@Local
public interface ReceitaRepository extends Repository<Receita, Long> {

	List<Receita> buscarComFiltro(Receita receita, Date dataFim);
}
