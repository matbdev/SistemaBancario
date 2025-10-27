package br.univates.sistemabancario.service;

import java.text.DateFormat;
import java.util.Date;

import br.univates.alexandria.util.FormatadorTexto;
import br.univates.alexandria.util.Verificador;

/**
 * Classe que representa uma movimentação de uma conta bancária
 * @author mateus.brambilla
 */
public class Transacao implements Comparable<Transacao> {
    // Constantes para controle
    public static final char MOV_DEBITO = 'D';
    public static final char MOV_CREDITO = 'C';
    public static final String DEFAULT_DESC = "default";
    
    // Geração de data
    private final DateFormat df = DateFormat.getDateInstance();
    private final DateFormat hf = DateFormat.getTimeInstance();
    private final Date d;
    
    // Outros atributos
    private final String desc;
    private final double valor;
    private final char indicador;
    private final Numero numero;
    private final double saldo;
    
    /**
     * Construtor que recebe o valor e a descrição
     * São imutáveis, para garantir que não haverá mudanças maliciosas
     * dos metadados da transação
     * @param valor - valor da transação
     * @param desc - descricão da transação
     * @param i - indicador da transação, podendo ser uma das constantes acima
     * @param saldo - saldo restante na conta após transação
     * @param data - timestamp da transação
     * @param numero - numero da conta que realizou a transação
     */    
    public Transacao(double valor, double saldo, String desc, char i, Date data, Numero numero){
        char iUpper = Character.toUpperCase(i);
        
        if(iUpper != MOV_DEBITO && iUpper != MOV_CREDITO){
            throw new IllegalArgumentException("Informe uma movimentação válida (MOV_CREDITO ou MOV_DEBITO)");
        }
        
        Verificador.verificaVazio(desc, "Endereço não pode ser vazio.");
        
        this.d = data;
        this.valor = valor;
        this.saldo = saldo;
        this.indicador = iUpper;
        this.numero = numero;
        
        if(desc.equals(DEFAULT_DESC)){
            this.desc = getCorrespondentText() + " de R$" + this.valor + " em " + getDateForDesc();
        }else{ 
            this.desc = FormatadorTexto.converteTitleCase(desc);
        }
    }
    
    //Getters
    public Date getDateTime() {
        return d;
    }

    public String getDesc() {
        return desc;
    }

    public double getValor() {
        return valor;
    }

    public char getIndicador() {
        return indicador;
    }
    
    public Numero getNumero() {
        return numero;
    }

    public double getSaldo() {
        return saldo;
    }
    
    /**
     * Método auxiliar que se baseia na flag de movimentação para retornar o texto correspondente
     * @return "Depósito" ou "Saque" (com base no indicador)
     */
    private String getCorrespondentText(){
        return (this.indicador == 'D') ? "Débito" : "Crédito";
    }
    
    /**
     * Método auxiliar que retorna uma data formatada para a descrição
     * @return 
     */
    public String getDateForDesc(){
        return df.format(this.d) + " | " + hf.format(this.d);
    }

    /**
     * Sobrescrição do método toString(), em formato de log
     * Neste caso, está retornando a própria descrição
     * @return log da transação
     */
    @Override
    public String toString() {        
        return this.desc;
    }

    /**
     * Método sobrescrito da interface comparable
     * Compara duas datas
     * @param t - o objeto da outra transação
     * @return resultado da comparação das datas
     */
    @Override
    public int compareTo(Transacao t) {
        return this.getDateTime().compareTo(t.getDateTime());
    }
}
