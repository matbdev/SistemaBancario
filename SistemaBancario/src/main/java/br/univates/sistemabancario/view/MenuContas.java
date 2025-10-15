package br.univates.sistemabancario.view;

import br.univates.alexandria.tools.Messages;
import br.univates.alexandria.view.Menu;
import br.univates.sistemabancario.business.ContaBancaria;
import br.univates.sistemabancario.persist.ContaBancariaDAO;
import java.util.ArrayList;

/**
 * Classe que representa o menu de contas para escolha do usuário
 * @author mateus.brambilla
 */
public class MenuContas extends Menu {
    private final ContaBancariaDAO cbdao = new ContaBancariaDAO();
    
    public MenuContas(){
        setTitulo("Escolha uma conta");
        setSubtitulo("== Escolha uma conta disponível para realizar movimentações ou consultar extrato ==");
        adicionarContas();
    }
    
    /**
     * Carrega as contas do DAO e as adiciona como opções no menu.
     */
    public void adicionarContas(){
        ArrayList<ContaBancaria> cbList = cbdao.readAll();
        
        for (ContaBancaria cb : cbList) {
            char icone = (char)(cbList.indexOf(cb) + '1');
            addOption(cb.toString(), icone, () -> rodaMenuBanco(cb));
        }
        addLastOption();
    }
    
    /**
     * Recebe uma conta bancária e abre o MenuBanco para ela.
     * @param conta - conta bancária selecionada pelo usuário.
     */
    public void rodaMenuBanco(ContaBancaria conta) {
        if (conta != null) {
            MenuBanco m = new MenuBanco(conta);
            Messages.infoMessage("Conta escolhida com sucesso!");
            m.verificarStatus();
            m.gerarMenu();
            cbdao.update(conta);
            Messages.infoMessage("Alterações salvas no arquivo.");
        }
    }
}
