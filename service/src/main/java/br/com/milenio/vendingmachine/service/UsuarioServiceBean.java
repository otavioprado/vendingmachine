package br.com.milenio.vendingmachine.service;

import java.util.Date;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.EJBException;
import javax.ejb.Stateless;
import javax.persistence.NoResultException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import br.com.milenio.vendingmachine.domain.model.Perfil;
import br.com.milenio.vendingmachine.domain.model.UsuarioSistema;
import br.com.milenio.vendingmachine.exceptions.CadastroInexistenteException;
import br.com.milenio.vendingmachine.exceptions.ConteudoJaExistenteNoBancoDeDadosException;
import br.com.milenio.vendingmachine.exceptions.UsuarioBloqueadoNoSistemaException;
import br.com.milenio.vendingmachine.exceptions.UsuarioInexistenteNoSistemaException;
import br.com.milenio.vendingmachine.repository.ConfiguracaoSistemaRepository;
import br.com.milenio.vendingmachine.repository.PerfilRepository;
import br.com.milenio.vendingmachine.repository.UsuarioSistemaRepository;
import br.com.milenio.vendingmachine.utils.MD5Util;

@Stateless
public class UsuarioServiceBean implements UsuarioService {

	@EJB
	UsuarioSistemaRepository usuarioSistemaRepository;
	
	@EJB
	ConfiguracaoSistemaRepository configuracaoSistemaRepository;
	
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
			
			if(login.equalsIgnoreCase(novoUsuario.getLogin())) {
				logger.info("J� existe um usu�rio com login " + usuario.getLogin() + " cadastrado no banco de dados.");
				throw new ConteudoJaExistenteNoBancoDeDadosException("O login " + usuario.getLogin() + " j� existe cadastrado no sistema.");
			} else if(email.equalsIgnoreCase(novoUsuario.getEmail())) {
				logger.info("J� existe um usu�rio com email " + usuario.getEmail() + " cadastrado no banco de dados.");
				throw new ConteudoJaExistenteNoBancoDeDadosException("O email " + usuario.getEmail() + " j� existe cadastrado no sistema.");
			}
		}
		
		try{
			Perfil perfil = perfilRepository.findById(perfilId);
			novoUsuario.setPerfil(perfil);
			
			// Criptografa a senha digitada antes de persistir no banco de dados
			novoUsuario.setSenhaAplicacao(MD5Util.criptografar(novoUsuario.getSenhaAplicacao()));
			
			usuarioSistemaRepository.persist(novoUsuario);
		} catch(Exception e) {
			// Erro desconhecido ao tentar realizar a persist�ncia dos dados no banco de dados
			logger.error("Erro ao tentar gravar o usu�rio " + novoUsuario.getNome() + " no banco de dados");
		}
		
		logger.info("Cadastro do usu�rio " + novoUsuario.getLogin() + " salvo com sucesso no banco de dados.");
	}
	
	public void editarUsuario(UsuarioSistema usuarioEditado) throws ConteudoJaExistenteNoBancoDeDadosException {
		// Carrega um objeto com os dados atuais do usu�rio sendo editado
		UsuarioSistema usuarioAtual = usuarioSistemaRepository.findById(usuarioEditado.getId());
		
		UsuarioSistema usuario = null;
		
		if(!usuarioAtual.getLogin().equalsIgnoreCase(usuarioEditado.getLogin())) {
			// Se houve mudan�a no login, � necess�rio validar se o novo login j� n�o existe no sistema
			
			// Verifica se j� existe um usu�rio com o mesmo login cadastrado no banco de dados do sistema
			usuario = usuarioSistemaRepository.findUsuarioByLogin(usuarioEditado.getLogin());
			
		} else if(!usuarioAtual.getEmail().equalsIgnoreCase(usuarioEditado.getEmail())) {
			// Se houve mudan�a no email, � necess�rio validar se o novo email j� n�o existe no sistema
			
			// Verifica se j� existe um usu�rio com o mesmo e-mail cadastrado no banco de dados do sistema
			usuario = usuarioSistemaRepository.findUsuarioByEmail(usuarioEditado.getEmail());
		}
			
		if(usuario != null) {
			String login = usuario.getLogin();
			String email = usuario.getEmail();
			
			if(login.equalsIgnoreCase(usuarioEditado.getLogin())) {
				logger.info("J� existe um usu�rio com login " + usuario.getLogin() + " cadastrado no banco de dados.");
				throw new ConteudoJaExistenteNoBancoDeDadosException("O login " + usuario.getLogin() + " j� existe cadastrado no sistema.");
			} else if(email.equalsIgnoreCase(usuarioEditado.getEmail())) {
				logger.info("J� existe um usu�rio com email " + usuario.getEmail() + " cadastrado no banco de dados.");
				throw new ConteudoJaExistenteNoBancoDeDadosException("O email " + usuario.getEmail() + " j� existe cadastrado no sistema.");
			}
		}
			
		try {
			Perfil perfil = perfilRepository.findById(usuarioEditado.getPerfil().getId());
			usuarioEditado.setPerfil(perfil);
			
			usuarioSistemaRepository.merge(usuarioEditado);
		} catch(Exception e) {
			// Erro desconhecido ao tentar realizar a persist�ncia dos dados no banco de dados
			logger.error("Erro ao tentar alterar o cadastro do usu�rio " + usuarioEditado.getNome() + " no banco de dados");
		}
		
		logger.info("Cadastro do usu�rio " + usuarioEditado.getLogin() + " alterado com sucesso no banco de dados.");
	}
	
	public List<UsuarioSistema> listarTodos() {
		return usuarioSistemaRepository.getAll();
	}

	@Override
	public List<UsuarioSistema> buscarUsuariosComFiltro(String login, Boolean status, Long perfilId) throws CadastroInexistenteException {
		
		List<UsuarioSistema> usuarios;
		
		// Se n�o houver filtros informados, far� a busca de todos os registros
		if(((login == null || login.isEmpty()) && status == null && perfilId == null)) {
			usuarios = usuarioSistemaRepository.getAll();
			
			if(usuarios.isEmpty()) {
				throw new CadastroInexistenteException("N�o existem usu�rios cadastrados no sistema");
			}
			
			return usuarios;
		} else {
			usuarios = usuarioSistemaRepository.buscarUsuariosComFiltro(login, status, perfilId);
			
			if(usuarios.isEmpty()) {
				throw new CadastroInexistenteException("N�o existe nenhum cadastro de usu�rio para o filtro informado.");
			}
			
			return usuarios;
		}
	}
	
	public UsuarioSistema findById(Long editUserId) {
		return usuarioSistemaRepository.findById(editUserId);
	}
	
	public UsuarioSistema findByLogin(String login) {
		return usuarioSistemaRepository.findUsuarioByLogin(login);
	}

	@Override
	public boolean desbloquearUsuario(Long id) {
		UsuarioSistema usuario = usuarioSistemaRepository.findById(id);
		usuario.setIndAtivo(true);
		usuario.setQtdTentativasAcessoInvalido(0);
		usuarioSistemaRepository.persist(usuario);
		
		return true;
	}

	@Override
	public boolean bloquearUsuario(Long id, String motivoBloqueio) {
		UsuarioSistema usuario = usuarioSistemaRepository.findById(id);
		usuario.setIndAtivo(false);
		usuario.setMotivoBloqueio(motivoBloqueio);
		usuarioSistemaRepository.persist(usuario);
		
		return true;
	}
	
	@Override
	public UsuarioSistema validarUsuarioAtivoPeloLoginSenha(String login, String senha) throws UsuarioBloqueadoNoSistemaException, UsuarioInexistenteNoSistemaException {
		UsuarioSistema usuario = null;
		try {
			usuario = usuarioSistemaRepository.findUsuarioByLoginEquals(login);
			
			// 1�  Se o usu�rio j� estiver bloqueado nem prossegue com as demais valida��es
			if(!usuario.getIndAtivo()) {
				throw new UsuarioBloqueadoNoSistemaException(usuario.getMotivoBloqueio());
			}
			
			// Valida a senha digitada. Se errou a senha, incrementa a quantidade de tentativas com senha errada
			if(!validarSenhaUsuario(usuario, senha)) {
				usuario.setQtdTentativasAcessoInvalido(usuario.getQtdTentativasAcessoInvalido() + 1);
				
				int qtdMaxTentativasComSenhaInvalida  = Integer.parseInt(configuracaoSistemaRepository.getValorConfiguracaoPeloNome("QTD_MAX_TENTATIVAS_ACESSO_COM_SENHA_INVALIDA"));
				
				if(usuario.getQtdTentativasAcessoInvalido() >= qtdMaxTentativasComSenhaInvalida) {
					usuario.setDataBloqueio(new Date());
					usuario.setIndAtivo(false);
					usuario.setMotivoBloqueio("Quantidade m�xima de tentativas de acesso com senha inv�lida excedido");
					usuarioSistemaRepository.merge(usuario);
					
					throw new UsuarioBloqueadoNoSistemaException("Quantidade m�xima de tentativas de acesso com senha inv�lida excedido");
				} else {
					usuarioSistemaRepository.merge(usuario);
				}
			}
		} catch(EJBException e) {
			if(e.getCause() instanceof NoResultException) {
				throw new UsuarioInexistenteNoSistemaException("Usu�rio " + login + " n�o existe cadastrado no sistema.");
			}
		}
		return usuario;
	}
	
	private boolean validarSenhaUsuario(UsuarioSistema usuario, String senhaDigitada) {
		String senhaDigitadaCriptografada = MD5Util.criptografar(senhaDigitada);
		
		return senhaDigitadaCriptografada.equals(usuario.getSenhaAplicacao());
	}
	
}
