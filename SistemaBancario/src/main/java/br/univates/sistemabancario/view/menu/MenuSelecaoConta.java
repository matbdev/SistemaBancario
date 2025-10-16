package br.univates.sistemabancario.view.menu;

import java.awt.Color;
import java.awt.Frame;
import java.util.ArrayList;

import javax.swing.Box;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import br.univates.alexandria.models.Pessoa;
import br.univates.alexandria.util.FormatadorTexto;
import br.univates.alexandria.util.Verificador;
import br.univates.alexandria.view.MenuJPanel;
import br.univates.alexandria.view.OpcaoButton;
import br.univates.sistemabancario.repository.ContaBancariaDAO;
import br.univates.sistemabancario.repository.CorrentistaDAO;
import br.univates.sistemabancario.service.ContaBancaria;
import br.univates.sistemabancario.view.elements.PessoaComboBox;

public class MenuSelecaoConta extends javax.swing.JDialog{
    private final CorrentistaDAO cdao;
    private final ContaBancariaDAO cbdao;

    // Componentes
    private PessoaComboBox cbCorrentista;
    private MenuJPanel buttonsPanel;
    private JPanel painelSuperior;
    private JLabel titleLabel;

    /**
     * Construtor que recebe o frame pai e se é modal ou não
     * Também recebe um dao de correntista
     * @param parent - frame pai
     * @param modal - se é modal ou não
     * @param cdao - dao de correntista
     * @param cbdao - dao de conta bancária
     */
    public MenuSelecaoConta(Frame parent, boolean modal, CorrentistaDAO cdao, ContaBancariaDAO cbdao){
        super(parent, modal);
        this.cdao = cdao;
        this.cbdao = cbdao;

        initComponents();
        addDisabledOption("Selecione um usuário");
        buttonsPanel.revalidate();
        buttonsPanel.repaint();
        pack();

        setTitulo("Menu: Contas Disponíveis");
        setSubtitulo("Escolha uma conta para realizar movimentações");
    }

    /**
     * Método que adiciona uma opção padrão, desabilitada
     */
    private void addDisabledOption(String text){
        OpcaoButton ob = new OpcaoButton(text, () -> {});
        ob.setEnabled(false);
        buttonsPanel.addOption(ob, MenuJPanel.DISABLED_COLOR_FOR_BUTTON);
        add(Box.createVerticalStrut(5));
    }

    /**
     * Define ou atualiza o título do menu.
     * @param titulo - titulo a ser setado
     */
    public void setTitulo(String titulo) {
        Verificador.verificaVazio(titulo, "O título não pode estar vazio.");
        setTitle(FormatadorTexto.converteTitleCase(titulo));
    }
    
    /**
     * Define ou atualiza o subtítulo do menu.
     * @param subtitulo - subtítulo a ser setado
     */
    public void setSubtitulo(String subtitulo) {
        Verificador.verificaVazio(subtitulo, "O subtítulo não pode estar vazio.");
        this.titleLabel.setText(subtitulo);
        this.titleLabel.setSize(this.titleLabel.getWidth(), 30);
    }

    /**
     * Adiciona uma opção completa ao menu, associando um ícone a um texto.
     * @param text O texto descritivo da opção.
     * @param acao A ação executada pela opção
     */
    public void addOption(String text, Runnable acao) {
        buttonsPanel.addOption(new OpcaoButton(text, acao));
    }

    /**
     * Adiciona uma opção completa ao menu, associando um ícone a um texto.
     * @param text O texto descritivo da opção.
     * @param acao A ação executada pela opção
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
                () -> this.dispose()
            ), MenuJPanel.RED_COLOR_FOR_BUTTON
        );
   }
    
    /**
     * Método principal de exibição do menu
     */
    public void exibir() {
        buttonsPanel.revalidate();
        buttonsPanel.repaint();
        pack();
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
        cbCorrentista = new PessoaComboBox(this.cdao);
        painelSuperior = new JPanel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        painelSuperior.setLayout(new javax.swing.BoxLayout(painelSuperior, javax.swing.BoxLayout.Y_AXIS));
        
        titleLabel.setFont(new java.awt.Font("Segoe UI", 1, 16));
        titleLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        titleLabel.setText("Menu Title");
        titleLabel.setAlignmentX(java.awt.Component.CENTER_ALIGNMENT);
        titleLabel.setBorder(javax.swing.BorderFactory.createEmptyBorder(10, 10, 5, 10));
        painelSuperior.add(titleLabel);

        cbCorrentista.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 10, 10, 10));
        cbCorrentista.setAutoscrolls(true);
        cbCorrentista.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbCorrentistaActionPerformed(evt);
            }
        });

        painelSuperior.add(cbCorrentista);

        getContentPane().add(painelSuperior, java.awt.BorderLayout.NORTH);

        buttonsPanel.setLayout(new javax.swing.BoxLayout(buttonsPanel, javax.swing.BoxLayout.Y_AXIS));
        buttonsPanel.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 15, 15, 15));
        
        getContentPane().add(buttonsPanel, java.awt.BorderLayout.CENTER);

        setMinimumSize(new java.awt.Dimension(300, 200));
    }

    /**
     * Método responsável por realizar uma ação quando o combobox é alterado
     * Carrega as contas apenas do correntista em específico
     * @param evt - não utilizado
     */
    private void cbCorrentistaActionPerformed(java.awt.event.ActionEvent evt) {
        buttonsPanel.removeAll();
        Pessoa correntista = (Pessoa) this.cbCorrentista.getSelectedItem();

        if(correntista == null){
            addDisabledOption("Selecione um usuáiro");
        }else{
            ArrayList<ContaBancaria> cbList = this.cbdao.read(correntista);

            if(cbList.isEmpty()){
                addDisabledOption("Usuário sem contas");
            }else{
                for (ContaBancaria cb : cbList) {
                    System.out.println(cb);
                    addOption(cb.getNumeroContaFormatado(), () -> rodaMenuBanco(cb));
                }
            }
            addDefaultLastOption();
        }

        buttonsPanel.revalidate();
        buttonsPanel.repaint();
        pack();
    }
    
    /**
     * Recebe uma conta bancária e abre o MenuBanco para ela.
     * @param conta - conta bancária selecionada pelo usuário.
     */
    private void rodaMenuBanco(ContaBancaria conta) {
        if (conta != null) {
            SwingUtilities.invokeLater(() -> {
                MenuBanco m = new MenuBanco(conta, this.cbdao);
                m.exibir();
            });
        }
    }
}