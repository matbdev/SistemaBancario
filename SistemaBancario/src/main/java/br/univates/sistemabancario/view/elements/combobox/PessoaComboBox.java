package br.univates.sistemabancario.view.elements.combobox;

import java.util.ArrayList;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;

import br.univates.alexandria.models.Pessoa;

/**
 * Modelo de combobox para correntistas
 * @author mateus.brambilla
 */
public class PessoaComboBox extends JComboBox<Pessoa> {
    private final DefaultComboBoxModel<Pessoa> model;
    private ArrayList<Pessoa> al;

    //Constructor overload
    public PessoaComboBox() {
        this.model = new DefaultComboBoxModel<>();
        this.al = new ArrayList<>();
        setModel(model);
        carregarItens();
    }
    
    /**
     * Construtor que recebe a lista de itens.
     * @param al - lista com os objetos de Pessoa
     */
    public PessoaComboBox(ArrayList<Pessoa> al) {
        this.model = new DefaultComboBoxModel<>();
        this.al = (al != null) ? al : new ArrayList<>();
        setModel(model);
        carregarItens();
    }

    /**
     * Define a lista de dados para este ComboBox e atualiza a exibição.
     * @param pessoas - nova lista de pessoas a ser exibida.
     */
    public void setDados(ArrayList<Pessoa> pessoas) {
        this.al = (pessoas != null) ? pessoas : new ArrayList<>();
        carregarItens();
    }
    
    /**
     * Método para carregar os itens no ComboBox.
     */
    private void carregarItens() {
        model.removeAllElements();
        model.addElement(null);

        for (Pessoa p : this.al) {
            model.addElement(p);
        }
    }

    /**
     * Seleciona um item no ComboBox com base no objeto.
     * @param correntista - objeto a ser selecionado
     */
    public void setSelecionado(Pessoa correntista) {
        if (correntista != null && this.al.contains(correntista)) {
            model.setSelectedItem(correntista);
        } else {
            model.setSelectedItem(null);
        }
    }
    
    /**
     * Retorna o item selecionado, já fazendo o cast para Pessoa.
     * @return objeto de pessoa
     */
    public Pessoa getSelecionado() {
        return (Pessoa) model.getSelectedItem();
    }

    /**
     * Deleta o correntista da lista interna e do modelo de exibição.
     * @param p - objeto de pessoa (deve estar na lista)
     */
    public void deletar(Pessoa p) {
        if (!this.al.contains(p)) {
            throw new IllegalArgumentException("Essa pessoa não está na lista de pessoas da combobox.");
        }
        this.al.remove(p);
        model.removeElement(p);
    }

    /**
     * Adiciona uma pessoa na lista interna e no modelo de exibição.
     * @param p - objeto de pessoa
     */
    public void adicionar(Pessoa p) {
        if (p != null && !this.al.contains(p)) {
            this.al.add(p);
            model.addElement(p);
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