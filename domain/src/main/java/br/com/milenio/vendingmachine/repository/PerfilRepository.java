package br.com.milenio.vendingmachine.repository;

import java.util.List;

import br.com.milenio.vendingmachine.domain.Repository;
import br.com.milenio.vendingmachine.domain.model.Perfil;
import br.com.milenio.vendingmachine.domain.model.Permissao;

public interface PerfilRepository extends Repository<Perfil, Long> {
	public List<Permissao> getPermissoesPerfil(Perfil perfil);
}
