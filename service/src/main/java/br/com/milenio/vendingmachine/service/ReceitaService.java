package br.com.milenio.vendingmachine.service;

import java.util.List;

import javax.ejb.Local;

import br.com.milenio.vendingmachine.domain.model.Receita;
import br.com.milenio.vendingmachine.exceptions.CadastroInexistenteException;

@Local
public interface ReceitaService {
	public void cadastrar(Receita receita);

	public List<Receita> buscarComFiltro(Receita receita) throws CadastroInexistenteException;
}
