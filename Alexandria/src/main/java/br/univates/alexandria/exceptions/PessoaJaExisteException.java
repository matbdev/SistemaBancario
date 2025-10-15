package br.univates.alexandria.exceptions;

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
