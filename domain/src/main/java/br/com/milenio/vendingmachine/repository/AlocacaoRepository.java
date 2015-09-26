package br.com.milenio.vendingmachine.repository;

import java.util.List;

import javax.ejb.Local;

import br.com.milenio.vendingmachine.domain.Repository;
import br.com.milenio.vendingmachine.domain.model.Alocacao;

@Local
public interface AlocacaoRepository extends Repository<Alocacao, Long> {

	List<Alocacao> findAlocacoesAtivasByCliente(Long clienteId);
}
