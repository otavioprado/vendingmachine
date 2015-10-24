package br.com.milenio.vendingmachine.security.spring;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.naming.InitialContext;

import org.springframework.context.ApplicationContext;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.session.SessionInformation;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.session.SessionRegistryImpl;

import br.com.milenio.vendingmachine.domain.model.Perfil;
import br.com.milenio.vendingmachine.domain.model.Permissao;
import br.com.milenio.vendingmachine.domain.model.UsuarioSistema;
import br.com.milenio.vendingmachine.repository.PerfilRepository;
import br.com.milenio.vendingmachine.security.UsuarioSistemaSpring;

public class PermissoesUtils {
	
	private static final String JNDI_PERFIL_REPOSITORY_NAME = "global/vendingmachine-ear/vendingmachine-domain/PerfilRepository";
	
	public void atualizarPermissoesDosUsuariosLogadosComPerfil(Perfil perfil) {
		
		InitialContext initialContext = null;
		PerfilRepository perfilRepository = null;
		
		try {
			initialContext = new InitialContext();
			perfilRepository = (PerfilRepository) initialContext.lookup(JNDI_PERFIL_REPOSITORY_NAME);
		} catch(Exception e) {
			e.printStackTrace();
		}
		
		ApplicationContext applicationContext = SpringUtils.getApplicationContext();
		SessionRegistry sessionRegistry = (SessionRegistry) applicationContext.getBean("sessionRegistry", SessionRegistryImpl.class);

		List<Object> loggedUsers = sessionRegistry.getAllPrincipals();
		for (Object principal : loggedUsers) {
		    if(principal instanceof UsuarioSistemaSpring) {
		    	UsuarioSistemaSpring loggedUser = (UsuarioSistemaSpring) principal;
		    	
		    	if(perfil.getId() == loggedUser.getUsuario().getPerfil().getId()) {
			    	// Carregar o perfil novamente através do ID para carregar as permissões atuais que estão no banco de dados
			    	Perfil perfilAtualizado = perfilRepository.findById(loggedUser.getUsuario().getPerfil().getId());

					loggedUser.getUsuario().setPerfil(perfilAtualizado);
					
					loggedUser.getAuthorities();
					UsuarioSistemaSpring usuarioAtualizado = new UsuarioSistemaSpring(loggedUser.getUsuario(), buscarPermissoes(loggedUser.getUsuario()));
			    	
			    	List<SessionInformation> sessionsInfo = sessionRegistry.getAllSessions(principal, false);
		            if(null != sessionsInfo && sessionsInfo.size() > 0) {
		                for (SessionInformation sessionInformation : sessionsInfo) {
		                	sessionInformation.expireNow();
		                    sessionRegistry.removeSessionInformation(sessionInformation.getSessionId());
		                    // Registra a nova sessão com o mesmo ID, porém com as permissões atualizadas
                    		sessionRegistry.registerNewSession(sessionInformation.getSessionId(), usuarioAtualizado);
		                }
		            }
		    	}
		    }
		}
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
