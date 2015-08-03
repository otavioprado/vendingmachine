package br.com.milenio.vendingmachine.security.spring;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.session.SessionInformation;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;

@Component
public class Teste {
	
	@Autowired
	@Qualifier("sessionRegistry")
	private SessionRegistry sessionRegistry;
	
	public Teste() {
	}
	
	public SessionRegistry getSessionRegistry() {
		return sessionRegistry;
	}

	public void setSessionRegistry(SessionRegistry sessionRegistry) {
		this.sessionRegistry = sessionRegistry;
	}

	@Autowired
	private ApplicationContext ap;
	
	public ApplicationContext getAp() {
		return ap;
	}

	public void setAp(ApplicationContext ap) {
		this.ap = ap;
	}

	public void invalidar() {
		
		SessionRegistry sr = (SessionRegistry) ap.getBean("sessionRegistry");
		
		SecurityContextHolder.getContext().getAuthentication();
		
		List<Object> loggedUsers = sessionRegistry.getAllPrincipals();
		for (Object principal : loggedUsers) {
		    if(principal instanceof User) {
		        final User loggedUser = (User) principal;
		        if("otavio".equals(loggedUser.getUsername())) {
		            List<SessionInformation> sessionsInfo = sessionRegistry.getAllSessions(principal, false);
		            if(null != sessionsInfo && sessionsInfo.size() > 0) {
		                for (SessionInformation sessionInformation : sessionsInfo) {
		                    //LOGGER.info("Exprire now :" + sessionInformation.getSessionId());
		                    sessionInformation.expireNow();
		                    sessionRegistry.removeSessionInformation(sessionInformation.getSessionId());
		                    // User is not forced to re-logging
		                }
		            }
		        }
		    }
		}
	}
}
