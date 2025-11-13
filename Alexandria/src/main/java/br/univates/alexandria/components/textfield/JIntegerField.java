package br.univates.alexandria.components.textfield;

import javax.swing.JTextField;

public class JIntegerField extends JTextField {

    public JIntegerField() {
        this.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                char ch = evt.getKeyChar();
                
                if (!Character.isDigit(ch)) {
                    evt.consume();
                }
            }
        });
    }
    
    /**
     * Método que retorna o valor escrito no textfield, já com parse para int
     * @return - valor inteiro escrito
     */
    public int getInteger() {
        int retorno = 0;
        String aux = this.getText();
        if (!aux.isEmpty()) {
            retorno = Integer.parseInt(aux);
        }
        return retorno;
    }
    
    /**
     * Método que define um número para exibição
     * @param numero - o número a ser exibido
     */
    public void setInteger(int numero) {
        this.setText(String.valueOf(numero));
    }
}