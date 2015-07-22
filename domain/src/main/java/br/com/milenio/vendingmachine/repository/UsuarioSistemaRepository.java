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

	List<UsuarioSistema> buscarUsuariosComFiltro(String nome, Boolean status, Long perfilId);
}
