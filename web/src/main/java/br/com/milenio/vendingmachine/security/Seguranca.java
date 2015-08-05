package br.com.milenio.vendingmachine.security;

import java.util.Set;

import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.faces.context.FacesContext;
import javax.inject.Named;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

import br.com.milenio.vendingmachine.domain.model.Permissao;
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
	
	public String getEmailUsuarioLogado() {
		String email = null;
		UsuarioSistemaSpring usuario = getUsuarioSistemaSpringLogado();

		if (usuario != null) {
			email = usuario.getUsuario().getEmail();
		}
		
		return email;
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
