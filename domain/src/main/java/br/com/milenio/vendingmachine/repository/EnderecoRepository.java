package br.com.milenio.vendingmachine.repository;

import javax.ejb.Local;

import br.com.milenio.vendingmachine.domain.Repository;
import br.com.milenio.vendingmachine.domain.model.Endereco;

@Local
public interface EnderecoRepository extends Repository<Endereco, Long> {
	
}
