package br.univates.sistemabancario.repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;

import br.univates.alexandria.exceptions.CpfInvalidoException;
import br.univates.alexandria.exceptions.DataBaseException;
import br.univates.alexandria.exceptions.DuplicatedKeyException;
import br.univates.alexandria.exceptions.RecordNotFoundException;
import br.univates.alexandria.exceptions.RecordNotReady;
import br.univates.alexandria.interfaces.IDao;
import br.univates.alexandria.interfaces.IFilter;
import br.univates.alexandria.models.CPF;
import br.univates.alexandria.models.Pessoa;
import br.univates.alexandria.repository.DataBaseConnectionManager;
import br.univates.sistemabancario.exceptions.SaldoInvalidoException;
import br.univates.sistemabancario.service.ContaBancaria;
import br.univates.sistemabancario.service.ContaBancariaEspecial;

public class ContaBancariaDAO extends BaseDAO implements IDao<ContaBancaria, Integer> {

    public ContaBancariaDAO() {
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void create(ContaBancaria cb) throws DuplicatedKeyException, DataBaseException {
        DataBaseConnectionManager db = null;
        try {
            db = getDatabaseConnection();

            try {
                String tipoContaStr = (cb.getTipoConta().equals("ContaBancaria")) ? "N" : "E";
                db.runPreparedSQL("INSERT INTO conta VALUES (?,?,?,?,?);",
                        cb.getNumeroContaInt(), tipoContaStr, cb.getLimite(), cb.getPessoa().getCpfNumbers(),
                        cb.getSaldo());

            } catch (DataBaseException e) {
                throw new DuplicatedKeyException();
            }

        } finally {
            // Fecha a conexão do INSERT
            if (db != null) {
                db.closeConnection();
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ContaBancaria read(Integer numero) throws RecordNotFoundException, DataBaseException {
        ContaBancaria contaEncontrada = null;
        DataBaseConnectionManager db = null;
        CorrentistaDAO cdao = new CorrentistaDAO();

        try {
            db = getDatabaseConnection();

            try (ResultSet rs = db.runPreparedQuerySQL("SELECT * FROM conta where numero_conta = ?;",
                    numero)) {

                if (rs.isBeforeFirst()) {
                    rs.next();
                    String tipoContaStr = rs.getString("tipo_conta");
                    char tipoConta = tipoContaStr.charAt(0);
                    double limiteConta = rs.getDouble("limite_conta");
                    String cpfCorrentista = rs.getString("cpf_correntista");
                    double saldo = rs.getDouble("saldo");

                    CPF cpf = new CPF(cpfCorrentista);
                    Pessoa pEncontrada;
                    try {
                        pEncontrada = cdao.read(cpf);
                    } catch (RecordNotFoundException e) {
                        throw new RecordNotFoundException();
                    }

                    if (tipoConta == 'E') {
                        contaEncontrada = new ContaBancariaEspecial(pEncontrada, limiteConta, 0, numero);
                        if (saldo > 0)
                            contaEncontrada.depositaValor(saldo);
                        else if (saldo < 0)
                            contaEncontrada.sacarValor(Math.abs(saldo));
                    } else {
                        contaEncontrada = new ContaBancaria(pEncontrada, saldo, numero);
                    }
                }
            } catch (SQLException | CpfInvalidoException | SaldoInvalidoException e) {
                throw new RecordNotFoundException();
            }

        } finally {
            // Fecha a conexão
            if (db != null) {
                db.closeConnection();
            }
        }

        if (contaEncontrada == null) {
            throw new RecordNotFoundException();
        }
        return contaEncontrada;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ArrayList<ContaBancaria> readAll() throws RecordNotReady, DataBaseException {
        ArrayList<ContaBancaria> cbList = new ArrayList<>();
        DataBaseConnectionManager db = null;
        CorrentistaDAO cdao = new CorrentistaDAO();

        try {
            db = getDatabaseConnection();

            try (ResultSet rs = db.runQuerySQL("SELECT * FROM conta;")) {

                if (rs.isBeforeFirst()) {
                    rs.next();
                    while (!rs.isAfterLast()) {
                        int numeroConta = rs.getInt("numero_conta");
                        String tipoContaStr = rs.getString("tipo_conta");
                        char tipoConta = tipoContaStr.charAt(0);
                        double limiteConta = rs.getDouble("limite_conta");
                        String cpfCorrentista = rs.getString("cpf_correntista");
                        double saldo = rs.getDouble("saldo");

                        ContaBancaria cb;
                        CPF cpf = new CPF(cpfCorrentista);

                        Pessoa pEncontrada = cdao.read(cpf);

                        if (tipoConta == 'E') {
                            cb = new ContaBancariaEspecial(pEncontrada, limiteConta, 0, numeroConta);
                            if (saldo > 0)
                                cb.depositaValor(saldo);
                            else if (saldo < 0)
                                cb.sacarValor(Math.abs(saldo));
                        } else {
                            cb = new ContaBancaria(pEncontrada, saldo, numeroConta);
                        }
                        cbList.add(cb);
                        rs.next();
                    }
                }
                Collections.sort(cbList);
            } catch (SQLException | CpfInvalidoException | SaldoInvalidoException
                    | RecordNotFoundException e) {
                throw new RecordNotReady();
            }

        } finally {
            // Fecha a conexão
            if (db != null) {
                db.closeConnection();
            }
        }
        return cbList;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ArrayList<ContaBancaria> readAll(IFilter<ContaBancaria> filtro) throws RecordNotReady, DataBaseException {
        ArrayList<ContaBancaria> listaParcial = new ArrayList<>();
        for (ContaBancaria cb : this.readAll()) {
            if (filtro.isAccept(cb)) {
                listaParcial.add(cb);
            }
        }
        return listaParcial;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void update(ContaBancaria cb) throws RecordNotFoundException, DataBaseException {
        DataBaseConnectionManager db = null;
        try {
            db = getDatabaseConnection();

            db.runPreparedSQL("UPDATE conta SET saldo = ? WHERE numero_conta = ?",
                    cb.getSaldo(), cb.getNumeroContaInt());

        } finally {
            // Fecha a conexão
            if (db != null) {
                db.closeConnection();
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void delete(ContaBancaria cb) throws RecordNotFoundException, DataBaseException {
        DataBaseConnectionManager db = null;
        try {
            db = getDatabaseConnection();

            db.runPreparedSQL("DELETE FROM conta WHERE numero_conta = ?",
                    cb.getNumeroContaInt());

        } finally {
            // Fecha a conexão
            if (db != null) {
                db.closeConnection();
            }
        }
    }
}