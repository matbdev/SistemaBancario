package br.univates.sistemabancario.repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;

import br.univates.alexandria.exceptions.DataBaseException;
import br.univates.alexandria.repository.DataBaseConnectionManager;
import br.univates.alexandria.util.Messages;
import br.univates.sistemabancario.exceptions.NumeroContaInvalidoException;
import br.univates.sistemabancario.service.Numero;
import br.univates.sistemabancario.service.Transacao;

/**
 * Classe que realiza o salvamento das transações
 * 
 * @author mateus.brambilla
 */
public class TransacaoDAO {
    private final DataBaseConnectionManager db;

    public TransacaoDAO(DataBaseConnectionManager db) {
        this.db = db;
    }

    /**
     * Método que retorna todos os valores
     * 
     * @return arraylist dos valores
     */
    public ArrayList<Transacao> readAll() {
        ArrayList<Transacao> tList = new ArrayList<>();

        try { // Garante que o finally será executado
            ResultSet rs = this.db.runQuerySQL("SELECT * FROM transacao;");

            if (rs.isBeforeFirst()) {
                rs.next();

                while (!rs.isAfterLast()) {
                    int numeroConta = rs.getInt("numero_conta");
                    Numero nConta = new Numero(numeroConta);

                    Date d = rs.getDate("data_transacao");

                    String descricao = rs.getString("descricao");
                    String indicadorStr = rs.getString("tipo");
                    char indicador = indicadorStr.charAt(0);

                    double valor = rs.getDouble("valor");
                    double saldo = rs.getDouble("saldo");

                    tList.add(new Transacao(valor, saldo, descricao, indicador, d, nConta));
                    rs.next();
                }
            }

            db.closeConnection();
            Collections.sort(tList);

        } catch (DataBaseException | SQLException | NumeroContaInvalidoException e) {
            Messages.errorMessage(e);
        }

        return tList;
    }

    /**
     * Método que retorna uma lista de objetos específicos com base no Numero da
     * conta
     * 
     * @param n - um objeto da classe Numero
     * @return transações realizadas pela conta
     */
    public ArrayList<Transacao> read(Numero n) {
        ArrayList<Transacao> tList = readAll();
        ArrayList<Transacao> transacoesDaConta = new ArrayList<>();

        for (Transacao t : tList) {
            if (n.equals(t.getNumero())) {
                transacoesDaConta.add(t);
            }
        }

        return transacoesDaConta;
    }

    /**
     * Método responsável por adicionar um objeto no arraylist
     * 
     * @param t - transação a ser adicionada
     */
    public void create(Transacao t) {
        try {
            String indicadorStr = String.valueOf(t.getIndicador());
            int numeroContaInt = t.getNumero().getNumeroInt();

            SimpleDateFormat sf = new java.text.SimpleDateFormat("yyyy-MM-dd");
            String dataStr = sf.format(t.getDateTime());

            this.db.runPreparedSQL(
                "INSERT INTO transacao (numero_conta, data_transacao, descricao, valor, tipo, saldo) VALUES (?,?,?,?,?,?);",
                numeroContaInt, dataStr, t.getDesc(), t.getValor(), indicadorStr, t.getSaldo()
            );
            
        } catch (DataBaseException e) {
            Messages.errorMessage(e);
        }
    }
}
