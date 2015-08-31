package br.com.milenio.vendingmachine.repository;

import javax.ejb.Local;

import br.com.milenio.vendingmachine.domain.Repository;
import br.com.milenio.vendingmachine.domain.model.Produto;

@Local
public interface ProdutoRepository extends Repository<Produto, Long> {

	public Produto findByCodigo(String codigo);
	
}
