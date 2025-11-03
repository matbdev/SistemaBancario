package br.univates.sistemabancario.repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;

import br.univates.alexandria.exceptions.DataBaseException;
import br.univates.alexandria.exceptions.RecordNotFoundException;
import br.univates.alexandria.repository.DataBaseConnectionManager;
import br.univates.sistemabancario.repository.interfaces.IDaoTransacao;
import br.univates.sistemabancario.service.Numero;
import br.univates.sistemabancario.service.Transacao;

public class TransacaoDAO extends BaseDAO implements IDaoTransacao<Transacao, Integer> {

    /**
     * {@inheritDoc}
     * Propaga DataBaseException (erro de conexão) do initializeDatabase().
     */
    @Override
    public void create(Transacao t) throws DataBaseException {
        DataBaseConnectionManager db = getDatabaseConnection();

        try {
            String indicadorStr = String.valueOf(t.getIndicador());
            int numeroContaInt = t.getNumero().getNumeroInt();

            db.runPreparedSQL(
                    "INSERT INTO transacao (numero_conta, data_transacao, descricao, valor, tipo, saldo) VALUES (?,?,?,?,?,?);",
                    numeroContaInt, t.getDateTime(), t.getDesc(), t.getValor(), indicadorStr, t.getSaldo());

        } finally {
            if (db != null) {
                db.closeConnection();
            }
        }
    }

    /**
     * {@inheritDoc}
     * Propaga DataBaseException (erro de conexão) do initializeDatabase() e
     * runQuerySQL().
     * Mapeia erros de dados (SQL, NumeroInvalido) para RecordNotReady.
     */
    @Override
    public ArrayList<Transacao> read(Integer number) throws RecordNotFoundException, DataBaseException {
        DataBaseConnectionManager db = getDatabaseConnection();

        ArrayList<Transacao> tList = new ArrayList<>();

        try (ResultSet rs = db.runPreparedQuerySQL("SELECT * FROM transacao WHERE numero_conta = ?;", number)) {
            if (rs.isBeforeFirst()) {
                rs.next();
                while (!rs.isAfterLast()) {
                    Date d = rs.getDate("data_transacao");
                    String descricao = rs.getString("descricao");
                    char indicador = rs.getString("tipo").charAt(0);
                    double valor = rs.getDouble("valor");
                    double saldo = rs.getDouble("saldo");

                    tList.add(new Transacao(valor, saldo, descricao, indicador, d, new Numero(number)));
                    rs.next();
                }
            }
            Collections.sort(tList);
        } catch (SQLException e) {
            throw new RecordNotFoundException();
        } finally {
            db.closeConnection();
        }
        return tList;
    }
}