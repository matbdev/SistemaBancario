package br.univates.alexandria.components.menu;

import java.awt.Color;
import java.awt.Component;

import javax.swing.Box;

/**
 * Parte do menu que corresponde ao conjunto de opções
 * @author mateus.brambilla
 */
public class MenuJPanel extends javax.swing.JPanel {
    public static final Color RED_COLOR_FOR_BUTTON = new Color(194, 72, 78);
    public static final Color BLUE_COLOR_FOR_BUTTON = new Color(48, 55, 156);
    public static final Color DISABLED_COLOR_FOR_BUTTON = new Color(41, 43, 92);

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
        ob.setBackground(c);
        addStyledButton(ob);
    }

    /**
     * Adiciona uma opção completa ao menu, associando uma ação a um texto.
     * Pode receber uma cor
     * @param ob - a opção já instanciada
     */
    public void addOption(OpcaoButton ob) {
        ob.setBackground(BLUE_COLOR_FOR_BUTTON);
        addStyledButton(ob);
    }

    /**
     * Adiciona uma opção a sua estrutura interna
     * @param button - opção do menu
     */
    private void addStyledButton(OpcaoButton button) {
        button.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        int buttonHeight = 30;
        button.setMaximumSize(new java.awt.Dimension(Short.MAX_VALUE, buttonHeight));
        button.setMinimumSize(new java.awt.Dimension(0, buttonHeight));
        button.setPreferredSize(new java.awt.Dimension(0, buttonHeight)); 

        add(button); // Adiciona o botão diretamente ao painel
        add(Box.createVerticalStrut(5)); // Adiciona espaçamento
    }
}
