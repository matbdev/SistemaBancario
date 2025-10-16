package br.univates.sistemabancario;

import com.formdev.flatlaf.FlatDarkLaf;

import br.univates.sistemabancario.view.TelaPrincipalNewMenu;

/**
 * Ponto de entrada da aplicação
 * @author mateus.brambilla
 */
public class App {
    public static void main(String[] args) {
        FlatDarkLaf.setup(); // Configuração e aplicação do FlatLaf

        TelaPrincipalNewMenu tp = new TelaPrincipalNewMenu();
        tp.iniciarMenuContas();
    }
}
