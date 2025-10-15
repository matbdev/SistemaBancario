package br.univates.alexandria.exceptions;

/**
 * Exception para quando uma conta já existe na hora da adição no banco de dados
 * @author mateus.brambilla
 */
public class ContaJaExisteException extends PersistenciaException {
    public ContaJaExisteException(String text){
        super(text);
    }
}
