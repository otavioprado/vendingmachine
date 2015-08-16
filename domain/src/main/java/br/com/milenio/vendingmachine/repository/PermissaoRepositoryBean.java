package br.com.milenio.vendingmachine.repository;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import br.com.milenio.vendingmachine.domain.AbstractVendingMachineRepositoryBean;
import br.com.milenio.vendingmachine.domain.model.Permissao;

@Stateless(name = "PermissaoRepository")
public class PermissaoRepositoryBean extends AbstractVendingMachineRepositoryBean<Permissao, Long> 
	implements PermissaoRepository {

	public PermissaoRepositoryBean() {
		super(Permissao.class);
	}

	@Override
	public List<Permissao> getPermissoesVisiveis() {
		EntityManager em = getEntityManager();
		
		TypedQuery<Permissao> query = em.createQuery("SELECT perm FROM Permissao perm WHERE perm.indAtribAnyPerfil = :indAtribAnyPerfil", Permissao.class);
		query.setParameter("indAtribAnyPerfil", Boolean.valueOf(true));
		
		return query.getResultList();
	}

	@Override
	public List<Permissao> getPermissoesAdministrativasRestritas() {
		EntityManager em = getEntityManager();
		
		TypedQuery<Permissao> query = em.createQuery("SELECT perm FROM Permissao perm WHERE perm.indAtribAnyPerfil = :indAtribAnyPerfil", Permissao.class);
		query.setParameter("indAtribAnyPerfil", Boolean.valueOf(false));
		
		return query.getResultList();
	}
}
