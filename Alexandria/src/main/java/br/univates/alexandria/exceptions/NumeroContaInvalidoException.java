package br.univates.alexandria.exceptions;

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
