package br.univates.sistemabancario.exceptions;

import br.univates.alexandria.exceptions.PersistenciaException;

/**
 * Exception personalizada para saldo
 * @author mateus.brambilla
 */
public class SaldoInvalidoException extends PersistenciaException {
    public SaldoInvalidoException(String text){
        super(text);
    }
}
