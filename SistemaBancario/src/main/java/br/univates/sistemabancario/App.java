package br.univates.sistemabancario;

import com.formdev.flatlaf.FlatDarkLaf;

import br.univates.alexandria.interfaces.IDao;
import br.univates.alexandria.models.CPF;
import br.univates.alexandria.models.Pessoa;
import br.univates.alexandria.util.Messages;
import br.univates.sistemabancario.repository.DAOFactory;
import br.univates.sistemabancario.repository.interfaces.IDaoTransacao;
import br.univates.sistemabancario.service.ContaBancaria;
import br.univates.sistemabancario.service.Transacao;
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
            // Injestão de dependências
            IDao<Pessoa, CPF> correntistaDAO = DAOFactory.getCorrentistaDAO();
            IDao<ContaBancaria, Integer> contaBancariaDAO = DAOFactory.getContaBancariaDAO();
            IDaoTransacao<Transacao, Integer> transacaoDAO = DAOFactory.getTransacaoDAO();

            TelaPrincipal tp = new TelaPrincipal(correntistaDAO, contaBancariaDAO, transacaoDAO);
            tp.iniciarMenuContas();
        } catch (Exception e) {
            Messages.errorMessage(e.getMessage(), "Erro fatal ao iniciar a aplicação");
            System.exit(0);
        }
    }
}