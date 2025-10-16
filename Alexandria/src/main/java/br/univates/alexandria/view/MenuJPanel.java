package br.univates.alexandria.view;

import java.awt.Color;
import java.awt.Component;
import java.util.ArrayList;

import javax.swing.Box;

import br.univates.alexandria.models.OpcaoButton;

/**
 * Parte do menu que corresponde ao conjunto de opções
 * @author mateus.brambilla
 */
public class MenuJPanel extends javax.swing.JPanel {
    public static final Color RED_COLOR_FOR_BUTTON = new Color(194, 72, 78);
    public static final Color BLUE_COLOR_FOR_BUTTON = new Color(48, 55, 156);

    private final ArrayList<OpcaoButton> opcoes = new ArrayList<>();

    /**
     * Construtor que inicializa
     */
    public MenuJPanel(){}

    /**
     * Adiciona uma opção completa ao menu, associando uma ação a um texto.
     * Pode receber uma cor
     * @param ob - a opção já instanciada
     */
    public void addOption(OpcaoButton ob, Color c) {
        opcoes.add(ob);
        ob.setBackground(c);
    }

    /**
     * Adiciona uma opção completa ao menu, associando uma ação a um texto.
     * Pode receber uma cor
     * @param ob - a opção já instanciada
     */
    public void addOption(OpcaoButton ob) {
        opcoes.add(ob);
        ob.setBackground(BLUE_COLOR_FOR_BUTTON);
    }
    
    /**
     * Monta e retorna a sequência de botões que representam o menu
     */
    public void renderMenu(){
        removeAll(); // Limpa painel de botões
        
        // Itera sobre todas as opções do modelo para adicioná-las
        for (OpcaoButton ob : this.opcoes){
            OpcaoButton button = ob;
            
            button.setAlignmentX(Component.CENTER_ALIGNMENT); // Centraliza no BoxLayout
            
            int buttonHeight = 30;
            button.setMaximumSize(new java.awt.Dimension(Short.MAX_VALUE, buttonHeight));
            button.setMinimumSize(new java.awt.Dimension(0, buttonHeight));
            button.setPreferredSize(new java.awt.Dimension(0, buttonHeight)); 

            // Adiciona o botão ao painel
            add(button);
            
            if (this.opcoes.indexOf(ob) < this.opcoes.size() - 1) {
                add(Box.createVerticalStrut(5));
            }
        }
        
        // Atualiza a visualização do painel
        revalidate();
        repaint();
    }
}
