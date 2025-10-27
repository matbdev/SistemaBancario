package br.univates.sistemabancario.view.tela;

import java.awt.Color;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;

import br.univates.alexandria.models.Pessoa;
import br.univates.sistemabancario.repository.CorrentistaDAO;
import br.univates.sistemabancario.view.elements.PessoaComboBox;

/**
 * Tela responsável por realizar a edição de um usuário
 * 
 * @author mateus.brambilla
 */
public class TelaEditarUsuario extends javax.swing.JDialog {
    private final CorrentistaDAO cdao;

    // Componentes
    private JButton botaoCadastro;
    private PessoaComboBox cbCorrentista;
    private JLabel labelCPF;
    private JLabel labelCorrentista;
    private JLabel labelEndereco;
    private JLabel labelNome;
    private JLabel labelSuccessError;
    private JLabel labelTitulo;
    private JTextField tfCPF;
    private JTextField tfEndereco;
    private JTextField tfNome;

    /**
     * Creates new form FrameCadastroCorrentista
     * 
     * @param parent - um JFrame
     * @param cdao   - instancia de CorrentistaDAO
     */
    public TelaEditarUsuario(JFrame parent, CorrentistaDAO cdao) {
        super(parent, "Cadastro de Conta Bancária", true);
        this.cdao = cdao;

        initComponents();
        setLocationRelativeTo(parent);
    }

    /**
     * Método que inicializa os componentes da tela
     */
    private void initComponents() {
        labelCorrentista = new javax.swing.JLabel();
        botaoCadastro = new javax.swing.JButton();
        labelTitulo = new javax.swing.JLabel();
        tfCPF = new javax.swing.JTextField();
        labelCPF = new javax.swing.JLabel();
        labelNome = new javax.swing.JLabel();
        labelSuccessError = new javax.swing.JLabel();
        tfNome = new javax.swing.JTextField();
        labelEndereco = new javax.swing.JLabel();
        tfEndereco = new javax.swing.JTextField();
        cbCorrentista = new PessoaComboBox(this.cdao);

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Correntistas");
        setAlwaysOnTop(true);
        setBackground(new java.awt.Color(51, 51, 51));
        setBounds(new java.awt.Rectangle(0, 0, 0, 0));
        setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));

        labelCorrentista.setText("Informe o correntista:");

        botaoCadastro.setBackground(new java.awt.Color(0, 0, 51));
        botaoCadastro.setForeground(new java.awt.Color(255, 255, 255));
        botaoCadastro.setText("Editar");
        botaoCadastro.setAutoscrolls(true);
        botaoCadastro.addActionListener(this::botaoCadastroActionPerformed);

        labelTitulo.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        labelTitulo.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        labelTitulo.setText("Edição de Correntista");

        tfCPF.setEditable(false);

        labelCPF.setLabelFor(tfCPF);
        labelCPF.setText("CPF do correntista:");

        labelNome.setText("Nome do correntista:");

        cbCorrentista.setAutoscrolls(true);
        cbCorrentista.addActionListener(this::cbCorrentistaActionPerformed);

        labelEndereco.setText("Endereço do correntista:");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(labelTitulo, javax.swing.GroupLayout.Alignment.TRAILING,
                                javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE,
                                Short.MAX_VALUE)
                        .addGroup(layout.createSequentialGroup()
                                .addGap(18, 18, 18)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addGroup(layout.createSequentialGroup()
                                                .addComponent(labelEndereco)
                                                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout
                                                .createSequentialGroup()
                                                .addGroup(layout
                                                        .createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                                        .addComponent(tfEndereco,
                                                                javax.swing.GroupLayout.Alignment.LEADING)
                                                        .addComponent(labelCPF,
                                                                javax.swing.GroupLayout.Alignment.LEADING,
                                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                        .addComponent(cbCorrentista, 0,
                                                                javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                        .addComponent(labelCorrentista,
                                                                javax.swing.GroupLayout.Alignment.LEADING,
                                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                        .addComponent(labelNome, javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                        .addComponent(tfNome)
                                                        .addComponent(tfCPF)
                                                        .addGroup(layout.createSequentialGroup()
                                                                .addGap(0, 0, Short.MAX_VALUE)
                                                                .addComponent(labelSuccessError,
                                                                        javax.swing.GroupLayout.PREFERRED_SIZE, 272,
                                                                        javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                .addGap(18, 18, 18)
                                                                .addComponent(botaoCadastro)))
                                                .addGap(18, 18, 18)))));
        layout.setVerticalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addGap(15, 15, 15)
                                .addComponent(labelTitulo)
                                .addGap(12, 12, 12)
                                .addComponent(labelCorrentista)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(cbCorrentista, javax.swing.GroupLayout.PREFERRED_SIZE,
                                        javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(labelCPF, javax.swing.GroupLayout.PREFERRED_SIZE, 10,
                                        javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(tfCPF, javax.swing.GroupLayout.PREFERRED_SIZE,
                                        javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(labelNome)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(tfNome, javax.swing.GroupLayout.PREFERRED_SIZE,
                                        javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(labelEndereco)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(tfEndereco, javax.swing.GroupLayout.PREFERRED_SIZE,
                                        javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addGroup(layout.createSequentialGroup()
                                                .addGap(18, 18, 18)
                                                .addComponent(labelSuccessError, javax.swing.GroupLayout.PREFERRED_SIZE,
                                                        52, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addContainerGap(15, Short.MAX_VALUE))
                                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout
                                                .createSequentialGroup()
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED,
                                                        javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                .addComponent(botaoCadastro)
                                                .addGap(29, 29, 29)))));

        getAccessibleContext().setAccessibleName("Contas Bancárias");

        pack();
    }

    /**
     * Método que realiza a edição de um usuário quando o botão é pressionado
     * 
     * @param evt - não utilizado
     */
    private void botaoCadastroActionPerformed(@SuppressWarnings("unused") java.awt.event.ActionEvent evt) {
        try {
                Pessoa correntistaSelecionado = (Pessoa) this.cbCorrentista.getSelectedItem();
                if (correntistaSelecionado == null) {
                throw new Exception("Por favor, selecione um correntista.");
                }

                String nome = this.tfNome.getText();
                String endereco = this.tfEndereco.getText();

                if (nome.isBlank() && endereco.isBlank()) {
                throw new Exception("Altere pelo menos um dos campos.");
                }

                Pessoa p = new Pessoa(correntistaSelecionado.getCpfNumbers(), nome, endereco);
                this.cdao.update(p);

                this.labelSuccessError.setText("Correntista editado com sucesso!");
                this.labelSuccessError.setForeground(Color.green);

                this.cbCorrentista.recarregarDoBanco();
                this.cbCorrentista.carregarCorrentistas(p);

        } catch (Exception e) {
            this.labelSuccessError.setText("<html>Erro: " + e.getMessage() + "</html>");
            this.labelSuccessError.setForeground(Color.red);
        }
    }

    /**
     * Método responsável por realizar uma ação quando o combobox é alterado
     * 
     * @param evt - não utilizado
     */
    private void cbCorrentistaActionPerformed(@SuppressWarnings("unused") java.awt.event.ActionEvent evt) {
        Pessoa correntista = (Pessoa) this.cbCorrentista.getSelectedItem();

        if (correntista != null) {
            this.tfCPF.setText(correntista.getCpfFormatado());
            this.tfNome.setText(correntista.getNome());
            this.tfEndereco.setText(correntista.getEndereco());
        } else {
            this.tfCPF.setText("");
            this.tfNome.setText("");
            this.tfEndereco.setText("");
        }
    }
}
