package br.univates.sistemabancario;

import com.formdev.flatlaf.FlatDarkLaf;

import br.univates.sistemabancario.controller.FramePrincipalController;
import br.univates.sistemabancario.view.tela.navegacao.FramePrincipal;

/**
 * Ponto de entrada da aplicação
 * @author mateus.brambilla
 */
public class App {
    public static void main(String[] args) {
        FlatDarkLaf.setup();

        java.awt.EventQueue.invokeLater(() -> {
            FramePrincipal frame = new FramePrincipal();
            new FramePrincipalController(frame);
            
            frame.pack();
            
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });
    }
}