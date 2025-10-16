package br.univates.alexandria.models;

import java.util.Objects;

import br.univates.alexandria.exceptions.CpfInvalidoException;
import br.univates.alexandria.util.FormatadorTexto;
import br.univates.alexandria.util.Verificador;

/**
 * Classe responsável por simbolizar uma pessoa simples
 * @author mateus.brambilla
 */
public class Pessoa implements Comparable<Pessoa>{
    private final CPF cpf;
    private String nome;
    private String endereco;
    
    /**
     * Construtor que instancia uma pessoa
     * @param cpf - cpf do usuário (é passado para classe cpf)
     * @param nome - nome do usuário
     * @param endereco - endereco do usuário
     * @throws CpfInvalidoException - em caso de cpf inválido
     */
    public Pessoa(String cpf, String nome, String endereco) throws CpfInvalidoException{
        this.cpf = new CPF(cpf);
        setNome(nome);
        setEndereco(endereco);
    }
    
    // Getters
    public String getEndereco() {
        return this.endereco;
    }

    public String getNome(){
        return this.nome;
    }
    
    public CPF getCPF(){
        return this.cpf;
    }
    
    public String getCpfFormatado(){
        return getCPF().getCpfFormatado();
    }
    
    public String getCpfNumbers(){
        return getCPF().getCpf();
    }
    
    public String getLineForSave(){
        return this.getCpfNumbers() + ";" + this.getNome() + ";" + this.getEndereco();
    }
    
    // Setters
    public void setEndereco(String endereco){
        Verificador.verificaVazio(endereco, "Endereço não pode ser vazio.");
        this.endereco = FormatadorTexto.converteTitleCase(endereco);
    }

    public void setNome(String nome){
        Verificador.verificaVazio(nome, "Nome não pode ser vazio.");
        this.nome = FormatadorTexto.converteTitleCase(nome);
    }
    
    /**
     * Método sobrescrito de object, para que apareça formatado na combobox
     * @return cpf + nome
     */
    @Override
    public String toString(){
        return(this.getCpfFormatado() + " - " + this.getNome());
    }
    
    /**
     * Método sobrescrito de comparable, verificando se os cpfs são iguais
     * @param outraPessoa - instância de Pessoa
     * @return diferença entre os dígitos
     */
    @Override
    public int compareTo(Pessoa outraPessoa){
        return this.getNome().compareTo(outraPessoa.getNome());
    }
    
    /**
    * Método sobrescrito de Object
    * Verifica se duas contas são iguais pelo seu número
    * @param obj - O objeto a ser comparado
    * @return valor booleano para igualdade das contas
    */
    @Override
    public boolean equals(Object obj) {
        boolean r = false;
        if (obj != null && obj instanceof Pessoa){
            Pessoa outraPessoa = (Pessoa) obj;
            r = Objects.equals(this.getCpfFormatado(), outraPessoa.getCpfFormatado());
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
        return this.getCpfFormatado().hashCode();
    }
}