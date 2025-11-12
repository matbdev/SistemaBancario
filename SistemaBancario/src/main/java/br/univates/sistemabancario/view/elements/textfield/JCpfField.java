package br.univates.sistemabancario.view.elements.textfield;

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
                if (!validarCPF(cpf)) setBorder(failBorder);
                else setBorder(successBorder);
            }
        });
    }
    
    /**
     * Método privado que realiza a validação do CPF
     * @param cpf - cpf em formato de string
     * @return booleano relativo à validade desse cpf
     */
    private boolean validarCPF(String cpf) {
        try{
            new CPF(cpf);
            return true;
        } catch (CpfInvalidoException e){
            return false;
        }
    }
}