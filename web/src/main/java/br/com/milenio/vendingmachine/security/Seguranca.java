package br.com.milenio.vendingmachine.security;

import javax.enterprise.context.RequestScoped;
import javax.faces.context.FacesContext;
import javax.inject.Named;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

@Named
@RequestScoped
public class Seguranca {

	public String getNomeUsuarioLogado() {
		String nome = null;
		UsuarioSistemaSpring usuario = getUsuarioSistemaSpringLogado();

		if (usuario != null) {
			nome = usuario.getUsuario().getNome();
		}
		
		return nome;
	}
	
	public String getEmailUsuarioLogado() {
		String email = null;
		UsuarioSistemaSpring usuario = getUsuarioSistemaSpringLogado();

		if (usuario != null) {
			email = usuario.getUsuario().getEmail();
		}
		
		return email;
	}

	private UsuarioSistemaSpring getUsuarioSistemaSpringLogado() {
		UsuarioSistemaSpring usuario = null;
		
		UsernamePasswordAuthenticationToken auth = (UsernamePasswordAuthenticationToken)
				FacesContext.getCurrentInstance().getExternalContext().getUserPrincipal();
		
		if(auth != null && auth.getPrincipal() != null){
			usuario = (UsuarioSistemaSpring) auth.getPrincipal();
		}
		
		return usuario;
	}
}
