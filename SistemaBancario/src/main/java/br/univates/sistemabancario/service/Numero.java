package br.univates.sistemabancario.service;

import br.univates.sistemabancario.exceptions.NumeroContaInvalidoException;

/**
 * Classe que representa um número da conta
 * @author mateus.brambilla
 */
public class Numero {
    private final String num;
    private final int intNum;
    private static int nContaCounter = 0; 

    public Numero() throws NumeroContaInvalidoException {
        int novoNumero = ++nContaCounter;
        this.intNum = novoNumero;
        this.num = formatarString(this.intNum);
    }

    /**
     * Construtor para quando o número da conta já é conhecido
     */
    public Numero(int n) {
        this.intNum = n;
        this.num = formatarString(n);
    }
    
    //Getter
    public String getNumero(){
        return this.num;
    }
    
    public int getNumeroInt(){
        return this.intNum;
    }
    
    /**
     * Método que formata um int, o convertendo para uma String, com 7 dígitos
     * @param n - número de entrada
     * @return número com 7 dígitos
     */
    private String formatarString(int n){
        return String.format("%07d", n);
    }
    
    /**
     * Método sobrescrito de object, que verifica se dois números são iguais
     * @param obj - outro número instanciado
     * @return valor booleano da igualdade
     */
    @Override
    public boolean equals(Object obj){
        boolean r = false;
        if (obj != null && obj instanceof Numero){
            Numero outroNumero = (Numero) obj;
            r = this.getNumero().equals(outroNumero.getNumero());
        }
        return r;
    }
    
    /**
     * Método sobrescrito, recomendado pelo próprio netbeans
     * Usado para facilitar acesso com HashMap
     * Utiliza os mesmos campos utilizados no equals
     * @return código hash gerado
     */
    @Override
    public int hashCode() {
        return this.getNumero().hashCode();
    }
}