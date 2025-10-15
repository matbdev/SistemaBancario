package br.univates.alexandria.models;

/**
 * Classe que replica um telefone
 * @author mateus.brambilla
 */
public class Telefone {
    private String telefone;
    
    public Telefone(String telefone){
        setTelefone(telefone);
    }
    /**
     * Verifica se tamanho do telefone é válido
     * Por enquanto verifica apenas tamanho (aceita 10 ou 11 dígitos)
     * @param telefone - telefone do construtor/setter
     * @throws IllegalArgumentException - em caso de tamanho inválido
     */
    private void verificaTelefone(String telefone) throws IllegalArgumentException{
        int tamanho = telefone.length();
        if (!(tamanho == 10 || tamanho == 11)) {
            throw new IllegalArgumentException("Telefone inválido");
        }
    }
    
    // Setter
    public void setTelefone(String telefone) {
        String telefoneLimpo = telefone.replaceAll("[^0-9]", "");
        verificaTelefone(telefoneLimpo);
        this.telefone = telefoneLimpo;
    }
    
    // Getters
    public String getTelefoneFormatado() {
        int tamanho = telefone.length();
        String ddd = telefone.substring(0, 2);
        String telefonePRetornar = "";

        if (tamanho == 11) {
            String primeiraCadeia = telefone.substring(2, 7);
            String segundaCadeia = telefone.substring(7, 11);
            telefonePRetornar = "(" + ddd + ") " + primeiraCadeia + "-" + segundaCadeia; 
        }
        else if (tamanho == 10) {
            String primeiraCadeia = telefone.substring(2, 6);
            String segundaCadeia = telefone.substring(6, 10);
            telefonePRetornar = "(" + ddd + ") " + primeiraCadeia + "-" + segundaCadeia; 
        }
        
        return telefonePRetornar;
    }
    
    public String getTelefone() {
        return this.telefone;
    }
}
