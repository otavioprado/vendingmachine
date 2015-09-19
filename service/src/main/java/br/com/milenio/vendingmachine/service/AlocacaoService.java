package br.com.milenio.vendingmachine.service;

import javax.ejb.Local;

import br.com.milenio.vendingmachine.domain.model.Alocacao;
import br.com.milenio.vendingmachine.exceptions.InconsistenciaException;

@Local
public interface AlocacaoService {
	public void cadastrar(Alocacao alocacao) throws InconsistenciaException;
}
