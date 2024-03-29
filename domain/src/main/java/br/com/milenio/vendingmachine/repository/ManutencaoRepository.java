package br.com.milenio.vendingmachine.repository;

import java.util.List;

import javax.ejb.Local;

import br.com.milenio.vendingmachine.domain.Repository;
import br.com.milenio.vendingmachine.domain.model.Manutencao;
import br.com.milenio.vendingmachine.domain.model.Maquina;

@Local
public interface ManutencaoRepository extends Repository<Manutencao, Long> {

	List<Manutencao> buscarComFiltro(Manutencao manutencao);
}
