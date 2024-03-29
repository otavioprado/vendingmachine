package br.com.milenio.vendingmachine.repository;

import java.util.List;

import javax.ejb.Local;

import br.com.milenio.vendingmachine.domain.Repository;
import br.com.milenio.vendingmachine.domain.model.UsuarioSistema;

@Local
public interface UsuarioSistemaRepository extends Repository<UsuarioSistema, Long> {
	UsuarioSistema findUsuarioByLoginEquals(String login);
	
	UsuarioSistema findUsuarioByLoginAndEmail(UsuarioSistema usuario);
	
	List<Long> findClientesComEmail();

	List<UsuarioSistema> buscarUsuariosComFiltro(String login, Boolean status, Long perfilId);

	UsuarioSistema findUsuarioByLogin(String login);

	UsuarioSistema findUsuarioByEmail(String email);
}
