package br.univates.alexandria.models;

// - Contato de uma agenda: o objeto deve representar um dos contatos que estão 

import br.univates.alexandria.exceptions.CpfInvalidoException;
import br.univates.alexandria.exceptions.ElementoVazioException;

// em uma agenda de telefone, por exemplo. Sugere-se que este contato tenha um 
// identificador (email, cpf ou outro código) e os dados dele como, por exemplo, 
// nome, email e telefone.

public class Contato {
    // Atributos
    private CPF cpf;
    private Telefone telefone;
    private Email email;
    private String nome;
    private String endereco;

    public Contato(String cpf, String telefone, String endereco, String nome, String email) throws CpfInvalidoException, ElementoVazioException{
        this.cpf = new CPF(cpf);
        setTelefone(telefone);
        setNome(nome);
        setEmail(email);
        setEndereco(endereco);
    }

    // Getters
    public CPF getCPF(){
        return this.cpf;
    }
    
    public String getCpfFormatado(){
        return getCPF().getCpfFormatado();
    }
    
    public String getCpfNumbers(){
        return getCPF().getCpf();
    }

    public String getNome() {
        return this.nome;
    }

    public Email getEmail() {
        return this.email;
    }
    
    public String getEmailStr() {
        return getEmail().getEmail();
    }

    public Telefone getTelefone() {
        return this.telefone;
    }
    
    public String getTelefoneFormatado() {
        return getTelefone().getTelefoneFormatado();
    }
    
    public String getTelefoneNumbers() {
        return getTelefone().getTelefone();
    }

    public String getEndereco() {
        return this.endereco;
    }

    /**
     * Relatório sobre contato
     */
    public String getInfo(){
        return String.format(
            """
            CPF: %s
            Nome: %s
            Telefone: %s
            Endereço: %s
            Email: %s  
            """,
            getCpfFormatado(),
            getNome(),
            getTelefoneFormatado(),
            getEndereco(),
            getEmailStr()
        );
    }

    // Setters
    public void setNome(String nome) {
        if (nome == null || nome.trim().isEmpty()) 
            throw new IllegalArgumentException("Nome não pode ser vazio.");
        this.nome = nome;
    }

    public void setEndereco(String endereco) {
        if (endereco == null || endereco.trim().isEmpty())
            throw new IllegalArgumentException("Endereço não pode ser vazio.");
        this.endereco = endereco;
    }
    
    public void setTelefone(String telefone) {
        this.telefone = new Telefone(telefone);
    }
    
    public void setEmail(String email) {
        this.email = new Email(email);
    }
}
