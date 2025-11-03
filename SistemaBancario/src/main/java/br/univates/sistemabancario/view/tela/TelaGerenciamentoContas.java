package br.univates.sistemabancario.view.tela;

import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import br.univates.alexandria.exceptions.DataBaseException;
import br.univates.alexandria.exceptions.RecordNotReady;
import br.univates.alexandria.interfaces.IDao;
import br.univates.alexandria.models.CPF;
import br.univates.alexandria.models.Pessoa;
import br.univates.alexandria.util.Messages;
import br.univates.alexandria.view.MenuDialog;
import br.univates.sistemabancario.repository.interfaces.IDaoTransacao;
import br.univates.sistemabancario.service.ContaBancaria;
import br.univates.sistemabancario.service.Transacao;

public class TelaGerenciamentoContas {
    private JFrame parentFrame;

    public TelaGerenciamentoContas(JFrame parent) {
        this.parentFrame = parent;
    }

    /**
     * Menu responsável por mostrar opções referentes à contas bancárias
     * * @author mateus.brambilla
     */
    public class MenuContasBancarias extends MenuDialog {
        private final IDao<Pessoa, CPF> cdao;
        private final IDao<ContaBancaria, Integer> cbdao;
        private final IDaoTransacao<Transacao, Integer> tdao;
        private final JFrame parentFrame;

        // Construtor
        public MenuContasBancarias(JFrame parent, IDao<Pessoa, CPF> cdao,
                IDao<ContaBancaria, Integer> cbdao,
                IDaoTransacao<Transacao, Integer> tdao) {
            super(parent, true);
            this.parentFrame = parent;
            this.cdao = cdao;
            this.cbdao = cbdao;
            this.tdao = tdao;

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
            try {
                if (cdao.readAll().isEmpty()) {
                    Messages.infoMessage("Não há correntistas cadastrados");
                } else {
                    SwingUtilities.invokeLater(() -> {
                        TelaCadastroConta dialog = new TelaCadastroConta(parentFrame, this.cdao, this.cbdao);
                        dialog.setVisible(true);
                    });
                }
            } catch (DataBaseException | RecordNotReady e) {
                Messages.errorMessage(e.getMessage(), "Erro ao ler correntistas");
            }
        }

        /**
         * Método privado responsável por abrir o menu de controle de contas
         * Só abre se houver uma conta cadastrada
         */
        private void manusearContas() {
            try {
                if (cbdao.readAll().isEmpty()) {
                    Messages.infoMessage("Não há contas bancárias cadastradas");
                } else {
                    SwingUtilities.invokeLater(() -> {
                        TelaSelecaoConta mc = new TelaSelecaoConta(parentFrame, true, this.cdao, this.cbdao, this.tdao);
                        mc.exibir();
                    });
                }
            } catch (DataBaseException | RecordNotReady e) {
                Messages.errorMessage(e.getMessage(), "Erro ao ler contas bancárias");
            }
        }

        /**
         * Método privado responsável pela visualização das contas bancárias
         */
        private void visualizarContas() {
            try {
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
            } catch (DataBaseException | RecordNotReady e) {
                Messages.errorMessage(e.getMessage(), "Erro ao visualizar contas");
            }
        }
    }
}
