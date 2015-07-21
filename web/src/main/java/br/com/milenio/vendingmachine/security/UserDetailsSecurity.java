package br.com.milenio.vendingmachine.security;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.ejb.EJB;
import javax.naming.InitialContext;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import br.com.milenio.vendingmachine.domain.model.Perfil;
import br.com.milenio.vendingmachine.domain.model.Permissao;
import br.com.milenio.vendingmachine.domain.model.UsuarioSistema;
import br.com.milenio.vendingmachine.repository.UsuarioSistemaRepository;

// Solução: http://stackoverflow.com/questions/16613626/ejb-injection-in-a-custom-userdetailsservice-implementing-spring-security-user
// Aê, Aleluia!! 05-02-2015

// Problema 2: http://www.dextra.com.br/jpahibernate-problemas-com-lazy/

@EJB(name = "usuarioSistemaRepository", beanInterface = UsuarioSistemaRepository.class)
public class UserDetailsSecurity implements UserDetailsService {
	
	private static final String JNDI_USUARIO_SISTEMA_REPOSITORY_NAME = "global/vendingmachine-ear/vendingmachine-domain/UsuarioSistemaRepository";
	
	@Override
	public UserDetails loadUserByUsername(String login) throws UsernameNotFoundException {
		InitialContext initialContext = null;
		UsuarioSistemaRepository usuarioRepository = null;
		
		try {
			initialContext = new InitialContext();
			usuarioRepository = (UsuarioSistemaRepository) initialContext.lookup(JNDI_USUARIO_SISTEMA_REPOSITORY_NAME);
		} catch(Exception e) {
			e.printStackTrace();
		}
		
		UsuarioSistema usuario = usuarioRepository.findUsuarioByLoginEquals(login);
		
		User user = null;
		
		if(usuario != null) {
			user = new UsuarioSistemaSpring(usuario, buscarPermissoes(usuario));
		}
		
		return user;
	}

	private Collection<? extends GrantedAuthority> buscarPermissoes(UsuarioSistema usuario) {
		List<SimpleGrantedAuthority> authorities = new ArrayList<>();
		
		Perfil perfil = usuario.getPerfil();
		for(Permissao permissao : perfil.getPermissoes()) {
			authorities.add(new SimpleGrantedAuthority(permissao.getNome().toUpperCase()));
		}
		
		return authorities;
	}
	
}
