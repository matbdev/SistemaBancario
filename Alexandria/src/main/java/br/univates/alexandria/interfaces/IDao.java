package br.univates.alexandria.interfaces;

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
 * @param <T> O tipo do objeto (entidade/modelo) que será gerenciado (ex: Pessoa, ContaBancaria).
 * @param <K> O tipo da chave primária (Primary Key) usada para buscar o objeto (ex: CPF, Numero).
 */
public interface IDao<T,K>{
    
    /**
     * Insere um novo objeto no repositório de dados.
     *
     * @param objeto O objeto (T) a ser persistido.
     * @throws DuplicatedKeyException Se um registro com a mesma chave primária (ou chave única) já existir.
     * @throws RecordNotReady Se o objeto não estiver em um estado válido para ser salvo (ex: campos obrigatórios nulos).
     */
    public void create(T objeto) throws DuplicatedKeyException, DataBaseException;

    /**
     * Busca e retorna um objeto do repositório de dados com base em sua chave primária.
     *
     * @param pkey A chave primária (K) do objeto a ser lido.
     * @return O objeto (T) encontrado.
     * @throws RecordNotFoundException Se nenhum registro for encontrado com a chave primária fornecida.
     */
    public T read(K pkey) throws RecordNotFoundException, DataBaseException;

    /**
     * Busca e retorna todos os objetos (T) do repositório de dados.
     *
     * @return Um ArrayList<T> contendo todos os registros encontrados (pode estar vazio).
     * @throws RecordNotReady Se o repositório não estiver em condições de executar a leitura (ex: falha de conexão inicial).
     */
    public ArrayList<T> readAll() throws RecordNotReady, DataBaseException;

    /**
     * Busca e retorna uma lista filtrada de objetos (T) do repositório de dados.
     *
     * @param filtro O critério de filtro (IFilter) a ser aplicado em cada registro.
     * @return Um ArrayList<T> contendo apenas os registros que satisfazem o filtro.
     * @throws RecordNotReady Se o repositório não estiver em condições de executar a leitura.
     * @see IFilter
     */
    public ArrayList<T> readAll(IFilter<T> filtro) throws RecordNotReady, DataBaseException;

    /**
     * Atualiza um objeto existente no repositório de dados.
     * O objeto é geralmente identificado por sua chave primária contida dentro do próprio objeto.
     *
     * @param objeto O objeto (T) contendo os dados atualizados.
     * @throws RecordNotFoundException Se o registro a ser atualizado não for encontrado no repositório.
     */
    public void update(T objeto) throws RecordNotFoundException, DataBaseException;

    /**
     * Remove um objeto do repositório de dados.
     *
     * @param objeto O objeto (T) a ser removido (identificado por sua chave).
     * @throws RecordNotFoundException Se o registro a ser removido não for encontrado no repositório.
     */
    public void delete(T objeto) throws RecordNotFoundException, DataBaseException;
}