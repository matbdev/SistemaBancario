package br.univates.alexandria.models;

import br.univates.alexandria.util.FormatadorTexto;
import br.univates.alexandria.util.Verificador;

/**
 * Método que representa uma opção, com ícone, método e descrição
 * @author mateus.brambilla
 */
public class Opcao {
    private final char icone;
    private final String descricao;
    private final Runnable acao;
    
    /**
     * Construtor que recebe as opções e as valida
     * @param icone - letra da opção
     * @param descricao - descrição da opção
     * @param acao - acao da opção
     */
    public Opcao(char icone, String descricao, Runnable acao){
        Verificador.verificaVazio(icone, "Ícone vazio informado");
        Verificador.verificaVazio(descricao, "Ícone vazio informado");
        
        this.icone = Character.toUpperCase(icone);
        this.descricao = FormatadorTexto.converteTitleCase(descricao);
        this.acao = acao;
    }
    
    // Getters
    public char getIcone() {
        return icone;
    }

    public String getDescricao() {
        return descricao;
    }
    
    public void executarAcao() {
        acao.run(); // Runnable -> contém run para executar
    }
}
