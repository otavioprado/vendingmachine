package br.com.milenio.vendingmachine.repository;

import java.util.Date;
import java.util.List;

import javax.ejb.Local;

import br.com.milenio.vendingmachine.domain.Repository;
import br.com.milenio.vendingmachine.domain.model.Atividade;
import br.com.milenio.vendingmachine.domain.model.Perfil;

@Local
public interface AtividadeRepository extends Repository<Atividade, Long> {
	public List<Atividade> findAtividadesUsuarioByLoginDate(String login, Date data);

	public List<Atividade> buscarAtividadesComFiltro(String login, Perfil perfil, Date data);
}
