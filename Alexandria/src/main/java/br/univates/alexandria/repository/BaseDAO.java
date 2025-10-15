package br.univates.alexandria.repository;

import br.univates.alexandria.exceptions.PersistenciaException;
import java.util.ArrayList;

/**
 * Interface que implementa alguns métodos que todos os daos devem possuir
 * @author mateus.brambilla
 * @param <T> - objeto que será feito persistência
 * @param <K> - objeto responsável pela busca
 */
public interface BaseDAO<T, K> {
    public ArrayList<T> readAll();
    public T read(K k);
    public void create(T t) throws PersistenciaException;
}
