package br.com.milenio.vendingmachine.repository;

import javax.ejb.Stateless;

import br.com.milenio.vendingmachine.domain.AbstractVendingMachineRepositoryBean;
import br.com.milenio.vendingmachine.domain.model.Atividade;

@Stateless(name = "AtividadeRepository")
public class AtividadeRepositoryBean extends AbstractVendingMachineRepositoryBean<Atividade, Long> 
	implements AtividadeRepository {

	public AtividadeRepositoryBean() {
		super(Atividade.class);
	}
	
}
