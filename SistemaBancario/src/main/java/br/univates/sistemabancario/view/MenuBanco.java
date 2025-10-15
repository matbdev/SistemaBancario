package br.univates.sistemabancario.view;

import br.univates.alexandria.exceptions.NullInputException;
import br.univates.alexandria.util.Inputs;
import br.univates.alexandria.util.Messages;
import br.univates.alexandria.view.Menu;
import br.univates.sistemabancario.exceptions.SaldoInvalidoException;
import br.univates.sistemabancario.repository.ContaBancariaDAO;
import br.univates.sistemabancario.repository.TransacaoDAO;
import br.univates.sistemabancario.service.ContaBancaria;
import br.univates.sistemabancario.service.Transacao;

import java.util.ArrayList;

/**
 * Classe que herda de menu, representando a view do nosso banco
 * @author mateus.brambilla
 */
public class MenuBanco extends Menu {
    private final ContaBancaria cb;
    private final TransacaoDAO tdao = new TransacaoDAO();
    private final ContaBancariaDAO cbdao;
    
    /**
     * Construtor que recebe cpf, nome, endereco e saldo e instancia classe
     * @param cb - conta bancária já instanciada
     */
    public MenuBanco(ContaBancaria cb, ContaBancariaDAO cbdao){
        this.cb = cb;
        this.cbdao = cbdao;
        setTitulo("Escolha uma operação");
        setSubtitulo("== Escolha uma operação disponível para realizar na conta selecionada ==");
        adicionarOpcoes();
    }
    
    // Getter
    public ContaBancaria getContaBancaria(){
        return this.cb;
    }
    
    /**
     * Método auxilair que adiciona as opções ao menu
     */
    private void adicionarOpcoes(){
        addOption("Depositar valor", 'd', () -> depositarValor());
        addOption("Sacar valor", 's', () -> sacarValor());
        addOption("Verificar status", 'v', () -> verificarStatus());
        addOption("Consultar logs", 'l', () -> consultarLogs());
        addLastOption();
    }
    
    /**
     * Método que realiza um depósito de modo totalmente autônomo
     */
    private void depositarValor(){
        try{
            double dQtde = Inputs.Double(
                    "Infome a quantidade a ser depositada",
                    "DEPÓSITO"
            );
            
            this.cb.depositaValor(dQtde);
            this.tdao.create(
                    new Transacao(
                            dQtde, 
                            Transacao.DEFAULT_DESC, 
                            Transacao.MOV_CREDITO, 
                            this.cb.getNumeroConta()
                    )
            );
            this.cbdao.update(this.cb); // atualiza conta no arquivo

            verificarStatus();
                    
        }catch(SaldoInvalidoException e){
            Messages.errorMessage(e);
        }catch(NullInputException e){
            Messages.infoMessage("Cancelando operação...");
        }
    }
    
    /**
     * Método que realiza um saque de modo totalmente autônomo
     */
    private void sacarValor(){
        try{
            double dQtde = Inputs.Double(
                    "Informe a quantidade a ser sacada",
                    "SAQUE"
            );
                    
            this.cb.sacarValor(dQtde);
            
            this.tdao.create(
                    new Transacao(
                            dQtde, 
                            Transacao.DEFAULT_DESC, 
                            Transacao.MOV_DEBITO, 
                            this.cb.getNumeroConta()
                    )
            );
            this.cbdao.update(this.cb);
            
            verificarStatus();
    
        }catch(SaldoInvalidoException e){
            Messages.errorMessage(e);
        }catch(NullInputException e){
            Messages.infoMessage("Cancelando operação...");
        }
    }
    
    /**
     * Método que informa o status da conta
     */
    public void verificarStatus(){
        Messages.infoMessage(this.cb.consultarStatus());
    }
    
    /**
     * Método que informa os logs de movimentação da conta
     * Se não houve nenhuma movimentação naquela conta, exibe mensagem informativa
     */
    private void consultarLogs(){
        StringBuilder sb = new StringBuilder();
        ArrayList<Transacao> tList = this.tdao.read(this.cb.getNumeroConta());
        
        if(tList.isEmpty()){
            Messages.infoMessage("Não há nenhum registro de movimentação nessa conta");
        }else{
        
            for(Transacao t : tList){
                sb.append(t);

                if(tList.indexOf(t) != tList.size() - 1){
                    sb.append("\n");
                }
            }

            Messages.infoMessage(sb.toString());
        }
    }
}
