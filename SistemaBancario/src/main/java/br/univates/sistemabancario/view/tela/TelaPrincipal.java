package br.univates.sistemabancario.view.tela;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import br.univates.alexandria.interfaces.IDao;
import br.univates.alexandria.models.CPF;
import br.univates.alexandria.models.Pessoa;
import br.univates.alexandria.util.Messages;
import br.univates.alexandria.view.MenuDialog;
import br.univates.sistemabancario.repository.interfaces.IDaoTransacao;
import br.univates.sistemabancario.service.ContaBancaria;

/**
 * Tela que serve para garantir estrutura da arquitetura do projeto
 * 
 * @author mateus.brambilla
 */
public class TelaPrincipal extends JFrame {
    private final IDao<Pessoa, CPF> cdao;
    private final IDao<ContaBancaria, Integer> cbdao;
    private final IDaoTransacao tdao;

    public TelaPrincipal(IDao<Pessoa, CPF> cdao,
            IDao<ContaBancaria, Integer> cbdao,
            IDaoTransacao tdao) {
        this.cdao = cdao;
        this.cbdao = cbdao;
        this.tdao = tdao;

        setTitle("Sistema Bancário");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    public void iniciarMenuContas() {
        SwingUtilities.invokeLater(() -> {
            MenuContasCorrentistas m = new MenuContasCorrentistas(this, cdao, cbdao, tdao);
            m.exibir();
            Messages.infoMessage("Saindo da aplicação...");
            System.exit(0);
        });
    }

    /**
     * Menu que separa as funções de correntista e conta bancária
     * 
     * @author mateus.brambilla
     */
    public class MenuContasCorrentistas extends MenuDialog {
        private final IDao<Pessoa, CPF> cdao;
        private final IDao<ContaBancaria, Integer> cbdao;
        private final IDaoTransacao tdao;
        private final JFrame parentFrame;

        /**
         * Construtor que inicializa a classe
         * Recebe objetos dos DAOs para preservar itens estáticos
         */
        public MenuContasCorrentistas(JFrame parent, IDao<Pessoa, CPF> cdao,
                IDao<ContaBancaria, Integer> cbdao,
                IDaoTransacao tdao) {
            super(parent, true); // Agora é modal em relação ao JFrame principal
            this.parentFrame = parent;
            this.cdao = cdao;
            this.cbdao = cbdao;
            this.tdao = tdao;

            setTitulo("Menu: Fluxos");
            setSubtitulo("Escolha entre manusear correntistas ou contas");

            adicionarOpcoes();
        }

        /**
         * Método privado que adiciona as opções a este menu
         */
        private void adicionarOpcoes() {
            addOption("Correntistas", () -> {
                SwingUtilities.invokeLater(() -> {
                    // Passa os DAOs adiante
                    TelaGerenciamentoCorrentistas.MenuCorrentistas mc = new TelaGerenciamentoCorrentistas(
                            parentFrame).new MenuCorrentistas(parentFrame, cdao, cbdao);
                    mc.exibir();
                });
            });
            addOption("Contas Bancárias", () -> {
                SwingUtilities.invokeLater(() -> {
                    // Passa os DAOs adiante
                    TelaGerenciamentoContas.MenuContasBancarias mcb = new TelaGerenciamentoContas(
                            parentFrame).new MenuContasBancarias(parentFrame, cdao, cbdao, tdao);
                    mcb.exibir();
                });
            });
            addCustomLastOption("Encerrar o programa", () -> this.dispose());
        }
    }

}
