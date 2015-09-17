package br.com.milenio.vendingmachine.service;

import java.util.Date;
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
	public List<Receita> buscarComFiltro(Receita receita, Date dataFim) throws CadastroInexistenteException {
		List<Receita> receitas;
		
		// Se não houver filtros informados, fará a busca de todos os registros
		if((receita.getMaquina().getCodigo() == null || receita.getMaquina().getCodigo().isEmpty()) && 
				receita.getValor() == null && receita.getData() == null && dataFim == null && receita.getNaturezaFinanceira().getId() == null) {
			receitas = receitaRepository.getAll();
			
			if(receitas.isEmpty()) {
				throw new CadastroInexistenteException("Não existe nenhuma receita cadastrada no sistema");
			}
			
			return receitas;
		} else {
			receitas = receitaRepository.buscarComFiltro(receita, dataFim);
			
			if(receitas.isEmpty()) {
				throw new CadastroInexistenteException("Não existe nenhuma receita cadastrada no sistema para o filtro informado.");
			}
			
			return receitas;
		}
	}
}
