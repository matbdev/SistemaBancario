package br.univates.sistemabancario.exceptions;

import br.univates.alexandria.exceptions.PersistenciaException;

/**
 * Exception lançada quando um número inválido é informado para o construtor
 * da classe Número
 * @author mateus.brambilla
 */
public class NumeroContaInvalidoException extends PersistenciaException{
    public NumeroContaInvalidoException(String text){
        super(text);
    }
}
