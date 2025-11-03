package br.univates.sistemabancario.view.tela;

import java.awt.Color;
import java.awt.Frame;
import java.util.ArrayList;
import java.util.Calendar;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import br.univates.alexandria.exceptions.DataBaseException;
import br.univates.alexandria.exceptions.DuplicatedKeyException;
import br.univates.alexandria.exceptions.NullInputException;
import br.univates.alexandria.exceptions.RecordNotFoundException;
import br.univates.alexandria.exceptions.RecordNotReady;
import br.univates.alexandria.interfaces.IDao;
import br.univates.alexandria.models.CPF;
import br.univates.alexandria.models.Pessoa;
import br.univates.alexandria.util.FormatadorTexto;
import br.univates.alexandria.util.Inputs;
import br.univates.alexandria.util.Messages;
import br.univates.alexandria.util.Verificador;
import br.univates.alexandria.view.MenuDialog;
import br.univates.alexandria.view.MenuJPanel;
import br.univates.alexandria.view.OpcaoButton;
import br.univates.sistemabancario.exceptions.SaldoInvalidoException;
import br.univates.sistemabancario.repository.interfaces.IDaoTransacao;
import br.univates.sistemabancario.service.ContaBancaria;
import br.univates.sistemabancario.service.Transacao;
import br.univates.sistemabancario.view.elements.PessoaComboBox;

public class TelaSelecaoConta extends javax.swing.JDialog {
    private final IDao<Pessoa, CPF> cdao;
    private final IDao<ContaBancaria, Integer> cbdao;
    private final IDaoTransacao<Transacao, Integer> tdao;

    // Componentes
    private PessoaComboBox cbCorrentista;
    private MenuJPanel buttonsPanel;
    private JPanel painelSuperior;
    private JLabel titleLabel;

    /**
     * Construtor que recebe o frame pai e se é modal ou não
     * Também recebe um dao de correntista
     * 
     * @param parent - frame pai
     * @param modal  - se é modal ou não
     * @param cdao   - dao de correntista
     * @param cbdao  - dao de conta bancária
     */
    public TelaSelecaoConta(Frame parent, boolean modal,
            IDao<Pessoa, CPF> cdao,
            IDao<ContaBancaria, Integer> cbdao,
            IDaoTransacao<Transacao, Integer> tdao) {
        super(parent, modal);

        this.cdao = cdao;
        this.cbdao = cbdao;
        this.tdao = tdao;

        initComponents();
        addDisabledOption("Selecione um usuário");
        addDefaultLastOption();
        buttonsPanel.revalidate();
        buttonsPanel.repaint();
        pack();

        setTitulo("Menu: Contas Disponíveis");
        setSubtitulo("Escolha uma conta para realizar movimentações");
    }

    /**
     * Método que adiciona uma opção padrão, desabilitada
     */
    private void addDisabledOption(String text) {
        OpcaoButton ob = new OpcaoButton(text, () -> {
        });
        ob.setEnabled(false);
        buttonsPanel.addOption(ob, MenuJPanel.DISABLED_COLOR_FOR_BUTTON);
    }

    /**
     * Define ou atualiza o título do menu.
     * 
     * @param titulo - titulo a ser setado
     */
    public void setTitulo(String titulo) {
        Verificador.verificaVazio(titulo, "O título não pode estar vazio.");
        setTitle(FormatadorTexto.converteTitleCase(titulo));
    }

    /**
     * Define ou atualiza o subtítulo do menu.
     * 
     * @param subtitulo - subtítulo a ser setado
     */
    public void setSubtitulo(String subtitulo) {
        Verificador.verificaVazio(subtitulo, "O subtítulo não pode estar vazio.");
        this.titleLabel.setText(subtitulo);
        this.titleLabel.setSize(this.titleLabel.getWidth(), 30);
    }

    /**
     * Adiciona uma opção completa ao menu, associando um ícone a um texto.
     * 
     * @param text O texto descritivo da opção.
     * @param acao A ação executada pela opção
     */
    public void addOption(String text, Runnable acao) {
        buttonsPanel.addOption(new OpcaoButton(text, acao));
    }

    /**
     * Adiciona uma opção completa ao menu, associando um ícone a um texto.
     * 
     * @param text  O texto descritivo da opção.
     * @param acao  A ação executada pela opção
     * @param color A cor do botão
     */
    public void addOption(String text, Runnable acao, Color color) {
        buttonsPanel.addOption(new OpcaoButton(text, acao), color);
    }

    /**
     * Adiciona a opção final do menu, que por padrão é a de voltar.
     * Esta opção não terá um espaçamento após ela.
     */
    public void addDefaultLastOption() {
        buttonsPanel.addOption(
                new OpcaoButton(
                        "Voltar à página anterior",
                        () -> this.dispose()),
                MenuJPanel.RED_COLOR_FOR_BUTTON);
    }

    /**
     * Método principal de exibição do menu
     */
    public void exibir() {
        this.pack();
        this.setLocationRelativeTo(null);
        this.setVisible(true);
    }

    /**
     * Método que inicializa os componentes da tela
     */
    private void initComponents() {
        titleLabel = new javax.swing.JLabel();
        buttonsPanel = new MenuJPanel();

        try {
            cbCorrentista = new PessoaComboBox(this.cdao);
        } catch (RecordNotReady | DataBaseException e) {
            Messages.errorMessage(e.getMessage(), "Erro Crítico: Não foi possível carregar os correntistas.");
            dispose();
            return;
        }

        painelSuperior = new JPanel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        painelSuperior.setLayout(new javax.swing.BoxLayout(painelSuperior, javax.swing.BoxLayout.Y_AXIS));

        titleLabel.setFont(new java.awt.Font("Segoe UI", 1, 16));
        titleLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        titleLabel.setText("Menu Title");
        titleLabel.setAlignmentX(java.awt.Component.CENTER_ALIGNMENT);
        titleLabel.setBorder(javax.swing.BorderFactory.createEmptyBorder(10, 10, 5, 10));
        painelSuperior.add(titleLabel);

        JPanel comboBoxPanel = new JPanel(new java.awt.BorderLayout());
        comboBoxPanel.setBorder(javax.swing.BorderFactory.createEmptyBorder(5, 10, 5, 10));
        comboBoxPanel.add(cbCorrentista, java.awt.BorderLayout.CENTER);

        cbCorrentista.setAutoscrolls(true);
        cbCorrentista.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbCorrentistaActionPerformed(evt);
            }
        });

        painelSuperior.add(comboBoxPanel);

        getContentPane().add(painelSuperior, java.awt.BorderLayout.NORTH);

        buttonsPanel.setLayout(new javax.swing.BoxLayout(buttonsPanel, javax.swing.BoxLayout.Y_AXIS));
        buttonsPanel.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 15, 15, 15));

        getContentPane().add(buttonsPanel, java.awt.BorderLayout.CENTER);

        setMinimumSize(new java.awt.Dimension(300, 200));
    }

    /**
     * Método responsável por realizar uma ação quando o combobox é alterado
     * Carrega as contas apenas do correntista em específico
     * 
     * @param evt - não utilizado
     */
    private void cbCorrentistaActionPerformed(java.awt.event.ActionEvent evt) {
        buttonsPanel.removeAll();
        Pessoa correntista = (Pessoa) this.cbCorrentista.getSelectedItem();

        if (correntista == null) {
            addDisabledOption("Selecione um usuário");
        } else {
            try {
                ArrayList<ContaBancaria> cbList = this.cbdao.readAll(
                        conta -> conta.getPessoa().equals(correntista));

                if (cbList.isEmpty()) {
                    addDisabledOption("Usuário sem contas");
                } else {
                    for (ContaBancaria cb : cbList) {
                        final ContaBancaria contaFinal = cb;
                        addOption("Conta: " + contaFinal.getNumeroContaFormatado(), () -> rodaMenuBanco(contaFinal));
                    }
                }
            } catch (RecordNotReady | DataBaseException e) {
                Messages.errorMessage(e.getMessage(), "Erro ao carregar contas");
                addDisabledOption("Erro ao carregar contas");
            }
        }

        addDefaultLastOption();
        buttonsPanel.revalidate();
        buttonsPanel.repaint();
        pack();
    }

    /**
     * Recebe uma conta bancária e abre o MenuBanco para ela.
     * 
     * @param conta - conta bancária selecionada pelo usuário.
     */
    private void rodaMenuBanco(ContaBancaria conta) {
        if (conta != null) {
            SwingUtilities.invokeLater(() -> {
                MenuBanco m = new MenuBanco(conta, this.cbdao, this.tdao);
                m.exibir();
            });
        }
    }

    /**
     * Classe que herda de menu, representando a view do nosso banco
     * 
     * @author mateus.brambilla
     */
    public class MenuBanco extends MenuDialog {
        private final ContaBancaria cb;
        private final Calendar c = Calendar.getInstance();
        private final IDaoTransacao<Transacao, Integer> tdao;
        private final IDao<ContaBancaria, Integer> cbdao;

        /**
         * Construtor que recebe cpf, nome, endereco e saldo e instancia classe
         * 
         * @param cb - conta bancária já instanciada
         */
        public MenuBanco(ContaBancaria cb, IDao<ContaBancaria, Integer> cbdao, IDaoTransacao<Transacao, Integer> tdao) {
            super(null, true);

            this.cb = cb;
            this.cbdao = cbdao;
            this.tdao = tdao;

            setTitulo("Menu: Operações Bancárias");
            setSubtitulo("Operações de movimentação e visualização de conta");
            adicionarOpcoes();
        }

        /**
         * Método auxilair que adiciona as opções ao menu
         */
        private void adicionarOpcoes() {
            addOption("Depositar valor", () -> depositarValor());
            addOption("Sacar valor", () -> sacarValor());
            addOption("Verificar status", () -> verificarStatus());
            addOption("Consultar logs", () -> consultarLogs());
            addDefaultLastOption();
        }

        /**
         * Método que realiza um depósito de modo totalmente autônomo
         */
        private void depositarValor() {
            try {
                double dQtde = Inputs.Double(
                        "Infome a quantidade a ser depositada",
                        "DEPÓSITO");

                this.cb.depositaValor(dQtde);
                this.tdao.create(
                        new Transacao(
                                dQtde,
                                this.cb.getSaldo(),
                                Transacao.DEFAULT_DESC,
                                Transacao.MOV_CREDITO,
                                c.getTime(),
                                this.cb.getNumeroConta()));
                this.cbdao.update(this.cb); // atualiza conta no arquivo

                verificarStatus();

            } catch (SaldoInvalidoException | DataBaseException | DuplicatedKeyException | RecordNotFoundException e) {
                Messages.errorMessage(e);
            } catch (NullInputException e) {
                Messages.infoMessage("Cancelando operação...");
            }
        }

        /**
         * Método que realiza um saque de modo totalmente autônomo
         */
        private void sacarValor() {
            try {
                double dQtde = Inputs.Double(
                        "Informe a quantidade a ser sacada",
                        "SAQUE");

                this.cb.sacarValor(dQtde);

                this.tdao.create(
                        new Transacao(
                                dQtde,
                                this.cb.getSaldo(),
                                Transacao.DEFAULT_DESC,
                                Transacao.MOV_DEBITO,
                                c.getTime(),
                                this.cb.getNumeroConta()));
                this.cbdao.update(this.cb);

                verificarStatus();

            } catch (SaldoInvalidoException | DataBaseException | DuplicatedKeyException | RecordNotFoundException e) {
                Messages.errorMessage(e);
            } catch (NullInputException e) {
                Messages.infoMessage("Cancelando operação...");
            }
        }

        /**
         * Método que informa o status da conta
         */
        private void verificarStatus() {
            Messages.infoMessage(this.cb.consultarStatus());
        }

        /**
         * Método que informa os logs de movimentação da conta
         * Se não houve nenhuma movimentação naquela conta, exibe mensagem informativa
         */
        private void consultarLogs() {
            try {
                StringBuilder sb = new StringBuilder();
                ArrayList<Transacao> tList = this.tdao.read(this.cb.getNumeroConta().getNumeroInt());

                if (tList.isEmpty()) {
                    Messages.infoMessage("Não há nenhum registro de movimentação nessa conta");
                } else {

                    for (Transacao t : tList) {
                        sb.append(t);

                        if (tList.indexOf(t) != tList.size() - 1) {
                            sb.append("\n");
                        }
                    }

                    Messages.infoMessage(sb.toString());
                }
            } catch (RecordNotFoundException | DataBaseException e) {
                Messages.errorMessage(e);
            }
        }
    }

}