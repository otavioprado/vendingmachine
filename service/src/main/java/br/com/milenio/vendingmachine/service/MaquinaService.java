package br.com.milenio.vendingmachine.service;

import javax.ejb.Local;

import br.com.milenio.vendingmachine.domain.model.Maquina;
import br.com.milenio.vendingmachine.exceptions.ConteudoJaExistenteNoBancoDeDadosException;

@Local
public interface MaquinaService {
	public void cadastrar(Maquina maquina) throws ConteudoJaExistenteNoBancoDeDadosException;
}
