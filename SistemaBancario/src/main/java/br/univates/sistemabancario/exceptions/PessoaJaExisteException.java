package br.univates.sistemabancario.exceptions;

import br.univates.alexandria.exceptions.PersistenciaException;

/**
 * Exception lançada quando uma pessoa com um cpf já existente tenta ser
 * adicionada no banco de dados
 * @author mateus.brambilla
 */
public class PessoaJaExisteException extends PersistenciaException {
    public PessoaJaExisteException(String text){
        super(text);
    }
}
