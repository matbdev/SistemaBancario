package br.univates.alexandria.components.textfield;

import java.awt.Color;
import java.text.ParseException;

import javax.swing.BorderFactory;
import javax.swing.JFormattedTextField;
import javax.swing.border.Border;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.format.ResolverStyle;

/**
 * Field especializado para entrada de datas
 */
public class JDateField extends JFormattedTextField {
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/uuuu").withResolverStyle(ResolverStyle.STRICT);

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
        
                // Valida a data
                if (getLocalDate() == null) setBorder(failBorder);
                else setBorder(successBorder);
            }
        });
    }
    
    /**
     * Tenta converter o texto atual em um LocalDate.
     * @return - a data se não der erro, ou nulo
     */
    public LocalDate getLocalDate() {
        String dataStr = getText().trim().replace(" ", "");

        try {
            LocalDate date = LocalDate.parse(dataStr, DATE_FORMATTER);
            return date;
        } catch (DateTimeParseException e) {
            // Ignora o erro e retorna nulo
        }

        return null;
    }

    /**
     * Define o valor do campo usando um objeto LocalDate.
     * @param date A data para definir, ou null para limpar o campo.
     */
    public void setLocalDate(LocalDate date) {
        if (date != null) {
            this.setText(date.format(DATE_FORMATTER));
        } else {
            this.setValue(null);
        }
    }
}