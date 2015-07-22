package br.com.milenio.vendingmachine.service;

import java.util.List;

import javax.ejb.Local;

import br.com.milenio.vendingmachine.domain.model.UsuarioSistema;
import br.com.milenio.vendingmachine.exceptions.ConteudoJaExistenteNoBancoDeDadosException;

@Local
public interface UsuarioService {
	public void cadastrarUsuario(UsuarioSistema novoUsuario, Long perfilId) throws ConteudoJaExistenteNoBancoDeDadosException;
	
	public List<UsuarioSistema> listarTodos();

	public List<UsuarioSistema> buscarUsuariosComFiltro(String login, Boolean status, Long perfilId);

	public UsuarioSistema findById(Long editUserId);
}
