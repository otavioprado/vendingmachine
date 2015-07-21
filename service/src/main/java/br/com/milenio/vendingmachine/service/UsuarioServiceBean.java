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
		// Verifica se já existe um usuário com o mesmo login ou email cadastrado no banco de dados do sistema
		UsuarioSistema usuario = usuarioSistemaRepository.findUsuarioByLoginAndEmail(novoUsuario);
		
		if(usuario != null) {
			String login = usuario.getLogin();
			String email = usuario.getEmail();
			
			if(login.equals(novoUsuario.getLogin())) {
				logger.info("Já existe um usuário com login " + usuario.getLogin() + " cadastrado no banco de dados.");
				throw new ConteudoJaExistenteNoBancoDeDadosException("O login " + usuario.getLogin() + " já existe cadastrado no sistema.");
			} else if(email.equals(novoUsuario.getEmail())) {
				logger.info("Já existe um usuário com email " + usuario.getEmail() + " cadastrado no banco de dados.");
				throw new ConteudoJaExistenteNoBancoDeDadosException("O email " + usuario.getEmail() + " já existe cadastrado no sistema.");
			}
		}
		
		try{
			Perfil perfil = perfilRepository.findById(perfilId);
			novoUsuario.setPerfil(perfil);
			usuarioSistemaRepository.persist(novoUsuario);
		} catch(Exception e) {
			// Erro desconhecido ao tentar realizar a persistência dos dados no banco de dados
			logger.error("Erro ao tentar gravar o usuário " + novoUsuario.getNome() + " no banco de dados");
		}
		
		logger.info("Cadastro do usuário " + novoUsuario.getLogin() + " salvo com sucesso no banco de dados.");
	}
	
	public List<UsuarioSistema> listarTodos() {
		return usuarioSistemaRepository.getAll();
	}
	
}
