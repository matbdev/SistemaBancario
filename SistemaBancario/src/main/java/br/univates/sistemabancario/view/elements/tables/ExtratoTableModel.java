package br.univates.sistemabancario.view.elements.tables;

import br.univates.sistemabancario.model.Transacao;
import java.util.List;
import javax.swing.table.AbstractTableModel;

/**
 * Modelo de tabela para ler lista de movimentações bancárias
 * @author mateus.brambilla
 */
public class ExtratoTableModel extends AbstractTableModel {

    private final List<Transacao> transacoes;
    private final String[] colunas = {"Número", "Data", "Descrição", "Tipo", "Valor", "Saldo"};

    public ExtratoTableModel(List<Transacao> transacoes) {
        this.transacoes = transacoes;
    }

    @Override
    public int getRowCount() {
        return transacoes.size();
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
        Transacao t = transacoes.get(rowIndex);

        switch (columnIndex) {
            case 0 -> {return t.getNumero().getNumero();}
            case 1 -> {return t.getDateTime();}
            case 2 -> {return t.getDesc();}
            case 3 -> {return t.getIndicador();}
            case 4 -> {return t.getValor();}
            case 5 -> {return t.getSaldo();}
            default -> {return null;}
        }
    }
}