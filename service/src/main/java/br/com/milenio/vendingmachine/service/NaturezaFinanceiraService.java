package br.com.milenio.vendingmachine.service;

import java.util.List;

import javax.ejb.Local;

import br.com.milenio.vendingmachine.domain.model.NaturezaFinanceira;
import br.com.milenio.vendingmachine.exceptions.CadastroInexistenteException;
import br.com.milenio.vendingmachine.exceptions.ConteudoJaExistenteNoBancoDeDadosException;
import br.com.milenio.vendingmachine.exceptions.InconsistenciaException;

@Local
public interface NaturezaFinanceiraService {
	public void cadastrar(NaturezaFinanceira naturezaFinanceira) throws ConteudoJaExistenteNoBancoDeDadosException;

	public List<NaturezaFinanceira> buscarNaturezasComFiltro(NaturezaFinanceira naturezaFinanceira) throws CadastroInexistenteException;

	public NaturezaFinanceira findById(Long id);

	public void editar(NaturezaFinanceira naturezaFinanceira);

	public NaturezaFinanceira excluirNaturezaFinanceira(Long idNatureza) throws InconsistenciaException;
}
