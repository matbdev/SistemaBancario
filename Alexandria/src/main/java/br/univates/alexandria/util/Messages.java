package br.univates.alexandria.util;

import java.awt.Component;
import javax.swing.JOptionPane;

/**
 * Método que fornece uma mensagem pronta de erro
 * @author mateus.brambilla
 */
public class Messages {

    /**
     * Mensagem de erro - pode receber duas string
     * @param message - mensagem do erro
     * @param title - título do erro
     */
    public static void errorMessage(String message, String title){
        JOptionPane.showMessageDialog(
            null, 
            message, 
            title,
            JOptionPane.ERROR_MESSAGE
        );
    }
    
    /**
     * Mensagem de erro - pode receber uma string
     * @param message - mensagem do erro
     */
    public static void errorMessage(String message){
        JOptionPane.showMessageDialog(
            null, 
            message, 
            "ERRO",
            JOptionPane.ERROR_MESSAGE
        );
    }

    /**
     * Mensagem de erro - recebe uma exception
     * @param e - exception
     */
    public static void errorMessage(Exception e){
        JOptionPane.showMessageDialog(
            null, 
            e.getMessage(), 
            "Atenção", 
            JOptionPane.ERROR_MESSAGE
        );
    }
    
    /**
     * Mensagem de informação - recebe um texto
     * @param text - texto a ser exibido
     */
    public static void infoMessage(String text){
        JOptionPane.showMessageDialog(
            null, 
            text,
            "INFORMAÇÃO", 
            JOptionPane.INFORMATION_MESSAGE
        );
    }
    
    /**
     * Mostra uma mensagem de informação relativa a um componente "pai".
     * @param parent - componente pai
     * @param text - mensagem a ser exibida.
     */
    public static void infoMessage(Component parent, String text) {
        JOptionPane.showMessageDialog(
                parent,
                text,
                "INFORMAÇÃO",
                JOptionPane.INFORMATION_MESSAGE
        );
    }
    
    /**
     * Mostra uma mensagem de sucesso relativa a um componente "pai".
     * @param parent - componente pai
     * @param text - mensagem a ser exibida.
     */
    public static void sucessMessage(Component parent, String text) {
        JOptionPane.showMessageDialog(
            parent,
            text,
            "Sucesso",
            JOptionPane.INFORMATION_MESSAGE
        );
    }
    
    /**
     * Mostra uma mensagem de falha relativa a um componente "pai".
     * @param parent - componente pai
     * @param text - mensagem a ser exibida.
     */
    public static void errorMessage(Component parent, String text) {
        JOptionPane.showMessageDialog(
            parent,
            text,
            "Erro",
            JOptionPane.ERROR_MESSAGE
        );
    }
}
