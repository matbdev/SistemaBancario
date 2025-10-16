package br.univates.alexandria.view;

import java.awt.Color;
import java.awt.Component;
import java.awt.Frame;
import java.util.ArrayList;

import javax.swing.Box;
import javax.swing.JPanel;

import br.univates.alexandria.models.OpcaoButton;
import br.univates.alexandria.util.FormatadorTexto;
import br.univates.alexandria.util.Verificador;

/**
 * MenuDialog com implementação para GUI (JDialog)
 * @author mateus.brambilla
 */
public class MenuDialog extends javax.swing.JDialog {
    private MenuJPanel buttonsPanel = new MenuJPanel();

    /**
     * Construtor que recebe o frame pai e se é modal ou não
     * @param parent - frame pai
     * @param modal - se é modal ou não
     */
    public MenuDialog(Frame parent, boolean modal){
        super(parent, modal);
        setTitle("Menu");
        initComponents();
    }
    
    /**
     * Construtor que recebe o frame pai e se é modal ou não
     * Também recebe um subtítulo
     * @param parent - frame pai
     * @param modal - se é modal ou não
     * @param subtitulo - texto adicional
     */
    public MenuDialog(Frame parent, boolean modal, String subtitulo){
        this(parent, modal); // Chama o construtor padrão
        setSubtitulo(subtitulo);
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
     */
    public void addCustomLastOption(String text, Runnable acao) {
        buttonsPanel.addOption(new OpcaoButton(text, acao), MenuJPanel.RED_COLOR_FOR_BUTTON);
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
        buttonsPanel.renderMenu();
        this.pack();
        this.setLocationRelativeTo(null);
        this.setVisible(true);
    }

    private void initComponents() {
        titleLabel = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        
        titleLabel.setFont(new java.awt.Font("Segoe UI", 1, 16)); 
        titleLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        titleLabel.setText("Menu Title"); 
        
        titleLabel.setBorder(javax.swing.BorderFactory.createEmptyBorder(10, 10, 10, 10));

        getContentPane().add(titleLabel, java.awt.BorderLayout.NORTH);

        buttonsPanel.setLayout(new javax.swing.BoxLayout(buttonsPanel, javax.swing.BoxLayout.Y_AXIS));
        buttonsPanel.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 15, 15, 15));
        
        getContentPane().add(buttonsPanel, java.awt.BorderLayout.CENTER);

        setMinimumSize(new java.awt.Dimension(300, 200));
    }

    private javax.swing.JLabel titleLabel;
}
