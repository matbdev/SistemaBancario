package br.univates.sistemabancario.view.tela;

import br.univates.alexandria.models.Pessoa;
import br.univates.sistemabancario.repository.CorrentistaDAO;

import java.awt.Color;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;

/**
 * Dialog responsável pelo cadastro de correntistas
 * @author mateus.brambilla
 */
public class TelaCadastroCorrentista extends javax.swing.JDialog {
    private final CorrentistaDAO cdao;

    // Componentes
    private JButton botaoCadastro;
    private JLabel labelCpf;
    private JLabel labelEndereco;
    private JLabel labelNome;
    private JLabel labelSuccessError;
    private JLabel labelTitulo;
    private JTextField tfCpf;
    private JTextField tfEndereco;
    private JTextField tfNome;

    /**
     * Creates new form FrameCadastroCorrentista
     * @param parent - um JFrame
     * @param cdao - persistência para correntista
     */
    public TelaCadastroCorrentista(JFrame parent, CorrentistaDAO cdao) {
        super(parent, "Cadastro de Correntista", true); // <- true = modal
        this.cdao = cdao;
        initComponents();
        setLocationRelativeTo(parent);
    }

    /**
     * Método que inicializa os componentes da tela
     */
    private void initComponents() {

        labelNome = new javax.swing.JLabel();
        botaoCadastro = new javax.swing.JButton();
        tfNome = new javax.swing.JTextField();
        labelTitulo = new javax.swing.JLabel();
        tfCpf = new javax.swing.JTextField();
        labelCpf = new javax.swing.JLabel();
        labelEndereco = new javax.swing.JLabel();
        tfEndereco = new javax.swing.JTextField();
        labelSuccessError = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Correntistas");
        setAlwaysOnTop(true);
        setBackground(new java.awt.Color(51, 51, 51));
        setBounds(new java.awt.Rectangle(0, 0, 0, 0));
        setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));

        labelNome.setLabelFor(tfNome);
        labelNome.setText("Informe o nome do correntista:");

        botaoCadastro.setBackground(new java.awt.Color(0, 0, 51));
        botaoCadastro.setForeground(new java.awt.Color(255, 255, 255));
        botaoCadastro.setText("Cadastrar");
        botaoCadastro.setAutoscrolls(true);
        botaoCadastro.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botaoCadastroActionPerformed(evt);
            }
        });

        labelTitulo.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        labelTitulo.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        labelTitulo.setText("Cadastro de Correntista");

        labelCpf.setLabelFor(tfCpf);
        labelCpf.setText("Informe o CPF do correntista:");

        labelEndereco.setLabelFor(tfEndereco);
        labelEndereco.setText("Informe o endereço do correntista:");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(labelTitulo, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(labelNome, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(labelEndereco, javax.swing.GroupLayout.PREFERRED_SIZE, 370, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                .addGroup(layout.createSequentialGroup()
                                    .addComponent(labelSuccessError, javax.swing.GroupLayout.PREFERRED_SIZE, 272, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGap(18, 18, 18)
                                    .addComponent(botaoCadastro))
                                .addComponent(labelCpf, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(tfCpf)
                                .addComponent(tfEndereco)))
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addComponent(tfNome, javax.swing.GroupLayout.Alignment.LEADING))
                .addGap(18, 18, 18))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addComponent(labelTitulo)
                .addGap(12, 12, 12)
                .addComponent(labelNome)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(tfNome, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(labelCpf)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(tfCpf, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(labelEndereco)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(tfEndereco, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(labelSuccessError, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGap(15, 15, 15))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 29, Short.MAX_VALUE)
                        .addComponent(botaoCadastro)
                        .addGap(24, 24, 24))))
        );

        pack();
    }

    /**
     * Método que realiza o cadastro de um correntista quando o botão é pressionado
     * @param evt - não utilizado
     */
    private void botaoCadastroActionPerformed(java.awt.event.ActionEvent evt) {
        try{
            String nome = this.tfNome.getText();
            String cpf = this.tfCpf.getText();
            String endereco = this.tfEndereco.getText();
            
            Pessoa pessoa = new Pessoa(cpf, nome, endereco);
            cdao.create(pessoa);
            
            this.labelSuccessError.setText("Correntista adicionado com sucesso!");
            this.labelSuccessError.setForeground(Color.green);
            
            this.tfNome.setText("");
            this.tfCpf.setText("");
            this.tfEndereco.setText("");
        }catch(Exception e){
            // Html para quebra de linha
            this.labelSuccessError.setText("<html>Erro: " + e.getMessage() + "</html>");
            this.labelSuccessError.setForeground(Color.red);
        }
    }
}
