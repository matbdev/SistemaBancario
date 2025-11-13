package br.univates.alexandria.components.combobox;

import java.util.ArrayList;

public interface IComboBox<T> {
    /**
     * Define a lista de dados para este ComboBox e atualiza a exibição.
     * @param obj - nova lista de objetos a ser exibida.
     */
    public void setDados(ArrayList<T> obj);

    /**
     * Seleciona um item no ComboBox com base no objeto.
     * @param obj - objeto a ser selecionado
     */
    public void setSelecionado(T obj);

    /**
     * Retorna o item selecionado, já fazendo o cast para o objeto.
     * @return objeto da combobox
     */
    public T getSelecionado();

    /**
     * Deleta o objeto da lista interna e do modelo de exibição.
     * @param obj - objeto da combobox (deve estar na lista)
     */
    public void deletar(T obj);

    /**
     * Adiciona um objeto na lista interna e no modelo de exibição.
     * @param obj - objeto do combobox
     */
    public void adicionar(T obj);

    /**
     * Retorna a quantidade de itens na lista.
     * @return tamanho da lista
     */
    public int getTamanho();
}