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
    public static final Color RED_COLOR_FOR_BUTTON = new Color(194, 72, 78);
    public static final Color BLUE_COLOR_FOR_BUTTON = new Color(48, 55, 156);

    private JPanel buttonsPanel;
    private final ArrayList<OpcaoButton> opcoes = new ArrayList<>();

    /**
     * Construtor que não recebe nada mas seta um título padrão
     * @param parent - frame pai
     * @param modal - se é modal ou não
     */
    public MenuDialog(Frame parent, boolean modal){
        super(parent, modal);
        setTitle("Menu");
        initComponents();
    }
    
    /**
     * Construtor que recebe subtitulo recebe nada mas seta um título padrão
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
        opcoes.add(new OpcaoButton(text, acao));
    }

    /**
     * Adiciona uma opção completa ao menu, associando um ícone a um texto.
     * @param text O texto descritivo da opção.
     * @param acao A ação executada pela opção
     */
    public void addCustomLastOption(String text, Runnable acao) {
        OpcaoButton ob = new OpcaoButton(text, acao);
        ob.setBackground(RED_COLOR_FOR_BUTTON);
        opcoes.add(ob);
    }
    
    /**
    * Adiciona a opção final do menu, que por padrão é a de voltar.
    * Esta opção não terá um espaçamento após ela.
    */
   public void addLastOption() {
        OpcaoButton ob = new OpcaoButton("Voltar à página anterior", () -> this.dispose());
        ob.setBackground(RED_COLOR_FOR_BUTTON);

        opcoes.add(ob);
   }
    
    /**
     * Monta e retorna a string formatada do menu para exibição.
     */
    public void renderMenu(){
        buttonsPanel.removeAll(); // Limpa painel de botões
        
        // Itera sobre todas as opções do modelo para adicioná-las
        for (OpcaoButton ob : this.opcoes){
            OpcaoButton button = ob;
            
            button.setAlignmentX(Component.CENTER_ALIGNMENT); // Centraliza no BoxLayout
            
            int buttonHeight = 30;
            button.setMaximumSize(new java.awt.Dimension(Short.MAX_VALUE, buttonHeight));
            button.setMinimumSize(new java.awt.Dimension(0, buttonHeight));
            button.setPreferredSize(new java.awt.Dimension(0, buttonHeight)); 

            if (this.opcoes.indexOf(ob) < this.opcoes.size() - 1) {
                ob.setBackground(BLUE_COLOR_FOR_BUTTON);
            }

            // Adiciona o botão ao painel
            buttonsPanel.add(button);
            
            if (this.opcoes.indexOf(ob) < this.opcoes.size() - 1) {
                buttonsPanel.add(Box.createVerticalStrut(5));
            }
        }
        
        // Atualiza a visualização do painel
        buttonsPanel.revalidate();
        buttonsPanel.repaint();
    }
    
    /**
     * Método principal de exibição do menu
     */
    public void exibir() {
        renderMenu();
        this.pack();
        this.setLocationRelativeTo(null);
        this.setVisible(true);
    }

    private void initComponents() {
        titleLabel = new javax.swing.JLabel();
        buttonsPanel = new javax.swing.JPanel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        
        titleLabel.setFont(new java.awt.Font("Segoe UI", 1, 18)); 
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
