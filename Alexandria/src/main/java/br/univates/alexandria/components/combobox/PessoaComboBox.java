package br.univates.alexandria.components.combobox;

import java.util.ArrayList;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;

import br.univates.alexandria.models.Pessoa;

/**
 * Modelo de combobox para correntistas
 * @author mateus.brambilla
 */
public class PessoaComboBox extends JComboBox<Pessoa> implements IComboBox<Pessoa> {
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
     * {@inheritedDoc}
     */
    @Override
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
     * {@inheritedDoc}
     */
    @Override
    public void setSelecionado(Pessoa correntista) {
        if (correntista != null && this.al.contains(correntista)) {
            model.setSelectedItem(correntista);
        } else {
            model.setSelectedItem(null);
        }
    }
    
    /**
     * {@inheritedDoc}
     */
    @Override
    public Pessoa getSelecionado() {
        return (Pessoa) model.getSelectedItem();
    }

    /**
     * {@inheritedDoc}
     */
    @Override
    public void deletar(Pessoa p) {
        if (!this.al.contains(p)) {
            throw new IllegalArgumentException("Essa pessoa não está na lista de pessoas da combobox.");
        }
        this.al.remove(p);
        model.removeElement(p);
    }

    /**
     * {@inheritedDoc}
     */
    @Override
    public void adicionar(Pessoa p) {
        if (p != null && !this.al.contains(p)) {
            this.al.add(p);
            model.addElement(p);
        }
    }

    /**
     * {@inheritedDoc}
     */
    @Override
    public int getTamanho() {
        return this.al.size();
    }
}