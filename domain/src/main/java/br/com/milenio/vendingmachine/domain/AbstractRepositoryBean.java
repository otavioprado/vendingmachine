package br.com.milenio.vendingmachine.domain;

import java.io.Serializable;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

/**
 * Implementação abstrata de um Repository.
 * <p/>
 * Esta classe não deve ser estendida diretamente por classes concretas se o
 * aplicativo sendo desenvolvido utiliza várias unidades de persistência.
 * Idealmente, deve haver uma classe abstrata intermediária que é injetada com a
 * unidade de persistência desejada e que implementa o método
 * {@link #getEntityManager()}, de modo que o repositório atual estenderia esta
 * classe intermediária.
 *
 * @author Otávio Prado
 * @param <T>
 *            Interface implemented by the entity
 * @param <ID>
 *            Class of the primary key attribute
 */
public abstract class AbstractRepositoryBean<T extends Serializable, I extends Serializable>
		implements Repository<T, I> {

	protected final Class<? extends T> entityConcreteClass;

	/**
	 * Cria uma nova instância do repositório.
	 *
	 * @param entityConcreteClass
	 *            Classe concreta da entidade. Observe que esta classe está
	 *            escondida atrás da interface {@link Repository}
	 */
	protected AbstractRepositoryBean(Class<? extends T> entityConcreteClass) {
		this.entityConcreteClass = entityConcreteClass;
	}

	/**
	 * Obtém o entity manager atual.
	 * <p/>
	 * Sub-classes devem implementar este método de acordo com suas
	 * necessidades. Isto é especialmente útil quando forem usadas várias
	 * unidades de persistência.
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
	 * Cria uma nova instância da entidade.
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
