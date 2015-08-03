package br.com.milenio.vendingmachine.repository;

import java.util.List;

import javax.ejb.Stateless;

import br.com.milenio.vendingmachine.domain.AbstractVendingMachineRepositoryBean;
import br.com.milenio.vendingmachine.domain.model.Perfil;
import br.com.milenio.vendingmachine.domain.model.Permissao;

@Stateless(name = "PerfilRepository")
public class PerfilRepositoryBean extends AbstractVendingMachineRepositoryBean<Perfil, Long> 
	implements PerfilRepository {

	public PerfilRepositoryBean() {
		super(Perfil.class);
	}
	
	public List<Permissao> getPermissoesPerfil(Perfil perfil) {
		List<Permissao> permissoes = perfil.getPermissoes();
		
		return permissoes;
	}
	
}
