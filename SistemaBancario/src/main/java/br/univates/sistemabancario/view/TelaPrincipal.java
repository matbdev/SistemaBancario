package br.univates.sistemabancario.view;

import br.univates.alexandria.tools.Messages;

/**
 * Tela que serve para garantir estrutura da arquitetura do projeto
 * @author mateus.brambilla
 */
public class TelaPrincipal {
    public void iniciarMenuContas(){
        MenuContasCorrentistas m = new MenuContasCorrentistas();
        m.gerarMenu();
        Messages.infoMessage("Saindo da aplicação...");
    }
}
