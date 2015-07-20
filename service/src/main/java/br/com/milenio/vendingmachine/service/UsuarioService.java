package br.com.milenio.vendingmachine.service;

import javax.ejb.Local;

import br.com.milenio.vendingmachine.domain.model.UsuarioSistema;
import br.com.milenio.vendingmachine.exceptions.ConteudoJaExistenteNoBancoDeDadosException;

@Local
public interface UsuarioService {
	public void cadastrarUsuario(UsuarioSistema novoUsuario) throws ConteudoJaExistenteNoBancoDeDadosException;
}
