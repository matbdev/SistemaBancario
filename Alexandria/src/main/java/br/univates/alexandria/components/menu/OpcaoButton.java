package br.univates.alexandria.components.menu;

import br.univates.alexandria.util.FormatadorTexto;
import br.univates.alexandria.util.Verificador;

/**
 * Método que representa uma opção, com ícone, método e descrição
 * @author mateus.brambilla
 */
public class OpcaoButton extends javax.swing.JButton{ 
    /**
     * Construtor que recebe as opções e as valida
     * @param descricao - descrição da opção
     * @param acao - acao da opção
     */
    public OpcaoButton(String descricao, Runnable acao){
        Verificador.verificaVazio(descricao, "Descrição vazia informado");
        setText(FormatadorTexto.converteTitleCase(descricao));
        
        // Adiciona um ActionListener para tratar o clique
        addActionListener(evt -> acao.run());
    }
}
