package br.com.milenio.vendingmachine.repository;

import javax.ejb.Stateless;

import br.com.milenio.vendingmachine.domain.AbstractVendingMachineRepositoryBean;
import br.com.milenio.vendingmachine.domain.model.Manutencao;

@Stateless(name = "ManutencaoRepository")
public class ManutencaoRepositoryBean extends AbstractVendingMachineRepositoryBean<Manutencao, Long> 
	implements ManutencaoRepository {

	public ManutencaoRepositoryBean() {
		super(Manutencao.class);
	}
}
