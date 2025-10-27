package br.univates.sistemabancario.view.menu;

import java.util.ArrayList;

import javax.swing.SwingUtilities;

import br.univates.alexandria.repository.DataBaseConnectionManager;
import br.univates.alexandria.util.Messages;
import br.univates.alexandria.view.MenuDialog;
import br.univates.sistemabancario.repository.ContaBancariaDAO;
import br.univates.sistemabancario.repository.CorrentistaDAO;
import br.univates.sistemabancario.service.ContaBancaria;
import br.univates.sistemabancario.view.tela.TelaCadastroConta;

/**
 * Menu responsável por mostrar opções referentes à contas bancárias
 * 
 * @author mateus.brambilla
 */
public class MenuContasBancarias extends MenuDialog {
    private final CorrentistaDAO cdao;
    private final ContaBancariaDAO cbdao;
    private final DataBaseConnectionManager db;

    // Construtor
    public MenuContasBancarias(CorrentistaDAO cdao, ContaBancariaDAO cbdao, DataBaseConnectionManager db) {
        super(null, true);

        this.cdao = cdao;
        this.cbdao = cbdao;
        this.db = db;

        setTitulo("Menu: Conta Bancária");
        setSubtitulo("Opções relacionadas à Conta Bancária");
        adicionarOpcoes();
    }

    /**
     * Método privado responsável por adicionar as opções no menu
     */
    private void adicionarOpcoes() {
        addOption("Cadastrar Conta Bancária", () -> cadastrarContaBancaria());
        addOption("Manusear Contas Bancárias", () -> manusearContas());
        addOption("Visualizar Contas Bancárias", () -> visualizarContas());
        addDefaultLastOption();
    }

    /**
     * Método privado responsável por abrir um dialog de cadastro de conta
     * Só abre se houver um correntista
     */
    private void cadastrarContaBancaria() {
        if (cdao.readAll().isEmpty()) {
            Messages.infoMessage("Não há correntistas cadastrados");
        } else {
            SwingUtilities.invokeLater(() -> {
                TelaCadastroConta dialog = new TelaCadastroConta(null, this.cdao, this.cbdao);
                dialog.setVisible(true);
            });
        }
    }

    /**
     * Método privado responsável por abrir o menu de controle de contas
     * Só abre se houver uma conta cadastrada
     */
    private void manusearContas() {
        if (cbdao.readAll().isEmpty()) {
            Messages.infoMessage("Não há contas bancárias cadastradas");
        } else {
            SwingUtilities.invokeLater(() -> {
                MenuSelecaoConta mc = new MenuSelecaoConta(null, true, this.cdao, this.cbdao, this.db);
                mc.exibir();
            });
        }
    }

    /**
     * Método privado responsável pela visualização das contas bancárias
     */
    private void visualizarContas() {
        if (cbdao.readAll().isEmpty()) {
            Messages.infoMessage("Não há contas bancárias cadastradas");
        } else {
            StringBuilder sb = new StringBuilder();

            sb.append("== Contas Bancárias Cadastradas\n\n");

            ArrayList<ContaBancaria> cbList = cbdao.readAll();

            for (ContaBancaria cb : cbList) {
                sb.append(cb.toString());

                if (cbList.indexOf(cb) != cbList.size() - 1) {
                    sb.append("\n");
                }
            }

            Messages.infoMessage(sb.toString());
        }
    }
}