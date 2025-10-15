package br.univates.sistemabancario.view;

import br.univates.alexandria.view.Menu;
import br.univates.sistemabancario.repository.ContaBancariaDAO;
import br.univates.sistemabancario.repository.CorrentistaDAO;

/**
 * Menu que separa as funções de correntista e conta bancária
 * @author mateus.brambilla
 */
public class MenuContasCorrentistas extends Menu {
    private final CorrentistaDAO cdao;
    private final ContaBancariaDAO cbdao;
    /**
     * Construtor que inicializa a classe
     * Recebe objetos dos DAOs para preservar itens estáticos (arquivo)
     */
    public MenuContasCorrentistas(CorrentistaDAO cdao, ContaBancariaDAO cbdao){
        this.cdao = cdao;
        this.cbdao = cbdao;
        
        setTitulo("Escolha uma opção");
        setSubtitulo("== Escolha entre manusear correntistas ou contas bancárias ==");
        adicionarOpcoes();
    }
    
    /**
     * Método privado que adiciona as opções a este menu
     */
    private void adicionarOpcoes(){
        addOption("Correntistas", 'c', () -> {
            MenuCorrentistas mc = new MenuCorrentistas(this.cdao, this.cbdao);
            mc.gerarMenu();
        });
        addOption("Contas Bancárias", 'b', () -> {
            MenuContasBancarias mcb = new MenuContasBancarias(this.cdao, this.cbdao);
            mcb.gerarMenu();
        });
        addLastOption();
    }
}
