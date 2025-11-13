package br.univates.alexandria.components;

import br.univates.alexandria.util.Messages;

import java.awt.event.ActionListener;

import javax.swing.JPanel;

/**
 * Classe a ser herdada para mensagens na tela
 * @author mateus.brambilla
 */
public abstract class JFrameUtilsAdapter extends JPanel implements JFrameUtilsInterface {
    /**
     * {@inheritDoc}
     */
    @Override
    public void exibirErro(String mensagem) {
        Messages.errorMessage(this, mensagem);
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public void exibirSucesso(String mensagem) {
        Messages.sucessMessage(this, mensagem);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void adicionarAcaoBotao(ActionListener listener) {}
}
