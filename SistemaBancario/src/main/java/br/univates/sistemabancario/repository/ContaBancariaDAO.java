package br.univates.sistemabancario.repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

import br.univates.alexandria.exceptions.CpfInvalidoException;
import br.univates.alexandria.exceptions.DataBaseException;
import br.univates.alexandria.models.CPF;
import br.univates.alexandria.models.Pessoa;
import br.univates.alexandria.repository.BaseDAO;
import br.univates.alexandria.repository.DataBaseConnectionManager;
import br.univates.alexandria.util.Messages;
import br.univates.sistemabancario.exceptions.ContaJaExisteException;
import br.univates.sistemabancario.exceptions.NumeroContaInvalidoException;
import br.univates.sistemabancario.exceptions.SaldoInvalidoException;
import br.univates.sistemabancario.service.ContaBancaria;
import br.univates.sistemabancario.service.ContaBancariaEspecial;
import br.univates.sistemabancario.service.Numero;

/**
 * Classe com a lógica de gravação (inexistente por enquanto)
 * 
 * @author mateus.brambilla
 */
public class ContaBancariaDAO implements BaseDAO<ContaBancaria, Numero> {
    private final CorrentistaDAO cdao;
    private static final Random random = new Random();
    private final DataBaseConnectionManager db;

    /**
     * Construtor que recebe um cdao via injeção de dependência
     * 
     * @param cdao - objeto de CorrentistaDAO já instanciado
     */
    public ContaBancariaDAO(CorrentistaDAO cdao, DataBaseConnectionManager db) {
        this.cdao = cdao;
        this.db = db;
    }

    /**
     * Gera um número de conta único que ainda não existe no DAO.
     * 
     * @return um número inteiro único.
     * @throws NumeroContaInvalidoException - caso o número da conta for inválido
     */
    public int gerarNumeroUnico() throws NumeroContaInvalidoException {
        while (true) {
            int numero = random.nextInt(999999) + 1; // Gera de 1 a 999999
            if (this.read(numero) == null) {
                return numero;
            }
        }
    }

    /**
     * Método que retorna todos os registros
     * 
     * @return lista com objetos ContaBancaria
     */
    @Override
    public ArrayList<ContaBancaria> readAll() {
        ArrayList<ContaBancaria> cbList = new ArrayList<>();

        try {
            ResultSet rs = this.db.runQuerySQL("SELECT * FROM conta;");

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
                    Pessoa pEncontrada = this.cdao.read(cpf);

                    if (pEncontrada == null) {
                        Messages.errorMessage("Conta bancária órfã encontrada. CPF: " + cpf.getCpfFormatado());
                        continue;
                    }

                    if (tipoConta == 'E') {
                        cb = new ContaBancariaEspecial(pEncontrada, limiteConta, 0, numeroConta);
                        if (saldo > 0) {
                            cb.depositaValor(saldo);
                        } else if (saldo < 0) {
                            cb.sacarValor(Math.abs(saldo));
                        }
                    } else {
                        cb = new ContaBancaria(pEncontrada, saldo, numeroConta);
                    }

                    cbList.add(cb);
                    rs.next();
                }
            }

            db.closeConnection();
            Collections.sort(cbList);

        } catch (DataBaseException | CpfInvalidoException | SaldoInvalidoException | SQLException
                | NumeroContaInvalidoException e) {
            Messages.errorMessage(e);
        }
        return cbList;
    }

    /**
     * Método responsável por adicionar um objeto na lista
     * 
     * @param cb - ContaBancaria a ser adicionada
     * @throws ContaJaExisteException - caso a conta já exista
     */
    @Override
    public void create(ContaBancaria cb) throws ContaJaExisteException, NumeroContaInvalidoException {
        if (!cb.getNumeroConta().isDefinidoPeloUsuario()) {
            cb.getNumeroConta().setNumero(gerarNumeroUnico()); // Atribui o número gerado ao objeto da conta
        }

        String tipoContaStr;

        if (cb.getTipoConta().equals("ContaBancaria"))
            tipoContaStr = "N";
        else
            tipoContaStr = "E";

        try {
            this.db.runPreparedSQL("INSERT INTO conta VALUES (?,?,?,?,?);",
                    cb.getNumeroContaInt(), tipoContaStr, cb.getLimite(), cb.getPessoa().getCpfNumbers(),
                    cb.getSaldo());
        } catch (DataBaseException e) {
            Messages.errorMessage(e);
        }
    }

    /**
     * Método que retorna uma ContaBancaria com base no número da conta
     * 
     * @param numero - objeto Numero instanciado, da conta
     * @return a conta com o número igual
     */
    @Override
    public ContaBancaria read(Numero numero) {
        ContaBancaria contaEncontrada = null;

        try {
            ResultSet rs = this.db.runPreparedQuerySQL("SELECT * FROM conta where numero_conta = ?;",
                    numero.getNumeroInt());

            if (rs.isBeforeFirst()) {
                rs.next();

                int numeroConta = numero.getNumeroInt();
                String tipoContaStr = rs.getString("tipo_conta");
                char tipoConta = tipoContaStr.charAt(0);
                double limiteConta = rs.getDouble("limite_conta");
                String cpfCorrentista = rs.getString("cpf_correntista");
                double saldo = rs.getDouble("saldo");

                CPF cpf = new CPF(cpfCorrentista);
                Pessoa pEncontrada = this.cdao.read(cpf);

                if (pEncontrada == null) {
                    Messages.errorMessage("Conta bancária órfã encontrada. CPF: " + cpf.getCpfFormatado());
                } else {
                    if (tipoConta == 'E') {
                        contaEncontrada = new ContaBancariaEspecial(pEncontrada, limiteConta, 0, numeroConta);
                        if (saldo > 0) {
                            contaEncontrada.depositaValor(saldo);
                        } else if (saldo < 0) {
                            contaEncontrada.sacarValor(Math.abs(saldo));
                        }
                    } else {
                        contaEncontrada = new ContaBancaria(pEncontrada, saldo, numeroConta);
                    }
                }
            }

            db.closeConnection();

        } catch (DataBaseException | CpfInvalidoException | SaldoInvalidoException | SQLException
                | NumeroContaInvalidoException e) {
            Messages.errorMessage(e);
        }
        return contaEncontrada;
    }

    /**
     * Método que retorna uma ContaBancaria com base no correntista dela
     * 
     * @param p - objeto de Pessoa instanciado
     * @return lista de contas do correntista
     */
    public ArrayList<ContaBancaria> read(Pessoa p) {
        ArrayList<ContaBancaria> cbList = new ArrayList<>();

        try {
            ResultSet rs = this.db.runPreparedQuerySQL("SELECT * FROM conta where cpf_correntista = ?;",
                    p.getCpfNumbers());

            if (rs.isBeforeFirst()) {
                rs.next();

                while (!rs.isAfterLast()) {
                    int numeroConta = rs.getInt("numero_conta");
                    String tipoContaStr = rs.getString("tipo_conta");
                    char tipoConta = tipoContaStr.charAt(0);
                    double limiteConta = rs.getDouble("limite_conta");
                    double saldo = rs.getDouble("saldo");

                    ContaBancaria cb;
                    if (tipoConta == 'E') {
                        cb = new ContaBancariaEspecial(p, limiteConta, 0, numeroConta);
                        if (saldo > 0) {
                            cb.depositaValor(saldo);
                        } else if (saldo < 0) {
                            cb.sacarValor(Math.abs(saldo));
                        }
                    } else {
                        cb = new ContaBancaria(p, saldo, numeroConta);
                    }

                    cbList.add(cb);
                    rs.next();
                }
            }

            db.closeConnection();
            Collections.sort(cbList);

        } catch (DataBaseException | SaldoInvalidoException | SQLException
                | NumeroContaInvalidoException e) {
            Messages.errorMessage(e);
        }
        return cbList;
    }

    /**
     * Método que retorna uma ContaBancaria com base no número da conta
     * 
     * @param numero - int que representa número da conta
     * @return a conta com o número igual
     * @throws NumeroContaInvalidoException - caso o número for inválido
     */
    public ContaBancaria read(int numero) throws NumeroContaInvalidoException {
        Numero numeroObj = new Numero(numero);
        return read(numeroObj);
    }

    /**
     * Método que retorna uma ContaBancaria com base no número da conta (de outra
     * contabancária)
     * 
     * @param cb2 - outra conta bancária
     * @return a conta com o número igual
     */
    public ContaBancaria read(ContaBancaria cb2) {
        return read(cb2.getNumeroConta());
    }

    /**
     * Método que recebe uma conta e saldo e atualiza o cadastro da conta
     * 
     * @param cb - objeto de ContaBancaria, que vem deste próprio dao
     */
    public void update(ContaBancaria cb) {
        try {
            this.db.runPreparedSQL("UPDATE conta SET saldo = ? WHERE numero_conta = ?",
                    cb.getSaldo(), cb.getNumeroContaInt());
        } catch (DataBaseException e) {
            Messages.errorMessage(e);
        }
    }
}
