package br.com.milenio.vendingmachine.service;

import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import br.com.milenio.vendingmachine.domain.model.Perfil;
import br.com.milenio.vendingmachine.domain.model.UsuarioSistema;
import br.com.milenio.vendingmachine.exceptions.ConteudoJaExistenteNoBancoDeDadosException;
import br.com.milenio.vendingmachine.repository.PerfilRepository;
import br.com.milenio.vendingmachine.repository.UsuarioSistemaRepository;

@Stateless
public class UsuarioServiceBean implements UsuarioService {

	@EJB
	UsuarioSistemaRepository usuarioSistemaRepository;
	
	@EJB
	PerfilRepository perfilRepository;
	
	private Logger logger = LogManager.getLogger(UsuarioServiceBean.class);
	
	@Override
	public void cadastrarUsuario(UsuarioSistema novoUsuario, Long perfilId) throws ConteudoJaExistenteNoBancoDeDadosException {
		// Verifica se j� existe um usu�rio com o mesmo login ou email cadastrado no banco de dados do sistema
		UsuarioSistema usuario = usuarioSistemaRepository.findUsuarioByLoginAndEmail(novoUsuario);
		
		if(usuario != null) {
			String login = usuario.getLogin();
			String email = usuario.getEmail();
			
			if(login.equals(novoUsuario.getLogin())) {
				logger.info("J� existe um usu�rio com login " + usuario.getLogin() + " cadastrado no banco de dados.");
				throw new ConteudoJaExistenteNoBancoDeDadosException("O login " + usuario.getLogin() + " j� existe cadastrado no sistema.");
			} else if(email.equals(novoUsuario.getEmail())) {
				logger.info("J� existe um usu�rio com email " + usuario.getEmail() + " cadastrado no banco de dados.");
				throw new ConteudoJaExistenteNoBancoDeDadosException("O email " + usuario.getEmail() + " j� existe cadastrado no sistema.");
			}
		}
		
		try{
			Perfil perfil = perfilRepository.findById(perfilId);
			novoUsuario.setPerfil(perfil);
			usuarioSistemaRepository.persist(novoUsuario);
		} catch(Exception e) {
			// Erro desconhecido ao tentar realizar a persist�ncia dos dados no banco de dados
			logger.error("Erro ao tentar gravar o usu�rio " + novoUsuario.getNome() + " no banco de dados");
		}
		
		logger.info("Cadastro do usu�rio " + novoUsuario.getLogin() + " salvo com sucesso no banco de dados.");
	}
	
	public List<UsuarioSistema> listarTodos() {
		return usuarioSistemaRepository.getAll();
	}
	
}
