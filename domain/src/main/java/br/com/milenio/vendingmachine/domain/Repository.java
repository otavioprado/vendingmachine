package br.com.milenio.vendingmachine.domain;

import java.io.Serializable;
import java.util.List;

/**
 * Interface que representa um repositório.
 *
 * @author Otávio Prado
 * @param <T> Interface implemented by the supported entities
 * @param <I> Class of the identifier
 */
public interface Repository<T extends Serializable, I extends Serializable> {

    void persist(T c);

    T merge(T c);

    T findById(I id);

    void remove(T c);

    List<T> getAll();

    T newInstance();

}

