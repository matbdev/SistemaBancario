package br.univates.sistemabancario.view.elements.combobox;

import java.util.ArrayList;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;

import br.univates.sistemabancario.model.ContaBancaria;

/**
 * Modelo de combobox para contas bancárias
 * @author mateus.brambilla
 */
public class ContaBancariaComboBox extends JComboBox<ContaBancaria> {
    private final DefaultComboBoxModel<ContaBancaria> model;
    private ArrayList<ContaBancaria> al;

    //Constructor overload
    public ContaBancariaComboBox() {
        this.model = new DefaultComboBoxModel<>();
        this.al = new ArrayList<>();
        setModel(model);
        carregarItens();
    }
    
    public ContaBancariaComboBox(ArrayList<ContaBancaria> al) {
        this.model = new DefaultComboBoxModel<>();
        this.al = (al != null) ? al : new ArrayList<>();
        setModel(model);
        carregarItens();
    }

    /**
     * Define a lista de dados para este ComboBox e atualiza a exibição.
     * @param cb - nova lista de contas bancárias a ser exibida.
     */
    public void setDados(ArrayList<ContaBancaria> cb) {
        this.al = (cb != null) ? cb : new ArrayList<>();
        carregarItens();
    }
    
    /**
     * Método para carregar os itens no ComboBox.
     */
    private void carregarItens() {
        model.removeAllElements();
        model.addElement(null);

        for (ContaBancaria p : this.al) {
            model.addElement(p);
        }
    }

    /**
     * Seleciona um item no ComboBox com base no objeto.
     * @param cb - objeto a ser selecionado
     */
    public void setSelecionado(ContaBancaria cb) {
        if (cb != null && this.al.contains(cb)) {
            model.setSelectedItem(cb);
        } else {
            model.setSelectedItem(null);
        }
    }
    
    /**
     * Retorna o item selecionado, já fazendo o cast para ContaBancaria.
     * @return objeto de ContaBancaria
     */
    public ContaBancaria getSelecionado() {
        return (ContaBancaria) model.getSelectedItem();
    }

    /**
     * Deleta o correntista da lista interna e do modelo de exibição.
     * @param cb - objeto de conta bancária (deve estar na lista)
     */
    public void deletar(ContaBancaria cb) {
        if (!this.al.contains(cb)) {
            throw new IllegalArgumentException("Essa pessoa não está na lista de pessoas da combobox.");
        }
        this.al.remove(cb);
        model.removeElement(cb);
    }

    /**
     * Adiciona uma pessoa na lista interna e no modelo de exibição.
     * @param cb - objeto de ContaBancaria
     */
    public void adicionar(ContaBancaria cb) {
        if (cb != null && !this.al.contains(cb)) {
            this.al.add(cb);
            model.addElement(cb);
        }
    }

    /**
     * Retorna a quantidade de itens na lista.
     * @return tamanho da lista
     */
    public int getTamanho() {
        return this.al.size();
    }
}