package br.com.milenio.vendingmachine.service;

import javax.ejb.Local;

import br.com.milenio.vendingmachine.domain.model.UsuarioSistema;

@Local
public interface UsuarioService {
	public boolean cadastrarUsuario(UsuarioSistema usuario);
}
