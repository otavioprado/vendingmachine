package br.com.milenio.vendingmachine.repository;

import java.util.Date;
import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.TypedQuery;

import com.uaihebert.uaicriteria.UaiCriteria;
import com.uaihebert.uaicriteria.UaiCriteriaFactory;

import br.com.milenio.vendingmachine.domain.AbstractVendingMachineRepositoryBean;
import br.com.milenio.vendingmachine.domain.model.Atividade;
import br.com.milenio.vendingmachine.domain.model.Perfil;
import br.com.milenio.vendingmachine.domain.model.UsuarioSistema;

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

	@Override
	public List<Atividade> buscarAtividadesComFiltro(String login, Perfil perfil, Date data) {
		
		UaiCriteria<Atividade> uaiCriteria = UaiCriteriaFactory.createQueryCriteria(getEntityManager(), Atividade.class);
		
		if(login != null && !login.isEmpty()) {
			uaiCriteria.innerJoin("usuario");
			uaiCriteria.andStringLike("usuario.login", "%" + login + "%");
		}
		
		if(perfil != null) {
			uaiCriteria.innerJoin("usuario");
			uaiCriteria.andEquals("usuario.perfil", perfil);
		}
		
		if(data != null) {
			uaiCriteria.andEquals("dataAgendamento", data);
		}
		
		List<Atividade> lstAtividades = (List<Atividade>) uaiCriteria.getResultList();
		
		return lstAtividades;
	}
	
	
}
