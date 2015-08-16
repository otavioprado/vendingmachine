package br.com.milenio.vendingmachine.repository;

import java.util.Date;
import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.TypedQuery;

import br.com.milenio.vendingmachine.domain.AbstractVendingMachineRepositoryBean;
import br.com.milenio.vendingmachine.domain.model.Atividade;

@Stateless(name = "AtividadeRepository")
public class AtividadeRepositoryBean extends AbstractVendingMachineRepositoryBean<Atividade, Long> 
	implements AtividadeRepository {

	public AtividadeRepositoryBean() {
		super(Atividade.class);
	}
	
	public List<Atividade> findAtividadesUsuarioByLoginDate(String login, Date data) {
		StringBuilder sb = new StringBuilder();
		sb.append("SELECT atv FROM Atividade atv WHERE ");
		sb.append(" atv.usuario.login = :login AND");
		sb.append(" atv.dataAgendamento = :data");


		TypedQuery<Atividade> query = getEntityManager().createQuery(sb.toString(), Atividade.class);

		query.setParameter("login", login);
		query.setParameter("data", data);

		List<Atividade> lstAtividades = (List<Atividade>) query.getResultList();
		
		return lstAtividades;
	}
	
}
