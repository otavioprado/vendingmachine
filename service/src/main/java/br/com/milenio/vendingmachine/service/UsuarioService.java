package br.com.milenio.vendingmachine.service;

import java.util.List;

import javax.ejb.Local;

import br.com.milenio.vendingmachine.domain.model.UsuarioSistema;
import br.com.milenio.vendingmachine.exceptions.CadastroInexistenteException;
import br.com.milenio.vendingmachine.exceptions.ConteudoJaExistenteNoBancoDeDadosException;
import br.com.milenio.vendingmachine.exceptions.InconsistenciaException;
import br.com.milenio.vendingmachine.exceptions.UsuarioBloqueadoNoSistemaException;
import br.com.milenio.vendingmachine.exceptions.UsuarioInexistenteNoSistemaException;

@Local
public interface UsuarioService {
	public void cadastrarUsuario(UsuarioSistema novoUsuario, Long perfilId) throws ConteudoJaExistenteNoBancoDeDadosException;
	
	public List<UsuarioSistema> listarTodos();

	public List<UsuarioSistema> buscarUsuariosComFiltro(String login, Boolean status, Long perfilId) throws CadastroInexistenteException;

	public UsuarioSistema findById(Long editUserId);

	public boolean desbloquearUsuario(Long id);

	public boolean bloquearUsuario(Long id, String motivoBloqueio);
	
	public UsuarioSistema validarUsuarioAtivoPeloLoginSenha(String login, String senha) throws UsuarioBloqueadoNoSistemaException, UsuarioInexistenteNoSistemaException;
	
	public void editarUsuario(UsuarioSistema usuarioEditado) throws ConteudoJaExistenteNoBancoDeDadosException;
	
	public UsuarioSistema findByLogin(String login);

	public void editarCadastroPessoal(UsuarioSistema usuario) throws InconsistenciaException, ConteudoJaExistenteNoBancoDeDadosException;

	public void alterarSenha(UsuarioSistema usuario) throws InconsistenciaException;
}
