package br.univates.sistemabancario.repository.interfaces;

import java.util.ArrayList;

import br.univates.alexandria.exceptions.DataBaseException;
import br.univates.alexandria.exceptions.DuplicatedKeyException;
import br.univates.alexandria.exceptions.RecordNotFoundException;
import br.univates.alexandria.repository.DataBaseConnectionManager;
import br.univates.sistemabancario.service.Transacao;

/**
 * Define um DAO específico para Transações
 */
public interface IDaoTransacao {

    /**
     * Insere um novo objeto no repositório de dados.
     *
     * @param Transação a ser persistida
     * @throws DuplicatedKeyException - erro de chave duplicada
     * @throws DataBaseException - erro de conexão
     */
    public void create(Transacao objeto, DataBaseConnectionManager dbcm) throws DuplicatedKeyException, DataBaseException;

    /**
     * Cria um registro de transação usando uma conexão de banco de dados fornecida.
     * Não fecha a conexão, permitindo o uso em transações.
     *
     * @param t  - A Transacao a ser criada
     * @param db - O gerenciador de conexão já aberto
     * @throws RecordNotFoundException - erro de chave não encontrada
     * @throws DataBaseException - erro de conexão
     */
    public ArrayList<Transacao> read(Integer pkey) throws RecordNotFoundException, DataBaseException;
}