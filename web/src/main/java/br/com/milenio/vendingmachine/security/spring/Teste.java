package br.com.milenio.vendingmachine.security.spring;

import java.util.List;

import javax.naming.InitialContext;

import org.springframework.context.ApplicationContext;
import org.springframework.security.core.session.SessionInformation;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.session.SessionRegistryImpl;

import br.com.milenio.vendingmachine.domain.model.Perfil;
import br.com.milenio.vendingmachine.domain.model.Permissao;
import br.com.milenio.vendingmachine.repository.PerfilRepository;
import br.com.milenio.vendingmachine.security.UsuarioSistemaSpring;

public class Teste {
	
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
			    	// Carregar o perfil novamento pelo ID para carregar as permissões do banco de dados
			    	Perfil perfilAtualizado = perfilRepository.findById(loggedUser.getUsuario().getPerfil().getId());
			    	
			    	List<Permissao> permissoes = perfilAtualizado.getPermissoes();
					loggedUser.getUsuario().setPerfil(perfilAtualizado);
			    	
			    	List<SessionInformation> sessionsInfo = sessionRegistry.getAllSessions(principal, false);
		            if(null != sessionsInfo && sessionsInfo.size() > 0) {
		                for (SessionInformation sessionInformation : sessionsInfo) {
		                	sessionInformation.expireNow();
		                    sessionRegistry.removeSessionInformation(sessionInformation.getSessionId());
		                    // Registra a nova sessão com o mesmo ID, porém com as permissões atualiadas
                    		sessionRegistry.registerNewSession(sessionInformation.getSessionId(), loggedUser);
		                }
		            }
		    	}
		    }
		}
	}
}
