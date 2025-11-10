package br.univates.sistemabancario.exceptions;

import br.univates.alexandria.exceptions.RecordNotReady;

/**
 * Exception personalizada para saldo
 * @author mateus.brambilla
 */
public class SaldoInvalidoException extends RecordNotReady {
    public SaldoInvalidoException(String text){
        super(text);
    }
}
