package br.com.milenio.vendingmachine.domain;

import java.io.Serializable;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

public abstract class AbstractVendingMachineRepositoryBean<T extends Serializable, ID extends Serializable> extends
        AbstractRepositoryBean<T, ID> {

    @PersistenceContext(unitName = "primaryVendingMachine")
    private EntityManager em;

    protected AbstractVendingMachineRepositoryBean(Class<? extends T> entityConcreteClass) {
        super(entityConcreteClass);
    }

	@Override
	protected EntityManager getEntityManager() {
		return em;
	}
}
