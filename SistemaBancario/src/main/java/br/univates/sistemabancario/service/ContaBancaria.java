package br.univates.sistemabancario.service;

import br.univates.alexandria.models.Pessoa;
import br.univates.sistemabancario.exceptions.NumeroContaInvalidoException;
import br.univates.sistemabancario.exceptions.SaldoInvalidoException;

/**
 * Classe responsável por simular uma conta bancária simples
 * 
 * @author mateus.brambilla
 */
public class ContaBancaria implements Comparable<ContaBancaria> {
    private final Pessoa pessoa;
    private double saldo = 0;
    private double limite = 0;
    private final Numero nConta;

    // Constructor overload
    public ContaBancaria(Pessoa p, double saldo) throws SaldoInvalidoException, NumeroContaInvalidoException {
        verificaSaldo(saldo, "O saldo é menor que 0, informe uma quantia válida.");
        this.pessoa = p;
        this.saldo = saldo;
        this.nConta = new Numero();
    }

    public ContaBancaria(Pessoa p, double saldo, int numeroConta) throws SaldoInvalidoException {
        verificaSaldo(saldo, "O saldo é menor que 0, informe uma quantia válida.");
        this.pessoa = p;
        this.saldo = saldo;
        this.nConta = new Numero(numeroConta);
    }

    /**
     * Método auxiliar que verifica o saldo informado/resultado
     * 
     * @param saldo   - saldo informado
     * @param message - mensagem a ser exibida
     * @throws IllegalArgumentException - em caso de saldo inválido
     */
    private void verificaSaldo(double saldo, String message) throws SaldoInvalidoException {
        if (saldo < this.limite) {
            throw new SaldoInvalidoException(message);
        }
    }

    /**
     * Método que realiza o depósito de um valor
     * Verifica o valor informado e o resultante
     * 
     * @param saldo - saldo a ser recebido
     * @throws SaldoInvalidoException - em caso de saldo inválido
     */
    public void depositaValor(double saldo) throws SaldoInvalidoException {
        verificaSaldo(saldo, "O saldo é menor que 0, informe uma quantia válida.");
        double novoSaldo = this.saldo + saldo;

        this.saldo = novoSaldo;
    }

    /**
     * Método que realiza o saque de um valor
     * Verifica o valor informado e o resultante
     * 
     * @param saldo - saldo a ser sacado
     * @throws SaldoInvalidoException - em caso de saldo inválido
     */
    public void sacarValor(double saldo) throws SaldoInvalidoException {
        verificaSaldo(saldo, "O saldo é menor que 0, informe uma quantia válida.");
        double novoSaldo = this.saldo - saldo;

        verificaSaldo(
                novoSaldo,
                String.format(
                        "O novo saldo será menor do que R$%.2f.\nOperação cancelada",
                        getLimite()));

        this.saldo = novoSaldo;
    }

    // Getters
    public double getSaldo() {
        return this.saldo;
    }

    public Pessoa getPessoa() {
        return this.pessoa;
    }

    public String getNome() {
        return getPessoa().getNome();
    }

    public String getCpfFormatado() {
        return getPessoa().getCpfFormatado();
    }

    public String getEndereco() {
        return getPessoa().getEndereco();
    }

    public double getLimite() {
        return (this.limite == 0) ? 0 : this.limite * (-1);
    }

    public Numero getNumeroConta() {
        return this.nConta;
    }

    public String getNumeroContaFormatado() {
        return this.getNumeroConta().getNumero();
    }

    public int getNumeroContaInt() {
        return this.getNumeroConta().getNumeroInt();
    }

    public String getLineForSave() {
        return getPessoa().getCpfNumbers() + ";" + getNumeroContaInt() + ";" + getSaldo() + ";" + getLimite() + ";"
                + getTipoConta();
    }

    // Setter
    public void setLimite(double limite) {
        if (limite > 0) {
            this.limite = limite * (-1);
        } else {
            this.limite = limite;
        }
    }

    /**
     * Método que retorna o status atual da conta
     * 
     * @return - status atual da conta
     */
    public String consultarStatus() {
        StringBuilder sb = new StringBuilder();

        sb.append("Número da Conta: ").append(getNumeroContaFormatado());
        sb.append("\nTipo de conta: ").append(getTipoConta());

        if (!getTipoConta().equals("ContaBancaria")) // Só mostra limite caso for especial
            sb.append("\nLimite especial: R$").append(getLimite());

        sb.append("\nTitular da conta: ").append(getNome());
        sb.append("\nCPF do titular: ").append(getCpfFormatado());
        sb.append("\nEndereço do titular: ").append(getEndereco());
        sb.append("\nSaldo atual: R$").append(getSaldo());

        return sb.toString();
    }

    /**
     * Método que retorna o tipo da conta específica
     * 
     * @return - nome do tipo da conta
     */
    public String getTipoConta() {
        return this.getClass().getSimpleName();
    }

    /**
     * Método sobrescrito da interface comparable
     * Subtrai o número de outra conta desta conta
     * 
     * @param cb - o objeto da outra conta
     * @return resultado da subtração dos números
     */
    @Override
    public int compareTo(ContaBancaria cb) {
        return this.getNumeroContaInt() - cb.getNumeroContaInt();
    }

    /**
     * Método sobrescrito de Object
     * Verifica se duas contas são iguais pelo seu número
     * 
     * @param obj - O objeto a ser comparado
     * @return valor booleano para igualdade das contas
     */
    @Override
    public boolean equals(Object obj) {
        boolean r = false;
        if (obj != null && obj instanceof ContaBancaria) {
            ContaBancaria outraConta = (ContaBancaria) obj;
            r = this.getNumeroContaFormatado().equals(outraConta.getNumeroContaFormatado());
        }
        return r;
    }

    /**
     * Método sobrescrito, recomendado pelo próprio netbeans
     * Usado para facilitar acesso com HashMap
     * Utiliza os mesmos campos utilizados no equals
     * 
     * @return código hash gerado
     */
    @Override
    public int hashCode() {
        return this.getNumeroContaFormatado().hashCode();
    }

    /**
     * Método sobrescrito de object, para deixar o menu mais bonito
     * 
     * @return texto personalizado com número da conta e nome do correntista
     */
    @Override
    public String toString() {
        return "Conta: " + this.getNumeroContaFormatado() + " | " + this.getNome();
    }
}