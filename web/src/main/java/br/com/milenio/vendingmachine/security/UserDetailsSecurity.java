package br.com.milenio.vendingmachine.security;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.ejb.EJB;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import br.com.milenio.vendingmachine.domain.model.Papel;
import br.com.milenio.vendingmachine.domain.model.Usuario;
import br.com.milenio.vendingmachine.repository.UsuarioRepository;

// Solução: http://stackoverflow.com/questions/16613626/ejb-injection-in-a-custom-userdetailsservice-implementing-spring-security-user
// Aê, Aleluia!! 05-02-2015

// Problema 2: http://www.dextra.com.br/jpahibernate-problemas-com-lazy/

@EJB(name = "usuarioRepository", beanInterface = UsuarioRepository.class)
public class UserDetailsSecurity implements UserDetailsService {
	
	private static final String JNDI_USUARIO_REPOSITORY_BEAN_NAME = "global/vendingmachine-ear/vendingmachine-domain/UsuarioRepositoryBean";
	
	@Override
	public UserDetails loadUserByUsername(String login) throws UsernameNotFoundException {
		InitialContext initialContext = null;
		UsuarioRepository usuarioRepository = null;
		
		try {
			initialContext = new InitialContext();
			usuarioRepository = (UsuarioRepository) initialContext.lookup(JNDI_USUARIO_REPOSITORY_BEAN_NAME);
		} catch(Exception e) {
			e.printStackTrace();
		}
		
		Usuario usuario = usuarioRepository.findUsuarioByLoginEquals(login);
		
		User user = null;
		
		if(usuario != null) {
			user = new UsuarioSistemaSpring(usuario, getGrupos(usuario));
		}
		
		return user;
	}

	private Collection<? extends GrantedAuthority> getGrupos(Usuario usuario) {
		List<SimpleGrantedAuthority> authorities = new ArrayList<>();
		
		for(Papel papel : usuario.getPapeis()) {
			authorities.add(new SimpleGrantedAuthority(papel.getNome().toUpperCase()));
		}
		
		return authorities;
	}
	
}
