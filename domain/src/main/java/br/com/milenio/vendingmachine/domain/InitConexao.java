package br.com.milenio.vendingmachine.domain;

import java.util.Date;

import javax.annotation.PostConstruct;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import br.com.milenio.vendingmachine.domain.model.UsuarioSistema;

@Singleton
@Startup
public class InitConexao {
	
	@PersistenceContext(unitName = "primaryVendingMachine")
    private EntityManager em;
	
	@PostConstruct
	public void init() {
		UsuarioSistema us = new UsuarioSistema();
		
		us.setLogin("admin");
		us.setSenhaAplicacao("admin");
		us.setDataCadastro(new Date());
		us.setEmail("otavio_lipe@hotmail.com");
		
		em.persist(us);
		
		System.out.println(us.getEmail() + " persistido no banco de dados com sucesso!");
	}

}
