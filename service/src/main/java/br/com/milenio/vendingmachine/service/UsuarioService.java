package br.com.milenio.vendingmachine.service;

import java.util.List;

import javax.ejb.Local;

import br.com.milenio.vendingmachine.domain.model.Perfil;
import br.com.milenio.vendingmachine.domain.model.UsuarioSistema;
import br.com.milenio.vendingmachine.exceptions.ConteudoJaExistenteNoBancoDeDadosException;

@Local
public interface UsuarioService {
	public void cadastrarUsuario(UsuarioSistema novoUsuario, Long perfilId) throws ConteudoJaExistenteNoBancoDeDadosException;
	
	public List<UsuarioSistema> listarTodos();
}
