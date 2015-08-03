package br.com.milenio.vendingmachine.repository;

import javax.ejb.Stateless;

import br.com.milenio.vendingmachine.domain.AbstractVendingMachineRepositoryBean;
import br.com.milenio.vendingmachine.domain.model.Permissao;

@Stateless(name = "PermissaoRepository")
public class PermissaoRepositoryBean extends AbstractVendingMachineRepositoryBean<Permissao, Long> 
	implements PermissaoRepository {

	public PermissaoRepositoryBean() {
		super(Permissao.class);
	}
}
