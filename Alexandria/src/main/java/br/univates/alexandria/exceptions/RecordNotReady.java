package br.univates.alexandria.exceptions;

public class RecordNotReady extends Exception{

    public RecordNotReady(){}

    public RecordNotReady(String message){
        super(message);
    }
}
