package br.com.milenio.vendingmachine.service;

import java.util.List;

import javax.ejb.Local;

import br.com.milenio.vendingmachine.domain.model.Contrato;
import br.com.milenio.vendingmachine.exceptions.CadastroInexistenteException;
import br.com.milenio.vendingmachine.exceptions.ConteudoJaExistenteNoBancoDeDadosException;
import br.com.milenio.vendingmachine.exceptions.InconsistenciaException;

@Local
public interface ContratoService {

	void cadastrar(Contrato contrato) throws ConteudoJaExistenteNoBancoDeDadosException;

	List<Contrato> buscarContratosComFiltro(Contrato contratoConsParam) throws CadastroInexistenteException;

	Contrato excluir(Long id) throws InconsistenciaException;

	void editar(Contrato contrato) throws InconsistenciaException;

	Contrato findById(Long id);
}
