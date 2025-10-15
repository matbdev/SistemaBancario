package br.univates.sistemabancario;

import br.univates.sistemabancario.view.TelaPrincipal;

/**
 * Ponto de entrada da aplicação
 * @author mateu
 */
public class App {
    public static void main(String[] args) {
        TelaPrincipal tp = new TelaPrincipal();
        tp.iniciarMenuContas();
    }
}
