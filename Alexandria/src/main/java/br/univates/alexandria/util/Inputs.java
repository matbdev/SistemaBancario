package br.univates.alexandria.util;

import javax.swing.JOptionPane;
import br.univates.alexandria.exceptions.NullInputException;

/**
 * Classe similar ao Entrada
 * @author mateus.brambilla
 */
public class Inputs {
    /**
     * Recebe textos para a janela, tenta converter o input para double
     * @param mensagem - mensagem da janela
     * @param titulo - titulo da janela
     * @return - quantidade em double
     * @throws NullInputException - em caso de operação cancelada
     */
    public static double Double(String mensagem, String titulo) throws NullInputException { // 1. Avise que o método pode lançar a exceção
        while (true) {
            String qtde = JOptionPane.showInputDialog(
                    null,
                    mensagem,
                    titulo,
                    JOptionPane.QUESTION_MESSAGE
            );

            // Checa se o usuário cancelou (X ou Cancelar)
            if (qtde == null) {
                throw new NullInputException("Operação cancelada pelo usuário.");
            }

            try {
                String valorNormalizado = qtde.trim().replace(',', '.');
                double dQtde = Double.parseDouble(valorNormalizado);
                return dQtde; // Retorna apenas em caso de sucesso

            } catch (NumberFormatException e) { 
                Messages.errorMessage("Valor inválido. Por favor, digite um número válido.");
            }
        }
    }
    
    /**
     * Recebe textos para a janela
     * @param mensagem - mensagem da janela
     * @param titulo - titulo da janela
     * @return - quantidade em double
     */
    public static String String(String mensagem, String titulo) {
        while (true) {
            String text = JOptionPane.showInputDialog(
                    null,
                    mensagem,
                    titulo,
                    JOptionPane.QUESTION_MESSAGE
            );

            // Checa se o usuário cancelou (X ou Cancelar)
            if (text == null) {
                throw new NullInputException("Operação cancelada pelo usuário.");
            }

            return text;
        }
    }
}
