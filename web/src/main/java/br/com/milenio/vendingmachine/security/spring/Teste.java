package br.com.milenio.vendingmachine.security.spring;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.context.ApplicationContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.session.SessionInformation;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;

@Component("teste")
public class Teste {
	
	public void invalidar() {
		
		ApplicationContext applicationContext = SpringUtils.getApplicationContext();
		SessionRegistry sessionRegistry = (SessionRegistry) applicationContext.getBean("sessionRegistry", SessionRegistryImpl.class);
		
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
