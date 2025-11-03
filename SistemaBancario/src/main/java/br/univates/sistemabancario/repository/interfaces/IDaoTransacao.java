package br.univates.sistemabancario.repository.interfaces;

import java.util.ArrayList;

import br.univates.alexandria.exceptions.DataBaseException;
import br.univates.alexandria.exceptions.DuplicatedKeyException;
import br.univates.alexandria.exceptions.RecordNotFoundException;
import br.univates.alexandria.exceptions.RecordNotReady;

/**
 * Define o contrato padrão para um Data Access Object (DAO) genérico.
 * Esta interface estabelece as operações básicas de persistência (CRUD)
 * que devem ser implementadas por qualquer classe DAO.
 *
 * @param <T> O tipo do objeto (entidade/modelo) que será gerenciado (ex:
 *            Pessoa, ContaBancaria).
 * @param <K> O tipo da chave primária (Primary Key) usada para buscar o objeto
 *            (ex: CPF, Numero).
 */
public interface IDaoTransacao<T, K> {

    /**
     * Insere um novo objeto no repositório de dados.
     *
     * @param objeto O objeto (T) a ser persistido.
     * @throws DuplicatedKeyException Se um registro com a mesma chave primária (ou
     *                                chave única) já existir.
     * @throws RecordNotReady         Se o objeto não estiver em um estado válido
     *                                para ser salvo (ex: campos obrigatórios
     *                                nulos).
     */
    public void create(T objeto) throws DuplicatedKeyException, DataBaseException;

    /**
     * Busca e retorna um objeto do repositório de dados com base em sua chave
     * primária.
     *
     * @param pkey A chave primária (K) do objeto a ser lido.
     * @return O objeto (T) encontrado.
     * @throws RecordNotFoundException Se nenhum registro for encontrado com a chave
     *                                 primária fornecida.
     */
    public ArrayList<T> read(K pkey) throws RecordNotFoundException, DataBaseException;
}