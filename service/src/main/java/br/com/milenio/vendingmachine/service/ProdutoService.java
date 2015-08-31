package br.com.milenio.vendingmachine.service;

import javax.ejb.Local;

import br.com.milenio.vendingmachine.domain.model.Produto;
import br.com.milenio.vendingmachine.exceptions.ConteudoJaExistenteNoBancoDeDadosException;

@Local
public interface ProdutoService {
	public void cadastrar(Produto novoProduto) throws ConteudoJaExistenteNoBancoDeDadosException;
}
