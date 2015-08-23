package br.com.milenio.vendingmachine.repository;

import javax.ejb.Stateless;

import br.com.milenio.vendingmachine.domain.AbstractVendingMachineRepositoryBean;
import br.com.milenio.vendingmachine.domain.model.Endereco;

@Stateless(name = "EnderecoRepository")
public class EnderecoRepositoryBean extends AbstractVendingMachineRepositoryBean<Endereco, Long> 
	implements EnderecoRepository {
	
	public EnderecoRepositoryBean() {
		super(Endereco.class);
	}
}
