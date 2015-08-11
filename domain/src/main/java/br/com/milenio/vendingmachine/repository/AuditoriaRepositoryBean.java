package br.com.milenio.vendingmachine.repository;

import java.util.Date;
import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.TypedQuery;

import br.com.milenio.vendingmachine.domain.AbstractVendingMachineRepositoryBean;
import br.com.milenio.vendingmachine.domain.model.Auditoria;

@Stateless(name = "AuditoriaRepository")
public class AuditoriaRepositoryBean extends AbstractVendingMachineRepositoryBean<Auditoria, Long> 
	implements AuditoriaRepository {

	public AuditoriaRepositoryBean() {
		super(Auditoria.class);
	}
	
	public List<Auditoria> buscarAcoesRealizadas(String login, Date dataAcao) {
		StringBuilder sb = new StringBuilder();
		sb.append("SELECT audit FROM Auditoria audit WHERE ");
		sb.append(" audit.usuario.login = :login AND ");
		sb.append(" audit.dataAcao = :dataAcao");

		TypedQuery<Auditoria> query = getEntityManager().createQuery(sb.toString(), Auditoria.class);

		query.setParameter("login", login);
		query.setParameter("dataAcao", dataAcao);

		List<Auditoria> lstAuditoria = (List<Auditoria>) query.getResultList();
		
		return lstAuditoria;
	}
}
