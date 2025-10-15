package br.univates.sistemabancario.business;

import br.univates.alexandria.exceptions.NumeroContaInvalidoException;

/**
 * Classe que representa um número da conta, podendo ser aleatório ou não
 * @author mateus.brambilla
 */
public class Numero {
    private final String num;
    private final int intNum;
    
    //Constructor overload
    public Numero() throws NumeroContaInvalidoException{
        int numeroGerado = AuxNumero.gerarUnico();
        this.num = formatarString(numeroGerado);
        this.intNum = numeroGerado;
    }
    
    public Numero(int n, boolean verificarExistencia) throws NumeroContaInvalidoException{
        if(n < 1 | n > 999999){
            throw new NumeroContaInvalidoException("Informe um número de até 6 dígitos");
        }
        
        if (verificarExistencia) AuxNumero.verificaExiste(n);
            
        this.num = formatarString(n);
        this.intNum = n;
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
