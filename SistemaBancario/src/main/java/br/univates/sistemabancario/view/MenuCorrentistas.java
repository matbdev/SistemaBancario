package br.univates.sistemabancario.view;

import br.univates.alexandria.models.Pessoa;
import br.univates.alexandria.tools.Messages;
import br.univates.alexandria.view.Menu;
import br.univates.sistemabancario.business.ContaBancaria;
import br.univates.sistemabancario.persist.ContaBancariaDAO;
import br.univates.sistemabancario.persist.CorrentistaDAO;
import java.util.ArrayList;
import javax.swing.*;


/**
 * Menu principal da aplicação
 * @author mateus.brambilla
 */
public class MenuCorrentistas extends Menu {
    private final CorrentistaDAO cdao = new CorrentistaDAO();
    private final ContaBancariaDAO cbdao = new ContaBancariaDAO();
    
    // Construtor
    public MenuCorrentistas(){
        setTitulo("Opções relacionadas à Correntista");
        setSubtitulo("== Escolha uma opção para modificar/adicionar cadastros de correntistas ==");
        adicionarOpcoes();
    }
    
    /**
     * Método privado responsável por adicionar as opções no menu
     */
    private void adicionarOpcoes(){
        addOption("Cadastrar Correntista", 'c', () -> cadastrarCorrentista());
        addOption("Editar Correntista", 'e', () -> editarCorrentista());
        addOption("Deletar Correntista", 'd', () -> deletarCorrentista());
        addLastOption();
    }
    
    /**
     * Método privado responsável por abrir um dialog de cadastro de correntista
     */
    private void cadastrarCorrentista(){
        JDialog dialog = new TelaCadastroCorrentista(null, new CorrentistaDAO());
        dialog.setVisible(true);
    }
    
    /**
     * Método que verifica se há algum correntista e, se houver, abre a tela de edição
     */
    private void editarCorrentista(){
        ArrayList<Pessoa> cList = cdao.readAll();
        
        if(cList.isEmpty()){
            Messages.infoMessage("Não há correntistas cadastrados");
        }else{
            TelaEditarUsuario teu = new TelaEditarUsuario(null, new CorrentistaDAO());
            teu.setVisible(true);
        }
    }
    
    /**
     * Método que faz algumas verificações das pessoas e, se houver alguma sem conta,
     * abre uma tela para deletar
     */
    private void deletarCorrentista(){
        ArrayList<Pessoa> cList = cdao.readAll();
        ArrayList<ContaBancaria> cbList = cbdao.readAll();
        ArrayList<Pessoa> filteredList = new ArrayList<>();
        
        if(cList.isEmpty()){
            Messages.infoMessage("Não há correntistas cadastrados");
        }else{
            for(Pessoa p : cList){
                boolean temConta = false;
                
                for(ContaBancaria cb : cbList){
                    if(cb.getPessoa() == p){
                        temConta = true;
                        break;
                    }
                }
                
                if(!temConta){
                    filteredList.add(p);
                }
            }
            
            if(filteredList.isEmpty()){
                Messages.infoMessage("Não há correntistas sem conta bancária cadastrada");
            }else{
                TelaDeletarUsuario tdu = new TelaDeletarUsuario(null, new CorrentistaDAO(), filteredList);
                tdu.setVisible(true);
            }
        }
    }
}
