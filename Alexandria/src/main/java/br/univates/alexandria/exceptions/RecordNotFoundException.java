package br.univates.alexandria.exceptions;

public class RecordNotFoundException extends Exception{
    public RecordNotFoundException(){
        super("Registro não encontrato");
    }
}
