package br.univates.alexandria.exceptions;

/**
 * Exception responsável por indicar que algo está vazio, para a classe
 * Verificador, de br.univates.alexandria.tools
 * @author mateus.brambilla
 */
public class ElementoVazioException extends IllegalArgumentException {
    public ElementoVazioException(String text){
        super(text);
    }
}
