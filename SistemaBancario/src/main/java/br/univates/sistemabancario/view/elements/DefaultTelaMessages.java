package br.univates.sistemabancario.view.elements;

import br.univates.alexandria.util.Messages;
import javax.swing.JPanel;

/**
 * Classe a ser herdada para mensagens na tela
 * @author mateus.brambilla
 */
public abstract class DefaultTelaMessages extends JPanel {
    // Métodos públicos para o Controller exibir mensagens.
    public void exibirErro(String mensagem) {
        Messages.errorMessage(this, mensagem);
    }
    
    public void exibirSucesso(String mensagem) {
        Messages.sucessMessage(this, mensagem);
    }
}
