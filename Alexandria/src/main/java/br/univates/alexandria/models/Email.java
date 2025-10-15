package br.univates.alexandria.models;

/**
 * Classe que simula um email
 * @author mateus.brambilla
 */
public class Email {
    private String email;
    
    public Email(String email){
        setEmail(email);
    }
    /**
     * Setter de email que verifica se o texto contém um @
     * Pode ser desenvolvido futuramente para verificar se o domínio é válido
     * @param email - email a ser setado
     */
    public void setEmail(String email) {
        if (email == null || email.trim().isEmpty() || !email.contains("@")) 
            throw new IllegalArgumentException("Email parece ser inválido.");
        this.email = email;
    }
    
    public String getEmail(){
        return this.email;
    }
}
