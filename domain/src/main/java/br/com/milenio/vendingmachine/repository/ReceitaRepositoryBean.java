package br.com.milenio.vendingmachine.repository;

import javax.ejb.Stateless;

import br.com.milenio.vendingmachine.domain.AbstractVendingMachineRepositoryBean;
import br.com.milenio.vendingmachine.domain.model.Receita;

@Stateless(name = "ReceitaRepository")
public class ReceitaRepositoryBean extends AbstractVendingMachineRepositoryBean<Receita, Long> 
	implements ReceitaRepository {

	public ReceitaRepositoryBean() {
		super(Receita.class);
	}
}
