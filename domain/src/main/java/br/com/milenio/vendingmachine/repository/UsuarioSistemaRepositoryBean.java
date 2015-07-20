package br.com.milenio.vendingmachine.repository;

import java.util.List;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import br.com.milenio.vendingmachine.domain.AbstractVendingMachineRepositoryBean;
import br.com.milenio.vendingmachine.domain.model.UsuarioSistema;

@Stateless(name = "UsuarioSistemaRepository")
public class UsuarioSistemaRepositoryBean extends AbstractVendingMachineRepositoryBean<UsuarioSistema, Long> 
	implements UsuarioSistemaRepository {

	public UsuarioSistemaRepositoryBean() {
		super(UsuarioSistema.class);
	}
	
	public List<UsuarioSistema> findUsuarioSistemaById(long idUsuario) {
		TypedQuery<UsuarioSistema> query = getEntityManager().createNamedQuery("findByIdUsuarioSistema", UsuarioSistema.class);
		query.setParameter("idUsuarioSistema", idUsuario);
		return query.getResultList();
	}

	@Override
	public List<Long> findClientesComEmail() {
		TypedQuery<Long> query = getEntityManager().createNamedQuery("findUsuariosComEmail", Long.class);
		
		return query.getResultList();
	}

	@Override
	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	public UsuarioSistema findUsuarioByLoginEquals(String login) {
		EntityManager em = getEntityManager();
		
		TypedQuery<UsuarioSistema> query = em.createQuery("SELECT user FROM UsuarioSistema user JOIN FETCH user.perfils WHERE user.login = :login", UsuarioSistema.class);
		query.setParameter("login", login);
		
		UsuarioSistema usuario = (UsuarioSistema) query.getSingleResult();
		
		return usuario;
	}
	
	@Override
	public UsuarioSistema findUsuarioByLoginAndEmail(UsuarioSistema usuario) {
		StringBuilder sb = new StringBuilder();
		sb.append("SELECT user FROM UsuarioSistema user WHERE ");
		sb.append(" user.login = :login OR");
		sb.append(" user.email = :email");

		TypedQuery<UsuarioSistema> query = getEntityManager().createQuery(sb.toString(), UsuarioSistema.class);

		query.setParameter("login", usuario.getLogin());
		query.setParameter("email", usuario.getEmail());

		List<UsuarioSistema> usuarioCadastrado = (List<UsuarioSistema>) query.getResultList();
		
		if(!usuarioCadastrado.isEmpty()) {
			return usuarioCadastrado.get(0);
		}
		
		return null;
	}
	
}
