package br.univates.sistemabancario.repository.interfaces;

import java.util.ArrayList;

import br.univates.alexandria.exceptions.DataBaseException;
import br.univates.alexandria.exceptions.RecordNotFoundException;
import br.univates.alexandria.repository.DataBaseConnectionManager;
import br.univates.sistemabancario.model.Transacao;

/**
 * Define um DAO específico para Transações
 */
public interface IDaoTransacao {

    /**
     * Insere um novo objeto no repositório de dados.
     * Recebe a conexão com o banco de dados
     *
     * @param objeto - transação a ser persistida
     * @param db - banco de dados
     * @throws DataBaseException - erro de conexão
     */
    public void create(Transacao objeto, DataBaseConnectionManager db) throws DataBaseException;

    /**
     * Cria um registro de transação usando uma conexão de banco de dados fornecida.
     * Não fecha a conexão, permitindo o uso em transações.
     *
     * @param pkey - chave da conta bancária
     * @throws RecordNotFoundException - erro de chave não encontrada
     * @throws DataBaseException - erro de conexão
     */
    public ArrayList<Transacao> read(Integer pkey) throws RecordNotFoundException, DataBaseException;
}