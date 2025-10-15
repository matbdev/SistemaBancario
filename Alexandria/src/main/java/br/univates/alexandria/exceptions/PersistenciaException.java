package br.univates.alexandria.exceptions;

/**
 * Exception pai para os erros de persistência
 * @author mateus.brambilla
 */
public class PersistenciaException extends Exception {
    public PersistenciaException(String text){
        super(text);
    }
}
