package br.com.milenio.vendingmachine.service;

import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import br.com.milenio.vendingmachine.domain.model.Receita;
import br.com.milenio.vendingmachine.exceptions.CadastroInexistenteException;
import br.com.milenio.vendingmachine.repository.ReceitaRepository;

@Stateless
public class ReceitaServiceBean implements ReceitaService {

	@EJB
	ReceitaRepository receitaRepository;
	
	@Override
	public void cadastrar(Receita receita) {
		receitaRepository.persist(receita);
	}

	@Override
	public List<Receita> buscarComFiltro(Receita receita) throws CadastroInexistenteException {
		return null;
	}
}
