package br.univates.sistemabancario.view.elements.textfield;

import java.awt.Color;
import java.text.ParseException;

import javax.swing.BorderFactory;
import javax.swing.JFormattedTextField;
import javax.swing.border.Border;

import java.time.DateTimeException;
import java.time.LocalDate;

/**
 * Field especializado para entrada de datas
 */
public class JDateField extends JFormattedTextField {

    public JDateField() throws ParseException {
        this.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.MaskFormatter("##/##/####")));

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

                String dataStr = getText();
        
                // Valida a data
                if (!validarData(dataStr)) setBorder(failBorder);
                else setBorder(successBorder);
            }
        });
    }
    
    /**
     * Método privado que realiza a validação da data
     * @param dataStr - data em formato de string
     * @return booleano relativo à validade dessa data
     */
    private boolean validarData(String dataStr) {
        try{
            String[] partesData = dataStr.split("/");
            String diaStr = partesData[0];
            String mesStr = partesData[1];
            String anoStr = partesData[2];

            int dia = Integer.parseInt(diaStr);
            int mes = Integer.parseInt(mesStr);
            int ano = Integer.parseInt(anoStr);

            // Lança exception em caso de data inválida
            LocalDate.of(ano, mes, dia);
            return true;
        } catch (DateTimeException | NumberFormatException e){
            return false;
        } catch (Exception e) {
            return false;
        }
    }
}