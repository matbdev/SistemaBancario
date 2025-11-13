package br.univates.alexandria.components.textfield;

import javax.swing.JTextField;

/**
 * Text field que não aceita números - para nomes
 */
public class JOnlyTextField extends JTextField {

    public JOnlyTextField() {
        this.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                char ch = evt.getKeyChar();
                
                if (Character.isDigit(ch)) {
                    evt.consume();
                }
            }
        });
    }
}