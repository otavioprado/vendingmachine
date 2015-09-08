package br.com.milenio.vendingmachine.repository;

import java.util.List;

import javax.ejb.Local;

import br.com.milenio.vendingmachine.domain.Repository;
import br.com.milenio.vendingmachine.domain.model.Fornecedor;

@Local
public interface FornecedorRepository extends Repository<Fornecedor, Long> {

	public Fornecedor findFornecedorByCodigoEmailCpfCnpj(Fornecedor fornecedor);
	
	public List<Fornecedor> buscarFornecedoresComFiltro(Fornecedor fornecedor);

	public Fornecedor findByCodigo(String codigo);

	public Fornecedor findFornecedorByEmail(String email);

	public Fornecedor findFornecedorByCpfCnpj(String cpfCnpj);
}
