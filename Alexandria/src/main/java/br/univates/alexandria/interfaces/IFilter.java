package br.univates.alexandria.interfaces;

public interface IFilter<T> {
    public boolean isAccept(T record);
}
