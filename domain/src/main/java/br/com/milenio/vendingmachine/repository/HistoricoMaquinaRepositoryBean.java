package br.com.milenio.vendingmachine.repository;

import javax.ejb.Stateless;

import br.com.milenio.vendingmachine.domain.AbstractVendingMachineRepositoryBean;
import br.com.milenio.vendingmachine.domain.model.HistoricoMaquina;

@Stateless(name = "HistoricoMaquinaRepository")
public class HistoricoMaquinaRepositoryBean extends AbstractVendingMachineRepositoryBean<HistoricoMaquina, Long> 
	implements HistoricoMaquinaRepository {

	public HistoricoMaquinaRepositoryBean() {
		super(HistoricoMaquina.class);
	}
}
