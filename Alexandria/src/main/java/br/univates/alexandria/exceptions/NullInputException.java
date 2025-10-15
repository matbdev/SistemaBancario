package br.univates.alexandria.exceptions;
/**
 * Fornece um erro personalizado para a classe Input
 * @author mateus.brambilla
 */
public class NullInputException extends IllegalArgumentException{
    public NullInputException(String text){
        super(text);
    }
}
