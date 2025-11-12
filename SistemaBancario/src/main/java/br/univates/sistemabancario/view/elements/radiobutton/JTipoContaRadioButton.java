package br.univates.sistemabancario.view.elements.radiobutton;

import javax.swing.ButtonGroup;
import javax.swing.JRadioButton;

public class JTipoContaRadioButton extends ButtonGroup {
    private final String[] opcoes = {"Conta Normal", "Conta Especial"};
    private final JRadioButton[] buttons = new JRadioButton[opcoes.length];

    public JTipoContaRadioButton(){
        initButtons();
    }

    /**
     * Método que inicializa os botões
     */
    private void initButtons() {
        // Se baseia na lista
        for (int i = 0; i < opcoes.length; i++) {
            buttons[i] = new JRadioButton(opcoes[i]);
            add(buttons[i]);
        }

        // Primeiro item vem selecionado
        buttons[0].setSelected(true);
    }
}
