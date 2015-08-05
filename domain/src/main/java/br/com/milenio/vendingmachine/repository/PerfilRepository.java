package br.com.milenio.vendingmachine.repository;

import java.util.List;

import javax.ejb.Local;

import br.com.milenio.vendingmachine.domain.Repository;
import br.com.milenio.vendingmachine.domain.model.Perfil;
import br.com.milenio.vendingmachine.domain.model.Permissao;

@Local
public interface PerfilRepository extends Repository<Perfil, Long> {
	public List<Permissao> getPermissoesPerfil(Perfil perfil);
}
