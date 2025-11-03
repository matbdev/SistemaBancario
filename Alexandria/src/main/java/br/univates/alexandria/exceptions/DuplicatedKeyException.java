package br.univates.alexandria.exceptions;

public class DuplicatedKeyException extends Exception{
    public DuplicatedKeyException(){
        super("Chave primária duplicada");
    }
}
