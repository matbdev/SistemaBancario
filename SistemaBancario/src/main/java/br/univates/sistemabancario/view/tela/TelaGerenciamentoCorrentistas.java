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
import br.univates.sistemabancario.service.ContaBancaria;

public class TelaGerenciamentoCorrentistas {
    private JFrame parentFrame;

    public TelaGerenciamentoCorrentistas(JFrame parent) {
        this.parentFrame = parent;
    }

    /**
     * Menu responsável por mostrar opções referentes aos correntistas
     * 
     * @author mateus.brambilla
     */
    public class MenuCorrentistas extends MenuDialog {
        private final IDao<Pessoa, CPF> cdao;
        private final IDao<ContaBancaria, Integer> cbdao;
        private final JFrame parentFrame;

        // Construtor
        public MenuCorrentistas(JFrame parent, IDao<Pessoa, CPF> cdao, IDao<ContaBancaria, Integer> cbdao) {
            super(parent, true);
            this.parentFrame = parent;
            this.cdao = cdao;
            this.cbdao = cbdao;

            setTitulo("Menu: Correntista");
            setSubtitulo("Opções relacionadas à Correntista");
            adicionarOpcoes();
        }

        /**
         * Método privado responsável por adicionar as opções no menu
         */
        private void adicionarOpcoes() {
            addOption("Cadastrar Correntista", () -> cadastrarCorrentista());
            addOption("Editar Correntista", () -> editarCorrentista());
            addOption("Deletar Correntista", () -> deletarCorrentista());
            addOption("Visualizar Correntistas", () -> visualizarCorrentistas());
            addDefaultLastOption();
        }

        /**
         * Método privado responsável por abrir um dialog de cadastro de correntista
         */
        private void cadastrarCorrentista() {
            SwingUtilities.invokeLater(() -> {
                TelaCadastroCorrentista dialog = new TelaCadastroCorrentista(parentFrame, cdao);
                dialog.setVisible(true);
            });
        }

        /**
         * Método que verifica se há algum correntista e, se houver, abre a tela de
         * edição
         */
        private void editarCorrentista() {
            try {
                ArrayList<Pessoa> cList = cdao.readAll();

                if (cList.isEmpty()) {
                    Messages.infoMessage("Não há correntistas cadastrados");
                } else {
                    SwingUtilities.invokeLater(() -> {
                        TelaEditarUsuario teu = new TelaEditarUsuario(parentFrame, cdao);
                        teu.setVisible(true);
                    });
                }
            } catch (DataBaseException | RecordNotReady e) {
                Messages.errorMessage(e.getMessage(), "Erro ao ler correntistas");
            }
        }

        /**
         * Método que faz algumas verificações das pessoas e, se houver alguma sem
         * conta,
         * abre uma tela para deletar
         */
        private void deletarCorrentista() {
            try {
                ArrayList<Pessoa> cList = cdao.readAll();
                ArrayList<ContaBancaria> cbList = cbdao.readAll();
                ArrayList<Pessoa> filteredList = new ArrayList<>();

                if (cList.isEmpty()) {
                    Messages.infoMessage("Não há correntistas cadastrados");
                } else {
                    for (Pessoa p : cList) {
                        boolean temConta = false;

                        for (ContaBancaria cb : cbList) {
                            if (cb.getPessoa().equals(p)) {
                                temConta = true;
                                break;
                            }
                        }

                        if (!temConta) {
                            filteredList.add(p);
                        }
                    }

                    if (filteredList.isEmpty()) {
                        Messages.infoMessage("Não há correntistas sem conta bancária cadastrada");
                    } else {
                        SwingUtilities.invokeLater(() -> {
                            TelaDeletarUsuario tdu = new TelaDeletarUsuario(parentFrame, cdao, filteredList);
                            tdu.setVisible(true);
                        });
                    }
                }
            } catch (DataBaseException | RecordNotReady e) {
                Messages.errorMessage(e.getMessage(), "Erro ao ler dados");
            }
        }

        /**
         * Classe que itera por todos os registros de clientes e os lista
         */
        private void visualizarCorrentistas() {
            try {
                ArrayList<Pessoa> cList = cdao.readAll();

                if (cList.isEmpty()) {
                    Messages.infoMessage("Não há nenhum correntista cadastrado");
                } else {
                    StringBuilder sb = new StringBuilder();

                    sb.append("== Correntistas Cadastrados ==\n\n");

                    for (Pessoa p : cList) {
                        sb.append(p);

                        if (cList.indexOf(p) != cList.size() - 1)
                            sb.append("\n");
                    }

                    Messages.infoMessage(sb.toString());
                }
            } catch (DataBaseException | RecordNotReady e) {
                Messages.errorMessage(e.getMessage(), "Erro ao visualizar correntistas");
            }
        }
    }
}
