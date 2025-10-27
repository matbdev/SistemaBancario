package br.univates.sistemabancario;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import com.formdev.flatlaf.FlatDarkLaf;

import br.univates.alexandria.exceptions.DataBaseException;
import br.univates.alexandria.repository.DataBaseConnectionManager;
import br.univates.alexandria.util.Messages;
import br.univates.sistemabancario.repository.ContaBancariaDAO;
import br.univates.sistemabancario.repository.CorrentistaDAO;
import br.univates.sistemabancario.view.tela.TelaPrincipal;

/**
 * Ponto de entrada da aplicação
 * 
 * @author mateus.brambilla
 */
public class App {
    public static void main(String[] args) {
        FlatDarkLaf.setup(); // Configuração e aplicação do FlatLaf

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

            // Ingestão de dependências
            CorrentistaDAO cdao = new CorrentistaDAO(dbcm);
            ContaBancariaDAO cbdao = new ContaBancariaDAO(cdao, dbcm);

            TelaPrincipal tp = new TelaPrincipal();
            tp.iniciarMenuContas(cdao, cbdao, dbcm);
        } catch (DataBaseException | IOException e) {
            Messages.errorMessage(e);
            System.exit(0);
        }
    }
}