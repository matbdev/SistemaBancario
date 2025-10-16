package br.univates.sistemabancario.view.menu;

import javax.swing.SwingUtilities;

import br.univates.alexandria.view.MenuDialog;
import br.univates.sistemabancario.repository.ContaBancariaDAO;
import br.univates.sistemabancario.repository.CorrentistaDAO;

/**
     * Menu que separa as funções de correntista e conta bancária
     * @author mateus.brambilla
     */
    public class MenuContasCorrentistas extends MenuDialog {
        private final CorrentistaDAO cdao;
        private final ContaBancariaDAO cbdao;

        /**
         * Construtor que inicializa a classe
         * Recebe objetos dos DAOs para preservar itens estáticos (arquivo)
         */
        public MenuContasCorrentistas(CorrentistaDAO cdao, ContaBancariaDAO cbdao){
            super(null, true);

            this.cdao = cdao;
            this.cbdao = cbdao;

            setTitulo("Menu: Fluxos");
            setSubtitulo("Escolha entre manusear correntistas ou contas");
            
            adicionarOpcoes();
        }
        
        /**
         * Método privado que adiciona as opções a este menu
         */
        private void adicionarOpcoes(){
            addOption("Correntistas", () -> {
                SwingUtilities.invokeLater(() -> {
                    MenuCorrentistas mc = new MenuCorrentistas(this.cdao, this.cbdao);
                    mc.exibir();
                });
            });
            addOption("Contas Bancárias", () -> {
                SwingUtilities.invokeLater(() -> {
                    MenuContasBancarias mcb = new MenuContasBancarias(this.cdao, this.cbdao);
                    mcb.exibir();
                });
            });
            addCustomLastOption("Encerrar o programa", () -> this.dispose());
        }
    }
