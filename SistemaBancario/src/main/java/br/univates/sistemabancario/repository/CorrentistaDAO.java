package br.univates.sistemabancario.repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;

import br.univates.alexandria.exceptions.CpfInvalidoException;
import br.univates.alexandria.exceptions.DataBaseException;
import br.univates.alexandria.models.CPF;
import br.univates.alexandria.models.Pessoa;
import br.univates.alexandria.repository.BaseDAO;
import br.univates.alexandria.repository.DataBaseConnectionManager;
import br.univates.alexandria.util.Messages;

/**
 * Classe que lida com a persistência para cadastro de correntista
 * 
 * @author mateus.brambilla
 */
public class CorrentistaDAO implements BaseDAO<Pessoa, CPF> {
    private final DataBaseConnectionManager db;

    public CorrentistaDAO(DataBaseConnectionManager db) {
        this.db = db;
    }

    /**
     * Método que retorna todos os valores
     * 
     * @return arraylist dos valores
     */
    @Override
    public ArrayList<Pessoa> readAll() {
        ArrayList<Pessoa> pList = new ArrayList<>();
        try {
            ResultSet rs = this.db.runQuerySQL("SELECT * FROM correntista;");

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

            db.closeConnection();
            Collections.sort(pList);

        } catch (DataBaseException | SQLException | CpfInvalidoException e) {
            Messages.errorMessage(e);
        }

        return pList;
    }

    /**
     * Método que retorna um objeto específico com base no cpf
     * 
     * @param cpf - um objeto da classe CPF
     * @return a pessoa encontrada ou nulo
     */
    @Override
    public Pessoa read(CPF cpf) {
        ArrayList<Pessoa> pList = readAll();

        for (Pessoa p : pList) {
            if (p.getCpfNumbers().equals(cpf.getCpf())) {
                return p;
            }
        }
        return null;
    }

    /*
     * Método que retorna uma pessoa se já houver uma no banco de dados
     * com o mesmo CPF
     * 
     * @param pessoa - um objeto da classe Pessoa
     * 
     * @return a pessoa encontrada ou nulo
     */
    public Pessoa read(Pessoa pessoa) {
        return read(pessoa.getCPF()); // Reutiliza o método acima
    }

    /*
     * Método que retorna uma pessoa se já houver uma no banco de dados
     * com o mesmo CPF
     * 
     * @param pessoa - um objeto da classe Pessoa
     * 
     * @return a pessoa encontrada ou nulo
     */
    public Pessoa read(String cpf) {
        try {
            return read(new CPF(cpf)); // Reutiliza o método acima
        } catch (CpfInvalidoException e) {
            Messages.errorMessage(e);
        }
        return null;
    }

    /**
     * Método responsável por adicionar um objeto no arraylist
     * 
     * @param p - pessoa a ser adicionada
     */
    @Override
    public void create(Pessoa p) {
        try {
            this.db.runPreparedSQL("INSERT INTO correntista VALUES (?,?,?);",
                    p.getCpfNumbers(), p.getNome(), p.getEndereco());
        } catch (DataBaseException e) {
            Messages.errorMessage(e);
        }
    }

    /**
     * Método que recebe uma pessoa, nome e endereço e atualiza o cadastro do
     * usuário.
     * Retorna a instância da pessoa atualizada.
     * 
     * @param p        - objeto de pessoa original
     * @param nome     - nome (novo ou corrente) da pessoa
     * @param endereco - endereço (novo ou corrente) da pessoa
     */
    public void update(Pessoa p) {
        try {
            this.db.runPreparedSQL("UPDATE correntista SET nome = ?, endereco = ? WHERE cpf_correntista = ?",
                    p.getNome(), p.getEndereco(), p.getCpfNumbers());
        } catch (DataBaseException e) {
            Messages.errorMessage(e);
        }
    }

    /**
     * Método que recebe um objeto e o delta
     * 
     * @param p - objeto de pessoa
     */
    public void delete(Pessoa p) {
        try {
            this.db.runPreparedSQL("DELETE FROM correntista WHERE cpf_correntista = ?",
                    p.getCpfNumbers());
        } catch (DataBaseException e) {
            Messages.errorMessage(e);
        }
    }
}
