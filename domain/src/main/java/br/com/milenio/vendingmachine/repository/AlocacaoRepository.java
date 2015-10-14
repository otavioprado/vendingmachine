package br.com.milenio.vendingmachine.repository;

import java.util.List;

import javax.ejb.Local;

import br.com.milenio.vendingmachine.domain.Repository;
import br.com.milenio.vendingmachine.domain.model.Alocacao;
import br.com.milenio.vendingmachine.domain.model.Maquina;

@Local
public interface AlocacaoRepository extends Repository<Alocacao, Long> {

	List<Alocacao> findAlocacoesByCliente(Long clienteId);

	List<Alocacao> buscarComFiltro(Alocacao alocacao);

	List<Alocacao> findAlocacoesAtivasByCliente(Long clienteId);

	List<Alocacao> findAlocacoesPendentesAlocacao();

	List<Alocacao> findAlocacoesAtivas();

	Alocacao findAlocacaoAtualmenteAtivaParaUmaMaquina(Maquina maquina);

	List<Alocacao> findAlocacoesPendentesDesalocacao();
}
