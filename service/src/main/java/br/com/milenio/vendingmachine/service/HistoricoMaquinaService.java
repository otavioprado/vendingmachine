package br.com.milenio.vendingmachine.service;

import java.util.List;

import javax.ejb.Local;

import br.com.milenio.vendingmachine.domain.model.HistoricoMaquina;
import br.com.milenio.vendingmachine.exceptions.CadastroInexistenteException;

@Local
public interface HistoricoMaquinaService {
	public void cadastrar(HistoricoMaquina historicoMaquina);

	public List<HistoricoMaquina> buscarComFiltro(HistoricoMaquina historicoConsParam) throws CadastroInexistenteException;
}
