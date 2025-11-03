package br.univates.sistemabancario.repository;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import br.univates.alexandria.exceptions.DataBaseException;
import br.univates.alexandria.repository.DataBaseConnectionManager;
import br.univates.sistemabancario.App;

public class DataBaseHelper {
    public static DataBaseConnectionManager initializeDataBaseConnectionManager()
            throws DataBaseException, IOException {
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
    }
}
