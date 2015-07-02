package br.com.milenio.vendingmachine.service;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import br.com.milenio.vendingmachine.domain.model.UsuarioSistema;
import br.com.milenio.vendingmachine.repository.UsuarioSistemaRepository;

@Stateless
public class UsuarioServiceBean implements UsuarioService {

	@EJB
	UsuarioSistemaRepository usuarioSistemaRepository;
	
	private Logger logger = LogManager.getLogger(UsuarioServiceBean.class);
	
	@Override
	public boolean cadastrarUsuario(UsuarioSistema usuario) {
		try{
			usuarioSistemaRepository.persist(usuario);
		} catch(Exception e) {
			
		}
		
		return true;
	}
	
}
