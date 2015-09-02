package br.com.milenio.vendingmachine.repository;

import java.util.List;

import javax.ejb.Local;

import br.com.milenio.vendingmachine.domain.Repository;
import br.com.milenio.vendingmachine.domain.model.Produto;

@Local
public interface ProdutoRepository extends Repository<Produto, Long> {

	public Produto findByCodigo(String codigo);

	public List<Produto> buscarComFiltro(Produto produto);
	
}
