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

public class CorrentistaDAO implements IDao<Pessoa, CPF> {

    public CorrentistaDAO() {
    }

    /**
     * {@inheritedDoc}
     */
    @Override
    public void create(Pessoa p, DataBaseConnectionManager db) throws DuplicatedKeyException, DataBaseException {
        if (db == null) {
            throw new DataBaseException("A conexão com o banco de dados não pode ser nula.");
        }
        try {
            db.runPreparedSQL("INSERT INTO correntista VALUES (?,?,?);",
                    p.getCpfNumbers(), p.getNome(), p.getEndereco());
        } catch (DataBaseException e) {
            throw new DuplicatedKeyException();
        }
    }

    /**
     * {@inheritDoc}
     * 
     * Este método gerencia sua própria conexão e a fecha após o uso.
     */
    @Override
    public void create(Pessoa p) throws DuplicatedKeyException, DataBaseException {
        DataBaseConnectionManager db = null;
        try {
            db = DAOFactory.getDataBaseConnectionManager();
            this.create(p, db);
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
    public Pessoa read(CPF cpf) throws RecordNotFoundException, DataBaseException {
        Pessoa pessoaEncontrada = null;
        DataBaseConnectionManager db = null;

        try {
            db = DAOFactory.getDataBaseConnectionManager();

            try (ResultSet rs = db.runPreparedQuerySQL("SELECT * FROM correntista where cpf_correntista = ?;",
                    cpf.getCpf())) {

                if (rs.isBeforeFirst()) {
                    rs.next();
                    String nome = rs.getString("nome");
                    String endereco = rs.getString("endereco");
                    pessoaEncontrada = new Pessoa(cpf, nome, endereco);
                }
            } catch (SQLException e) {
                throw new RecordNotFoundException();
            }

        } finally {
            if (db != null) {
                db.closeConnection();
            }
        }

        if (pessoaEncontrada == null) {
            throw new RecordNotFoundException();
        }
        return pessoaEncontrada;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ArrayList<Pessoa> readAll() throws RecordNotReady, DataBaseException {
        ArrayList<Pessoa> pList = new ArrayList<>();
        DataBaseConnectionManager db = null;

        try {
            db = DAOFactory.getDataBaseConnectionManager();

            try (ResultSet rs = db.runQuerySQL("SELECT * FROM correntista;")) {

                if (rs.isBeforeFirst()) {
                    rs.next();
                    while (!rs.isAfterLast()) {
                        String cpf = rs.getString("cpf_correntista");
                        String nome = rs.getString("nome");
                        String endereco = rs.getString("endereco");

                        pList.add(new Pessoa(cpf, nome, endereco));
                        rs.next();
                    }
                }
                Collections.sort(pList);
            } catch (CpfInvalidoException | SQLException e) {
                throw new RecordNotReady();
            }

        } finally {
            // Fecha a conexão
            if (db != null) {
                db.closeConnection();
            }
        }
        return pList;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ArrayList<Pessoa> readAll(IFilter<Pessoa> filtro) throws RecordNotReady, DataBaseException {
        ArrayList<Pessoa> listaCompleta = this.readAll();
        ArrayList<Pessoa> listaFiltrada = new ArrayList<>();
        for (Pessoa p : listaCompleta) {
            if (filtro.isAccept(p)) {
                listaFiltrada.add(p);
            }
        }
        return listaFiltrada;
    }

    /**
     * {@inheritDoc}
     */
    public void update(Pessoa p, DataBaseConnectionManager db) throws RecordNotFoundException, DataBaseException {
        if (db == null) {
            throw new DataBaseException("A conexão com o banco de dados não pode ser nula.");
        }
        db.runPreparedSQL("UPDATE correntista SET nome = ?, endereco = ? WHERE cpf_correntista = ?",
                    p.getNome(), p.getEndereco(), p.getCpfNumbers());
    }

    /**
     * {@inheritDoc}
     * 
     * Este método gerencia sua própria conexão e a fecha após o uso.
     */
    @Override
    public void update(Pessoa p) throws RecordNotFoundException, DataBaseException {
        DataBaseConnectionManager db = null;
        try {
            db = DAOFactory.getDataBaseConnectionManager();
            this.update(p, db);
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
    public void delete(Pessoa p) throws RecordNotFoundException, DataBaseException {
        DataBaseConnectionManager db = null;
        try {
            // Abre a conexão
            db = DAOFactory.getDataBaseConnectionManager();

            // Propaga DataBaseException (erro de conexão)
            db.runPreparedSQL("DELETE FROM correntista WHERE cpf_correntista = ?",
                    p.getCpfNumbers());

        } finally {
            // Fecha a conexão
            if (db != null) {
                db.closeConnection();
            }
        }
    }
}