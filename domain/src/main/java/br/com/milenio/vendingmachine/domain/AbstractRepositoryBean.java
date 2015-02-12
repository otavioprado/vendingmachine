package br.com.milenio.vendingmachine.domain;

import java.io.Serializable;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

/**
 * Implementa��o abstrata de um Repository.
 * <p/>
 * Esta classe n�o deve ser estendida diretamente por classes concretas se o
 * aplicativo sendo desenvolvido utiliza v�rias unidades de persist�ncia.
 * Idealmente, deve haver uma classe abstrata intermedi�ria que � injetada com a
 * unidade de persist�ncia desejada e que implementa o m�todo
 * {@link #getEntityManager()}, de modo que o reposit�rio atual estenderia esta
 * classe intermedi�ria.
 *
 * @author Ot�vio Prado
 * @param <T>
 *            Interface implemented by the entity
 * @param <ID>
 *            Class of the primary key attribute
 */
public abstract class AbstractRepositoryBean<T extends Serializable, I extends Serializable>
		implements Repository<T, I> {

	protected final Class<? extends T> entityConcreteClass;

	/**
	 * Cria uma nova inst�ncia do reposit�rio.
	 *
	 * @param entityConcreteClass
	 *            Classe concreta da entidade. Observe que esta classe est�
	 *            escondida atr�s da interface {@link Repository}
	 */
	protected AbstractRepositoryBean(Class<? extends T> entityConcreteClass) {
		this.entityConcreteClass = entityConcreteClass;
	}

	/**
	 * Obt�m o entity manager atual.
	 * <p/>
	 * Sub-classes devem implementar este m�todo de acordo com suas
	 * necessidades. Isto � especialmente �til quando forem usadas v�rias
	 * unidades de persist�ncia.
	 *
	 * @return EntityManager
	*/
	protected abstract EntityManager getEntityManager();
	
	public void persist(T v) {
		getEntityManager().persist(v);
	}

	public T merge(T v) {
		return getEntityManager().merge(v);
	}

	public T findById(I id) {
		return (T) getEntityManager().find(entityConcreteClass, id);
	}

	public void remove(T c) {
		getEntityManager().remove(c);
	}

	@SuppressWarnings("unchecked")
	public List<T> getAll() {
		Query query = getEntityManager().createQuery(
				"SELECT e FROM " + entityConcreteClass.getName() + " e");
		return (List<T>) query.getResultList();
	}

	/**
	 * Cria uma nova inst�ncia da entidade.
	 */
	public T newInstance() {
		try {
			return entityConcreteClass.newInstance();
		} catch (InstantiationException e) {
			throw new RuntimeException("Error creating entity instance", e);
		} catch (IllegalAccessException e) {
			throw new RuntimeException("Error creating entity instance", e);
		}
	}

}
