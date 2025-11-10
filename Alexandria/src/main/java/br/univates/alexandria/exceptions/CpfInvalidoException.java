package br.univates.alexandria.exceptions;

/**
 * Exception para quando o cpf informado é inválido
 * @author mateus.brambilla
 */
public class CpfInvalidoException extends RecordNotReady{
    public CpfInvalidoException(String text){
        super(text);
    }
}
