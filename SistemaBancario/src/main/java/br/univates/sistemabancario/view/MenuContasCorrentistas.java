package br.univates.sistemabancario.view;

import br.univates.alexandria.view.Menu;

/**
 * Menu que separa as funções de correntista e conta bancária
 * @author mateus.brambilla
 */
public class MenuContasCorrentistas extends Menu {
    /**
     * Construtor que inicializa a classe
     */
    public MenuContasCorrentistas(){
        setTitulo("Escolha uma opção");
        setSubtitulo("== Escolha entre manusear correntistas ou contas bancárias ==");
        adicionarOpcoes();
    }
    
    /**
     * Método privado que adiciona as opções a este menu
     */
    private void adicionarOpcoes(){
        addOption("Correntistas", 'c', () -> {
            MenuCorrentistas mc = new MenuCorrentistas();
            mc.gerarMenu();
        });
        addOption("Contas Bancárias", 'b', () -> {
            MenuContasBancarias mcb = new MenuContasBancarias();
            mcb.gerarMenu();
        });
        addLastOption();
    }
}
