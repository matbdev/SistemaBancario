package br.univates.sistemabancario.persist;

import br.univates.alexandria.exceptions.NumeroContaInvalidoException;
import br.univates.alexandria.tools.Arquivo;
import br.univates.alexandria.tools.Messages;
import br.univates.sistemabancario.business.Numero;
import br.univates.sistemabancario.business.Transacao;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.InputMismatchException;

/**
 * Classe que realiza o salvamento das transações
 * @author mateus.brambilla
 */
public class TransacaoDAO {
    private static final Arquivo a = new Arquivo("transicao.dat");
    
    /**
     * Método que retorna todos os valores
     * @return arraylist dos valores
     */
    public ArrayList<Transacao> readAll(){
        ArrayList<Transacao> tList = new ArrayList<>();
        String linha;
        
        if(a.abrirLeitura()){
            while ((linha = a.lerLinha()) != null) {
                String[] tLine = linha.split(";");
                try {
                    int numero = Integer.parseInt(tLine[0]);
                    Numero nConta = new Numero(numero, false);
                    
                    long timestamp = Long.parseLong(tLine[1]);
                    Date d = new Date(timestamp);
                    
                    double saldo = Double.parseDouble(tLine[2]);
                    char indicador = tLine[3].charAt(0);
                    
                    tList.add(new Transacao(saldo, tLine[4], indicador, d, nConta));
                } catch(InputMismatchException e){
                    Messages.errorMessage("Erro de formatação de um dos campos do arquivo");
                } catch(NumeroContaInvalidoException e){
                    Messages.errorMessage(e);
                }
            }
        }
        a.fecharArquivo();
        
        Collections.sort(tList);
        return tList;
    }
    
    /**
     * Método que retorna uma lista de objetos específicos com base no Numero da conta
     * @param n - um objeto da classe Numero
     * @return transações realizadas pela conta
     */ 
    public ArrayList<Transacao> read(Numero n){
        ArrayList<Transacao> tList = readAll();
        
        ArrayList<Transacao> transacoes = new ArrayList<>();
        
        for(Transacao t : tList){
            if(t.getNumero().equals(n)){
                transacoes.add(t);
            }
        }
        
        return transacoes;
    }
    
    /**
     * Método responsável por adicionar um objeto no arraylist
     * @param t - transação a ser adicionada
     */
    public void create(Transacao t){
        addOnArchive(t);
    }
    
    /**
     * Método auxiliar que realiza o salvamento no arquivo
     * Não existe append, só overwrite
     * @param p - pessoa a ser adicionada
     */
    private void addOnArchive(Transacao t){
        ArrayList<Transacao> tList = readAll();
        tList.add(t);
        
        if(a.abrirEscrita()){
            for (Transacao tra : tList) {
                a.escreverLinha(tra.getLineForSave());
            }
        }
        a.fecharArquivo();
    }
}
    
