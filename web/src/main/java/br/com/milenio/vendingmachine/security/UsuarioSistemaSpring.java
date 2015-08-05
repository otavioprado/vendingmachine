package br.com.milenio.vendingmachine.security;

import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;

import br.com.milenio.vendingmachine.domain.model.UsuarioSistema;

@Component
public class UsuarioSistemaSpring extends User {
	private static final long serialVersionUID = 1L;
	private UsuarioSistema usuario;

	public UsuarioSistemaSpring(UsuarioSistema usuario, Collection<? extends GrantedAuthority> authorities) {
		// O spring security já tem o login e senha informado no login.xhtml (j_username e j_password)
		super(usuario.getLogin(), usuario.getSenhaAplicacao(), authorities);
		this.usuario = usuario;
	}

	public UsuarioSistema getUsuario() {
		return usuario;
	}
}
