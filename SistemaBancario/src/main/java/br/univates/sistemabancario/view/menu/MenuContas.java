package br.univates.sistemabancario.view.menu;

import java.util.ArrayList;

import javax.swing.SwingUtilities;

import br.univates.alexandria.view.MenuDialog;
import br.univates.sistemabancario.repository.ContaBancariaDAO;
import br.univates.sistemabancario.repository.CorrentistaDAO;
import br.univates.sistemabancario.service.ContaBancaria;

/**
     * Classe que representa o menu de contas para escolha do usuário
     * @author mateus.brambilla
     */
    public class MenuContas extends MenuDialog {
        private final ContaBancariaDAO cbdao;
        
        public MenuContas(ContaBancariaDAO cbdao, CorrentistaDAO cdao){
            super(null, true);

            this.cbdao = cbdao;
            
            setTitulo("Menu: Central de Contas");
            setSubtitulo("Listagem de contas bancárias");
            adicionarContas();
        }
        
        /**
         * Carrega as contas do DAO e as adiciona como opções no menu.
         */
        private void adicionarContas(){
            ArrayList<ContaBancaria> cbList = cbdao.readAll();
            
            for (ContaBancaria cb : cbList) {
                addOption(cb.toString(), () -> rodaMenuBanco(cb));
            }
            addDefaultLastOption();
        }
        
        /**
         * Recebe uma conta bancária e abre o MenuBanco para ela.
         * @param conta - conta bancária selecionada pelo usuário.
         */
        private void rodaMenuBanco(ContaBancaria conta) {
            if (conta != null) {
                SwingUtilities.invokeLater(() -> {
                    MenuBanco m = new MenuBanco(conta, this.cbdao);
                    m.exibir();
                });
                cbdao.update(conta);
            }
        }
    }
