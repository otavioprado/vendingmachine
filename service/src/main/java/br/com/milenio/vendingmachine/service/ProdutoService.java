package br.com.milenio.vendingmachine.service;

import java.util.List;

import javax.ejb.Local;

import br.com.milenio.vendingmachine.domain.model.Produto;
import br.com.milenio.vendingmachine.exceptions.CadastroInexistenteException;
import br.com.milenio.vendingmachine.exceptions.ConteudoJaExistenteNoBancoDeDadosException;
import br.com.milenio.vendingmachine.exceptions.InconsistenciaException;

@Local
public interface ProdutoService {
	public void cadastrar(Produto novoProduto) throws ConteudoJaExistenteNoBancoDeDadosException, InconsistenciaException;

	public List<Produto> buscarComFiltro(Produto produto) throws CadastroInexistenteException;

	public Produto excluir(Long id);

	public Produto findById(Long id);

	public void editar(Produto produto) throws InconsistenciaException;
}
