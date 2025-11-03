package br.univates.sistemabancario.repository;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import br.univates.alexandria.exceptions.DataBaseException;
import br.univates.alexandria.interfaces.IDao;
import br.univates.alexandria.models.CPF;
import br.univates.alexandria.models.Pessoa;
import br.univates.alexandria.repository.DataBaseConnectionManager;
import br.univates.sistemabancario.App;
import br.univates.sistemabancario.repository.interfaces.IDaoTransacao;
import br.univates.sistemabancario.service.ContaBancaria;

/**
 * Fábrica para criar instâncias de DAOs.
 * Esta fábrica utiliza Injeção de Dependência, recebendo a conexão
 * que será compartilhada pelos DAOs.
 */
public class DAOFactory {

    /**
     * Cria e retorna uma instância de CorrentistaDAO.
     * 
     * @return - instância de CorrentistaDAO
     */
    public static IDao<Pessoa, CPF> getCorrentistaDAO() {
        return new CorrentistaDAO();
    }

    /**
     * Cria e retorna uma instância de ContaBancariaDAO.
     * 
     * @return - instância de ContaBancariaDAO
     */
    public static IDao<ContaBancaria, Integer> getContaBancariaDAO() {
        return new ContaBancariaDAO();
    }

    /**
     * Cria e retorna uma instância de TransacaoDAO.
     * 
     * @return - instância de TransacaoDAO
     */
    public static IDaoTransacao getTransacaoDAO() {
        return new TransacaoDAO();
    }

    /**
     * Cria e retorna uma instância de DataBaseConnectionManager
     * @return - instância de DataBaseConnectionManager
     * @throws DataBaseException - erro de conexão
     */
    public static DataBaseConnectionManager getDataBaseConnectionManager() throws DataBaseException {
        return getSqliteConnection();
    }

    /**
     * Cria e retorna uma instância do gerenciador de banco de dados do postgresql
     * @return dbcm (DataBaseConnectionManager.SQLITE)
     * @throws DataBaseException - em caso de erro de conexão
     */
    private static DataBaseConnectionManager getPostgresqlConnection() throws DataBaseException {
        try {
            // Ocultando informações do banco de dados
            Properties props = new Properties();
            try (InputStream input = App.class.getClassLoader().getResourceAsStream("config.properties")) {
                if (input == null) {
                    throw new IOException("Arquivo 'config.properties' não encontrado");
                }
                props.load(input);
            }

            DataBaseConnectionManager dbcm = new DataBaseConnectionManager(
                    DataBaseConnectionManager.POSTGRESQL,
                    props.getProperty("db.name"),
                    props.getProperty("db.user"),
                    props.getProperty("db.password"));
            dbcm.connectionTest();

            return dbcm;
        } catch (IOException e) {
            throw new DataBaseException("Falha ao ler o arquivo de configuração do banco de dados: " + e.getMessage());
        }
    }

    /**
     * Cria e retorna uma instância do gerenciador de banco de dados do sqlite
     * @return dbcm (DataBaseConnectionManager.POSTGRESQL)
     * @throws DataBaseException - em caso de erro de conexão
     */
    private static DataBaseConnectionManager getSqliteConnection() throws DataBaseException {
        DataBaseConnectionManager dbcm = new DataBaseConnectionManager(
                DataBaseConnectionManager.SQLITE,
                "sistemabancario.db",
                "",
                "");
                
        dbcm.connectionTest();

        dbcm.runSQL(
            """
            CREATE TABLE IF NOT EXISTS correntista (
                cpf_correntista VARCHAR(11) PRIMARY KEY,
                nome VARCHAR(255) NOT NULL,
                endereco VARCHAR(100)
            );
            """
        );

        dbcm.runSQL(
            """
            CREATE TABLE IF NOT EXISTS conta (
                numero_conta INTEGER PRIMARY KEY,
                tipo_conta CHAR(1) NOT NULL,
                limite_conta REAL DEFAULT 0,
                cpf_correntista VARCHAR(11) NOT NULL,
                saldo REAL,

                FOREIGN KEY (cpf_correntista) REFERENCES correntista(cpf_correntista)
            );
            """
        );

        dbcm.runSQL(
            """
            CREATE TABLE IF NOT EXISTS transacao (
                numero_conta INTEGER NOT NULL,
                data_transacao TIMESTAMP NOT NULL,
                descricao TEXT,
                valor REAL NOT NULL,
                tipo CHAR(1) NOT NULL,
                saldo REAL NOT NULL,
                
                FOREIGN KEY (numero_conta) REFERENCES conta(numero_conta)
            );
            """
        );

        return dbcm;
    }
}
