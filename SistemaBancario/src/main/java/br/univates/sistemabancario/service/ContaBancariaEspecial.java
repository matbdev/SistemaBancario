package br.univates.sistemabancario.service;

import br.univates.alexandria.models.Pessoa;
import br.univates.sistemabancario.exceptions.SaldoInvalidoException;

/**
 * Classe que simula uma conta especial
 * Essa conta possui um limite máximo de saldo negativo
 * 
 * @author mateus.brambilla
 */
public class ContaBancariaEspecial extends ContaBancaria {
    public ContaBancariaEspecial(Pessoa p, double limite, double saldo)
            throws SaldoInvalidoException {
        super(p, saldo);
        setLimite(limite);
    }

    public ContaBancariaEspecial(Pessoa p, double limite, double saldo, int numeroConta)
            throws SaldoInvalidoException {
        super(p, saldo, numeroConta);
        setLimite(limite);
    }
}
