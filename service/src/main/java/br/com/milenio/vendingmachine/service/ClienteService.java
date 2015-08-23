package br.com.milenio.vendingmachine.service;

import javax.ejb.Local;

import br.com.milenio.vendingmachine.domain.model.Cliente;
import br.com.milenio.vendingmachine.exceptions.ConteudoJaExistenteNoBancoDeDadosException;

@Local
public interface ClienteService {
	public void cadastrar(Cliente cliente) throws ConteudoJaExistenteNoBancoDeDadosException;
}
