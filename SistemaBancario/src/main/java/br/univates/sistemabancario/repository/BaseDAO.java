package br.univates.sistemabancario.repository;

import java.io.IOException;

import br.univates.alexandria.exceptions.DataBaseException;
import br.univates.alexandria.repository.DataBaseConnectionManager;

/**
 * Classe base para todos os DAOs, centralizando a lógica de conexão.
 */
public abstract class BaseDAO {

    /**
     * Método auxiliar para obter e testar a conexão.
     */
    protected DataBaseConnectionManager getDatabaseConnection() throws DataBaseException {
        try {
            return DataBaseHelper.initializeDataBaseConnectionManager();
        } catch (IOException e) {
            throw new DataBaseException("Falha ao ler o arquivo de configuração do banco de dados: " + e.getMessage());
        }
    }
}
