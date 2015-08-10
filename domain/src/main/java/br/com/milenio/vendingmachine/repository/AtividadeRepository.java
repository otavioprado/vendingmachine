package br.com.milenio.vendingmachine.repository;

import java.util.List;

import javax.ejb.Local;

import br.com.milenio.vendingmachine.domain.Repository;
import br.com.milenio.vendingmachine.domain.model.Atividade;

@Local
public interface AtividadeRepository extends Repository<Atividade, Long> {
	public List<Atividade> findAtividadesUsuarioByLogin(String login);
}
