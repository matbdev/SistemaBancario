package br.univates.alexandria.exceptions;

/**
 * Exception personalizada para saldo
 * @author mateus.brambilla
 */
public class SaldoInvalidoException extends PersistenciaException {
    public SaldoInvalidoException(String text){
        super(text);
    }
}
