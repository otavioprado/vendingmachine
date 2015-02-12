package br.com.milenio.vendingmachine.repository;

import java.util.List;

import javax.ejb.Local;

import br.com.milenio.vendingmachine.domain.model.Usuario;

@Local
public interface UsuarioRepository {
	Usuario findUsuarioByLoginEquals(String login);
	
	List<Long> findClientesComEmail();
}
