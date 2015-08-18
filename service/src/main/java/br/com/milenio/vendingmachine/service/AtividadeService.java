package br.com.milenio.vendingmachine.service;

import java.util.Date;
import java.util.List;

import javax.ejb.Local;

import br.com.milenio.vendingmachine.domain.model.Atividade;
import br.com.milenio.vendingmachine.exceptions.CadastroInexistenteException;

@Local
public interface AtividadeService {
	public void cadastrarAtividade(Atividade atividade) throws CadastroInexistenteException;

	public List<Atividade> buscarAtividadesAgendadas(String login, Date data) throws CadastroInexistenteException;

	public List<Atividade> buscarAtividadesComFiltro(String login, Long perfilId, Date data);
}
