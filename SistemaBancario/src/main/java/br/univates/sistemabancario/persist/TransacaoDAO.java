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
    private static final Arquivo a = new Arquivo("transacao.dat");
    
    /**
     * Método que retorna todos os valores
     * @return arraylist dos valores
     */
    public ArrayList<Transacao> readAll(){
        ArrayList<Transacao> tList = new ArrayList<>();
        
        if(a.abrirLeitura()){
            try { // Garante que o finally será executado
                String linha;
                while ((linha = a.lerLinha()) != null) {
                    try {
                        String[] tLine = linha.split(";");
                        
                        if (tLine.length < 5) { // Proteção contra linhas mal formatadas
                            Messages.errorMessage("Registro de transação mal formatado no arquivo. Linha ignorada.");
                            continue;
                        }

                        int numero = Integer.parseInt(tLine[0]);
                        Numero nConta = new Numero(numero, false);

                        long timestamp = Long.parseLong(tLine[1]);
                        Date d = new Date(timestamp);

                        double valor = Double.parseDouble(tLine[2]);
                        char indicador = tLine[3].charAt(0);

                        tList.add(new Transacao(valor, tLine[4], indicador, d, nConta));
                        
                    } catch(NumberFormatException e){
                        Messages.errorMessage("Erro de formatação de número em uma linha do arquivo de transações. Linha ignorada.");
                    } catch(NumeroContaInvalidoException e){
                        Messages.errorMessage(e.getMessage());
                    }
                }
            } finally {
                // Garante que o arquivo seja fechado, não importa o que aconteça.
                a.fecharArquivo();
            }
        }
        
        Collections.sort(tList);
        return tList;
    }
    
    /**
     * Método que retorna uma lista de objetos específicos com base no Numero da conta
     * @param n - um objeto da classe Numero
     * @return transações realizadas pela conta
     */ 
    public ArrayList<Transacao> read(Numero n){
        ArrayList<Transacao> transacoesDaConta = new ArrayList<>();
        
        if (a.abrirLeitura()) {
            try {
                String linha;
                while ((linha = a.lerLinha()) != null) {
                    try {
                        String[] tLine = linha.split(";");
                        int numeroContaNoArquivo = Integer.parseInt(tLine[0]);

                        if (numeroContaNoArquivo == n.getNumeroInt()) {
                            long timestamp = Long.parseLong(tLine[1]);
                            Date d = new Date(timestamp);
                            double valor = Double.parseDouble(tLine[2]);
                            char indicador = tLine[3].charAt(0);
                            transacoesDaConta.add(new Transacao(valor, tLine[4], indicador, d, n));
                        }
                    } catch (Exception e) {
                        // Ignora qualquer linha mal formatada para não interromper a busca
                    }
                }
            } finally {
                a.fecharArquivo();
            }
        }
        
        Collections.sort(transacoesDaConta);
        return transacoesDaConta;
    }
    
    /**
     * Método responsável por adicionar um objeto no arraylist
     * @param t - transação a ser adicionada
     */
    public void create(Transacao t){
        ArrayList<Transacao> tList = readAll();
        tList.add(t);
        saveAll(tList);
    }
    
    /**
     * Método privado que salva a lista inteira de transações no arquivo,
     * sobrescrevendo o conteúdo anterior.
     * @param tList - lista completa a ser salva.
     */
    private void saveAll(ArrayList<Transacao> tList){
        if(a.abrirEscrita()){
            try {
                for (Transacao tra : tList) {
                    a.escreverLinha(tra.getLineForSave());
                }
            } finally {
                // Garante que o arquivo seja fechado.
                a.fecharArquivo();
            }
        }
    }
}
    
