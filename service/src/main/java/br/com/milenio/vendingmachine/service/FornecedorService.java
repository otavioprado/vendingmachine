package br.com.milenio.vendingmachine.service;

import java.util.List;

import javax.ejb.Local;

import br.com.milenio.vendingmachine.domain.model.Fornecedor;
import br.com.milenio.vendingmachine.exceptions.CadastroInexistenteException;
import br.com.milenio.vendingmachine.exceptions.ConteudoJaExistenteNoBancoDeDadosException;
import br.com.milenio.vendingmachine.exceptions.InconsistenciaException;

@Local
public interface FornecedorService {
	public void cadastrar(Fornecedor fornecedor) throws ConteudoJaExistenteNoBancoDeDadosException;

	public List<Fornecedor> buscarFornecedoresComFiltro(Fornecedor fornecedor) throws CadastroInexistenteException;

	public Fornecedor findById(Long id);

	public void editar(Fornecedor fornecedorEditado) throws ConteudoJaExistenteNoBancoDeDadosException;

	public Fornecedor excluir(Long id) throws InconsistenciaException;

	public Fornecedor findByCodigo(String codigo);
}
