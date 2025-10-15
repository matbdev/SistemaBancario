package br.univates.sistemabancario.view;

import br.univates.alexandria.util.Messages;
import br.univates.alexandria.view.Menu;
import br.univates.sistemabancario.repository.ContaBancariaDAO;
import br.univates.sistemabancario.repository.CorrentistaDAO;

import javax.swing.*;


/**
 * Menu principal da aplicação
 * @author mateus.brambilla
 */
public class MenuContasBancarias extends Menu {
    private final CorrentistaDAO cdao;
    private final ContaBancariaDAO cbdao;
    
    // Construtor
    public MenuContasBancarias(CorrentistaDAO cdao, ContaBancariaDAO cbdao){
        this.cdao = cdao;
        this.cbdao = cbdao;

        setTitulo("Opções relacionadas à Conta Bancária");
        setSubtitulo("== Escolha uma opção para modificar/adicionar cadastros de contas bancárias ==");
        adicionarOpcoes();
    }
    
    /**
     * Método privado responsável por adicionar as opções no menu
     */
    private void adicionarOpcoes(){
        addOption("Cadastrar Conta Bancária", 'c', () -> cadastrarContaBancaria());
        addOption("Manusear Contas Bancárias", 'm', () -> manusearContas());
        addLastOption();
    }
    
    /**
     * Método privado responsável por abrir um dialog de cadastro de conta
     * Só abre se houver um correntista
     */
    private void cadastrarContaBancaria(){
        if(cdao.readAll().isEmpty()){
            Messages.infoMessage("Não há correntistas cadastrados");
        }else{
            JDialog dialog = new TelaCadastroConta(null, this.cdao, this.cbdao);
            dialog.setVisible(true);
        }
    }
    
    /**
     * Método privado responsável por abrir o menu de controle de contas
     * Só abre se houver uma conta cadastrada
     */
    private void manusearContas(){
        if(cbdao.readAll().isEmpty()){
            Messages.infoMessage("Não há contas bancárias cadastradas");
        }else{
            MenuContas mc = new MenuContas(this.cbdao);
            mc.gerarMenu();
        }
    }
}
