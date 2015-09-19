package br.com.milenio.vendingmachine.repository;

import javax.ejb.Stateless;

import br.com.milenio.vendingmachine.domain.AbstractVendingMachineRepositoryBean;
import br.com.milenio.vendingmachine.domain.model.Alocacao;

@Stateless(name = "AlocacaoRepository")
public class AlocacaoRepositoryBean extends AbstractVendingMachineRepositoryBean<Alocacao, Long> 
	implements AlocacaoRepository {

	public AlocacaoRepositoryBean() {
		super(Alocacao.class);
	}
}
