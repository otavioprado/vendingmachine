package br.com.milenio.vendingmachine.service;

import java.util.List;

import javax.ejb.Local;

import br.com.milenio.vendingmachine.domain.model.Cliente;
import br.com.milenio.vendingmachine.exceptions.CadastroInexistenteException;
import br.com.milenio.vendingmachine.exceptions.ConteudoJaExistenteNoBancoDeDadosException;

@Local
public interface ClienteService {
	public void cadastrar(Cliente cliente) throws ConteudoJaExistenteNoBancoDeDadosException;

	public List<Cliente> buscarClientesComFiltro(Cliente cliente) throws CadastroInexistenteException;

	public boolean bloquearUsuario(Long id, String motivoBloqueio);

	public boolean desbloquearUsuario(Long id);

	public Cliente findById(Long id);

	public void editar(Cliente cliente);
}
