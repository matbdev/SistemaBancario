package br.univates.sistemabancario.view.components;

import br.univates.sistemabancario.model.ContaBancaria;

import java.util.Collections;
import java.util.List;
import javax.swing.table.AbstractTableModel;

/**
 * Modelo de tabela para ler lista de contas bancárias
 * @author mateus.brambilla
 */
public class ContaTableModel extends AbstractTableModel {

    private final List<ContaBancaria> contas;
    private final String[] colunas = {"Número", "Correntista", "Tipo", "Limite", "Saldo"};

    public ContaTableModel(List<ContaBancaria> contas) {
        this.contas = contas;
        Collections.sort(this.contas);
    }

    @Override
    public int getRowCount() {
        return contas.size();
    }

    @Override
    public int getColumnCount() {
        return colunas.length;
    }

    @Override
    public String getColumnName(int column) {
        return colunas[column];
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        ContaBancaria cb = contas.get(rowIndex);

        switch (columnIndex) {
            case 0 -> {return cb.getNumeroConta().getNumero();}
            case 1 -> {return cb.getPessoa().getCPF().getCpfFormatado();}
            case 2 -> {return cb.getTipoConta();}
            case 3 -> {return "R$" + cb.getLimite();}
            case 4 -> {return "R$" + cb.getSaldo();}
            default -> {return null;}
        }
    }
}