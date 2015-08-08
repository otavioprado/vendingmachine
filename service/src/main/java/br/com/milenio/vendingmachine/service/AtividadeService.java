package br.com.milenio.vendingmachine.service;

import javax.ejb.Local;

import br.com.milenio.vendingmachine.domain.model.Atividade;
import br.com.milenio.vendingmachine.exceptions.CadastroInexistenteException;

@Local
public interface AtividadeService {
	public void cadastrarAtividade(Atividade atividade) throws CadastroInexistenteException;
}
