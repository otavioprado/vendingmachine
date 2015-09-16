package br.com.milenio.vendingmachine.security;

import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.faces.context.FacesContext;
import javax.inject.Named;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

import br.com.milenio.vendingmachine.domain.model.Permissao;
import br.com.milenio.vendingmachine.domain.model.UsuarioSistema;
import br.com.milenio.vendingmachine.repository.PerfilRepository;

@Named
@RequestScoped
public class Seguranca {
	
	@EJB
	PerfilRepository perfilSistemaRepository;

	public String getNomeUsuarioLogado() {
		String nome = null;
		UsuarioSistemaSpring usuario = getUsuarioSistemaSpringLogado();

		if (usuario != null) {
			nome = usuario.getUsuario().getNome();
		}
		
		return nome;
	}
	
	public static Long getIdUsuarioLogado() {
		Long id = null;
		UsuarioSistemaSpring usuario = getUsuarioSistemaSpringLogado();

		if (usuario != null) {
			id = usuario.getUsuario().getId();
		}
		
		return id;
	}
	
	public static UsuarioSistema getUsuarioLogado() {
		UsuarioSistema usuarioSistema = null;
		UsuarioSistemaSpring usuario = getUsuarioSistemaSpringLogado();

		if (usuario != null) {
			usuarioSistema = usuario.getUsuario();
		}
		
		return usuarioSistema;
	}
	
	public String getEmailUsuarioLogado() {
		String email = null;
		UsuarioSistemaSpring usuario = getUsuarioSistemaSpringLogado();

		if (usuario != null) {
			email = usuario.getUsuario().getEmail();
		}
		
		return email;
	}
	
	public static String getLoginUsuarioLogado() {
		String login = null;
		UsuarioSistemaSpring usuario = getUsuarioSistemaSpringLogado();

		if (usuario != null) {
			login = usuario.getUsuario().getLogin();
		}
		
		return login;
	}
	
	public String getPerfilUsuarioLogado() {
		String perfil = null;
		UsuarioSistemaSpring usuario = getUsuarioSistemaSpringLogado();

		if (usuario != null) {
			perfil = usuario.getUsuario().getPerfil().getNome();
		}
		
		return perfil;
	}
	
	public boolean getPermissaoUsuarioLogado(String permissao) {
		UsuarioSistemaSpring usuario = getUsuarioSistemaSpringLogado();

		if (usuario != null) {
			for(Permissao perm : usuario.getUsuario().getPerfil().getPermissoes()) {
				if(perm.getNome().equals(permissao)) {
					return true;
				}
			}
		}
		
		return false;
	}

	private static UsuarioSistemaSpring getUsuarioSistemaSpringLogado() {
		UsuarioSistemaSpring usuario = null;
		
		UsernamePasswordAuthenticationToken auth = (UsernamePasswordAuthenticationToken)
				FacesContext.getCurrentInstance().getExternalContext().getUserPrincipal();
		
		if(auth != null && auth.getPrincipal() != null){
			usuario = (UsuarioSistemaSpring) auth.getPrincipal();
		}
		
		return usuario;
	}
	
	
}
