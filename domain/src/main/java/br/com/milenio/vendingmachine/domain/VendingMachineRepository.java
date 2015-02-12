package br.com.milenio.vendingmachine.domain;

import java.io.Serializable;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

public class VendingMachineRepository<T extends Serializable, ID extends Serializable> extends
        AbstractRepositoryBean<T, ID> {

    @PersistenceContext(unitName = "primaryVendingMachine")
    private EntityManager em;

    protected VendingMachineRepository(Class<? extends T> entityConcreteClass) {
        super(entityConcreteClass);
    }

	@Override
	protected EntityManager getEntityManager() {
		return em;
	}
}
