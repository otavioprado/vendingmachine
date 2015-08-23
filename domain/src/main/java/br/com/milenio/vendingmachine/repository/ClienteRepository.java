package br.com.milenio.vendingmachine.repository;

import javax.ejb.Local;

import br.com.milenio.vendingmachine.domain.Repository;
import br.com.milenio.vendingmachine.domain.model.Cliente;

@Local
public interface ClienteRepository extends Repository<Cliente, Long> {

	public Cliente findClienteByCodigoEmailCpfCnpj(Cliente cliente);
	
}
