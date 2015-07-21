package br.com.milenio.vendingmachine.repository;

import java.util.Set;

import br.com.milenio.vendingmachine.domain.Repository;
import br.com.milenio.vendingmachine.domain.model.Perfil;
import br.com.milenio.vendingmachine.domain.model.Permissao;

public interface PerfilRepository extends Repository<Perfil, Long> {
	public Set<Permissao> getPermissoesPerfil(Perfil perfil);
}
