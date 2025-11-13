package br.univates.alexandria.components.textfield;

import java.awt.Color;
import java.text.ParseException;

import javax.swing.BorderFactory;
import javax.swing.JFormattedTextField;
import javax.swing.border.Border;

import br.univates.alexandria.exceptions.CpfInvalidoException;
import br.univates.alexandria.models.CPF;

public class JCpfField extends JFormattedTextField {

    public JCpfField() throws ParseException {
        this.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.MaskFormatter("###.###.###-##")));

        this.addFocusListener(new java.awt.event.FocusAdapter() {
            // Quando ganha foco
            public void focusGained(java.awt.event.FocusEvent evt) {
                Border focusBorder = BorderFactory.createLineBorder(Color.CYAN);
                setBorder(focusBorder);
            }

            // Quando perde foco
            public void focusLost(java.awt.event.FocusEvent evt) {
                Border successBorder = BorderFactory.createLineBorder(Color.GREEN);
                Border failBorder = BorderFactory.createLineBorder(Color.RED);

                String cpf = getText();
        
                // Valida o CPF
                if (getCpf() == null) setBorder(failBorder);
                else setBorder(successBorder);
            }
        });
    }
    
    /**
     * Método que remove os focos
     * Necessário para melhorar experiência do usuário na minha tela de edição
     */
    public void removeFocus() {
        for (java.awt.event.FocusListener listener : getFocusListeners()) {
            removeFocusListener(listener);
        }
    }
    
    /**
     * Método auxiliar que já retorna um cpf instanciado
     * @return - objeto instanciado de cpf ou nulo
     */
    public CPF getCpf() {
        try{
            return new CPF(this.getText());
        } catch (CpfInvalidoException e){
            return null;
        }
    }
    
    /**
     * Método que escreve um cpf
     * @param cpf - objeto instanciado de cpf
     */
    public void setCpf(CPF cpf) {
        setText(cpf.getCpf());
    }
}