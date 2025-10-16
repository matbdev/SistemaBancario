package br.univates.sistemabancario.view.tela;

import br.univates.alexandria.models.Pessoa;
import br.univates.alexandria.util.Messages;
import br.univates.sistemabancario.repository.CorrentistaDAO;
import br.univates.sistemabancario.view.elements.PessoaComboBox;

import java.awt.Color;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;

/**
 * Tela responsável por editar um correntista
 * @author mateus.brambilla
 */
public class TelaDeletarUsuario extends javax.swing.JDialog {
    private final CorrentistaDAO cdao;

    // Componentes
    private JButton botaoCadastro;
    private JLabel labelCorrentista;
    private JLabel labelSuccessError;
    private JLabel labelTitulo;
    private PessoaComboBox cbCorrentista;
    
    /**
     * Creates new form FrameCadastroCorrentista
     * @param parent - um JFrame
     * @param cdao - instancia de CorrentistaDAO
     * @param al - arraylist com pessoas sem contas
     */
    public TelaDeletarUsuario(JFrame parent, CorrentistaDAO cdao, ArrayList<Pessoa> al) {
        super(parent, "Cadastro de Conta Bancária", true); 
        this.cdao = cdao;

        initComponents(al);
        setLocationRelativeTo(parent);
    }

    /**
     * Método que inicializa os componentes da tela
     * @param al - lista de correntista para combobox de pessoa
     */
    private void initComponents(ArrayList<Pessoa> al) {
        labelCorrentista = new javax.swing.JLabel();
        botaoCadastro = new javax.swing.JButton();
        labelTitulo = new javax.swing.JLabel();
        labelSuccessError = new javax.swing.JLabel();
        cbCorrentista = new PessoaComboBox(al);

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Correntistas");
        setAlwaysOnTop(true);
        setBackground(new java.awt.Color(51, 51, 51));
        setBounds(new java.awt.Rectangle(0, 0, 0, 0));
        setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));

        labelCorrentista.setText("Informe o correntista:");

        botaoCadastro.setBackground(new java.awt.Color(0, 0, 51));
        botaoCadastro.setForeground(new java.awt.Color(255, 255, 255));
        botaoCadastro.setText("Deletar");
        botaoCadastro.setAutoscrolls(true);
        botaoCadastro.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botaoCadastroActionPerformed(evt);
            }
        });

        labelTitulo.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        labelTitulo.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        labelTitulo.setText("Remoção de Correntista");

        cbCorrentista.setAutoscrolls(true);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(labelTitulo, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 406, Short.MAX_VALUE)
            .addGroup(layout.createSequentialGroup()
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(labelSuccessError, javax.swing.GroupLayout.PREFERRED_SIZE, 272, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(botaoCadastro)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(cbCorrentista, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(labelCorrentista, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGap(18, 18, 18))))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap(12, Short.MAX_VALUE)
                .addComponent(labelTitulo)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(labelCorrentista)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(cbCorrentista, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(32, 32, 32)
                        .addComponent(botaoCadastro))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(18, 18, 18)
                        .addComponent(labelSuccessError, javax.swing.GroupLayout.PREFERRED_SIZE, 46, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(15, 15, 15))
        );

        getAccessibleContext().setAccessibleName("Contas Bancárias");

        pack();
    }

    /**
     * Método que realiza a exclusão do correntista quando o botão é pressionado
     * @param evt - não utilizado
     */
    private void botaoCadastroActionPerformed(java.awt.event.ActionEvent evt) {
        try{
            Pessoa correntista = (Pessoa) this.cbCorrentista.getSelectedItem();
            if (correntista == null) {
                throw new Exception("Por favor, selecione um correntista.");
            }
            
            this.cdao.delete(correntista);
            this.cbCorrentista.deletar(correntista);
            
            this.labelSuccessError.setText("Correntista deletado com sucesso!");
            this.labelSuccessError.setForeground(Color.green);
            
            this.cbCorrentista.carregarCorrentistas();
            
            if(this.cbCorrentista.getTamanho() == 0){
                Messages.infoMessage(this, "Não há mais usuários sem conta cadastrada");
                dispose();
            }

        }catch(Exception e){
            // Html para quebra de linha
            this.labelSuccessError.setText("<html>Erro: " + e.getMessage() + "</html>");
            this.labelSuccessError.setForeground(Color.red);
        }
    }
}
