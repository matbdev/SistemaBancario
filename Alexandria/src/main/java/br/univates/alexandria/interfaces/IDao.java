package br.univates.alexandria.interfaces;

import java.util.ArrayList;

import br.univates.alexandria.exceptions.DataBaseException;
import br.univates.alexandria.exceptions.DuplicatedKeyException;
import br.univates.alexandria.exceptions.RecordNotFoundException;
import br.univates.alexandria.exceptions.RecordNotReady;
import br.univates.alexandria.repository.DataBaseConnectionManager;

/**
 * Define o contrato padrão para um Data Access Object (DAO) genérico.
 * Esta interface estabelece as operações básicas de persistência (CRUD)
 * que devem ser implementadas por qualquer classe DAO.
 *
 * @param <T> O tipo do objeto que será gerenciado.
 * @param <K> O tipo da chave primária usada para buscar o objeto .
 */
public interface IDao<T, K> {

    /**
     * Insere um novo objeto no repositório de dados.
     *
     * @param objeto O objeto (T) a ser persistido.
     * @throws DuplicatedKeyException - chave duplicada
     * @throws DataBaseException - erro de comunicação com o banco
     */
    public void create(T objeto) throws DuplicatedKeyException, DataBaseException;

    /**
     * Busca e retorna um objeto do repositório de dados com base em sua chave primária.
     *
     * @param pkey - chave primária do objeto a ser lido.
     * @return - objeto encontrado.
     * @throws RecordNotFoundException - em caso de nenhuma correspondência
     * @throws DataBaseException - erro de comunicação com o banco
     */
    public T read(K pkey) throws RecordNotFoundException, DataBaseException;

    /**
     * Busca e retorna todos os objetos (T) do repositório de dados.
     *
     * @return lista com os registros encontrados
     * @throws RecordNotReady - erros de atributos de objeto
     * @throws DataBaseException - erro de comunicação com o banco
     */
    public ArrayList<T> readAll() throws RecordNotReady, DataBaseException;

    /**
     * Busca e retorna uma lista filtrada de objetos do repositório de dados.
     *
     * @param filtro - filtro a ser aplicado em cada registro.
     * @return - lista contendo apenas os registros que satisfazem o filtro.
     * @throws RecordNotReady - erros de atributos de objeto
     * @throws DataBaseException - erro de comunicação com o banco
     */
    public ArrayList<T> readAll(IFilter<T> filtro) throws RecordNotReady, DataBaseException;

    /**
     * Atualiza um objeto existente no repositório de dados.
     *
     * @param objeto - objeto contendo os dados atualizados.
     * @throws RecordNotFoundException - registro não encontrado no banco de dados
     * @throws DataBaseException - erro de comunicação com o banco
     */
    public void update(T objeto) throws RecordNotFoundException, DataBaseException;

    /**
     * Atualiza um objeto existente no repositório de dados.
     * Recebe a conexão com o banco de dados, para garantir operações múltiplas
     *
     * @param objeto - objeto contendo os dados atualizados.
     * @param db - conexão com o banco
     * @throws RecordNotFoundException - registro não encontrado no banco de dados
     * @throws DataBaseException - erro de comunicação com o banco
     */
    public void update(T objeto, DataBaseConnectionManager db) throws RecordNotFoundException, DataBaseException;

    /**
     * Remove um objeto do repositório de dados.
     *
     * @param objeto - objeto a ser removido (identificado por sua chave).
     * @throws RecordNotFoundException - registro não encontrado no banco de dados
     * @throws DataBaseException - erro de comunicação com o banco
     */
    public void delete(T objeto) throws RecordNotFoundException, DataBaseException;
}