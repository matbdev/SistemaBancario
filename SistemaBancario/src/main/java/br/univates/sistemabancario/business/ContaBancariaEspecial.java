package br.univates.sistemabancario.business;

import br.univates.alexandria.exceptions.NumeroContaInvalidoException;
import br.univates.alexandria.exceptions.SaldoInvalidoException;
import br.univates.alexandria.models.Pessoa;

/**
 * Classe que simula uma conta especial
 * Essa conta possui um limite máximo de saldo negativo
 * @author mateus.brambilla
 */
public class ContaBancariaEspecial extends ContaBancaria {    
    public ContaBancariaEspecial(Pessoa p, double limite, double saldo, int numero)throws SaldoInvalidoException, NumeroContaInvalidoException{
        super(p, saldo, numero);
        setLimite(limite);
    }
    
    public ContaBancariaEspecial(Pessoa p, double limite, int numero) throws NumeroContaInvalidoException{
        super(p, numero);
        setLimite(limite);
    }
    
    public ContaBancariaEspecial(Pessoa p, double limite) throws NumeroContaInvalidoException{
        super(p);
        setLimite(limite);
    }
    
    public ContaBancariaEspecial(Pessoa p, double limite, double saldo)throws SaldoInvalidoException, NumeroContaInvalidoException{
        super(p, saldo);
        setLimite(limite);
    }
}
