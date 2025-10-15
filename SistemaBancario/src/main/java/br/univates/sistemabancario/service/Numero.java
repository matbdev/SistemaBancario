package br.univates.sistemabancario.service;

import br.univates.sistemabancario.exceptions.NumeroContaInvalidoException;

/**
 * Classe que representa um número da conta, podendo ser aleatório ou não
 * @author mateus.brambilla
 */
public class Numero {
    private String num;
    private int intNum;
    private boolean definidoPeloUsuario;
    
    // Construtor para quando o DAO vai gerar o número
    public Numero() throws NumeroContaInvalidoException{
        this.intNum = 0;
        this.num = formatarString(0);
        this.definidoPeloUsuario = false;
    }
    
    // Construtor para quando o usuário informa o número
    public Numero(int n) throws NumeroContaInvalidoException {
        if (n < 1 || n > 999999) {
            throw new NumeroContaInvalidoException("Informe um número de 1 a 6 dígitos");
        }
        this.intNum = n;
        this.num = formatarString(n);
        this.definidoPeloUsuario = true;
    }

    /**
     * Define o número da conta. Este método foi projetado para ser chamado
     * apenas uma vez pelo DAO durante a criação da conta. Uma vez definido,
     * o número não pode ser alterado novamente.
     * @param n O número único da conta.
     */
    public void setNumero(int n) {
        // Verificação de segurança: só permite a definição se o número atual for 0 (o valor inicial).
        if (this.intNum != 0) {
            throw new IllegalStateException("O número da conta já foi definido e não pode ser alterado.");
        }
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

    public boolean isDefinidoPeloUsuario() { 
        return this.definidoPeloUsuario; 
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
