package br.com.milenio.vendingmachine.repository;

import java.util.List;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import br.com.milenio.vendingmachine.domain.VendingMachineRepository;
import br.com.milenio.vendingmachine.domain.model.Usuario;

@Stateless
public class UsuarioRepositoryBean extends VendingMachineRepository<Usuario, Long> 
	implements UsuarioRepository {

	public UsuarioRepositoryBean() {
		super(Usuario.class);
	}
	
	public List<Usuario> findUsuarioSistemaById(long idUsuario) {
		TypedQuery<Usuario> query = getEntityManager().createNamedQuery("findByIdUsuarioSistema", Usuario.class);
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
	public Usuario findUsuarioByLoginEquals(String login) {
		EntityManager em = getEntityManager();
		
		TypedQuery<Usuario> query = em.createQuery("SELECT user FROM Usuario user JOIN FETCH user.papeis WHERE user.login = :login", Usuario.class);
		query.setParameter("login", login);
		
		Usuario usuario = (Usuario) query.getSingleResult();
		
		return usuario;
	}
	
}
