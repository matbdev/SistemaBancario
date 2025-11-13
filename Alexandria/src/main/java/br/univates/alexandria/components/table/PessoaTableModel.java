package br.univates.alexandria.components.table;

import br.univates.alexandria.models.Pessoa;

import java.util.Collections;
import java.util.List;
import javax.swing.table.AbstractTableModel;

/**
 * Modelo de tabela para ler lista de pessoas
 * @author mateus.brambilla
 */
public class PessoaTableModel extends AbstractTableModel {

    private final List<Pessoa> pessoas;
    private final String[] colunas = {"CPF", "Nome", "Endereço"};

    public PessoaTableModel(List<Pessoa> pessoas) {
        this.pessoas = pessoas;
        Collections.sort(this.pessoas);
    }

    @Override
    public int getRowCount() {
        return pessoas.size();
    }

    @Override
    public int getColumnCount() {
        return colunas.length;
    }

    @Override
    public String getColumnName(int column) {
        return colunas[column]; // Retorna o nome da coluna
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Pessoa pessoa = pessoas.get(rowIndex);

        switch (columnIndex) {
            case 0 -> {return pessoa.getCPF().getCpf();}
            case 1 -> {return pessoa.getNome();}
            case 2 -> {return pessoa.getEndereco();}
            default -> {return null;}
        }
    }
}