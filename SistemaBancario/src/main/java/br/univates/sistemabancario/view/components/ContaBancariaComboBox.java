package br.univates.sistemabancario.view.components;

import br.univates.alexandria.components.combobox.IComboBox;
import java.util.ArrayList;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;

import br.univates.sistemabancario.model.ContaBancaria;

/**
 * Modelo de combobox para contas bancárias
 * @author mateus.brambilla
 */
public class ContaBancariaComboBox extends JComboBox<ContaBancaria> implements IComboBox<ContaBancaria> {
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
     * {@inheritedDoc}
     */
    @Override
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
     * {@inheritedDoc}
     */
    @Override
    public void setSelecionado(ContaBancaria cb) {
        if (cb != null && this.al.contains(cb)) {
            model.setSelectedItem(cb);
        } else {
            model.setSelectedItem(null);
        }
    }
    
    /**
     * {@inheritedDoc}
     */
    @Override
    public ContaBancaria getSelecionado() {
        return (ContaBancaria) model.getSelectedItem();
    }

    /**
     * {@inheritedDoc}
     */
    @Override
    public void deletar(ContaBancaria cb) {
        if (!this.al.contains(cb)) {
            throw new IllegalArgumentException("Essa pessoa não está na lista de pessoas da combobox.");
        }
        this.al.remove(cb);
        model.removeElement(cb);
    }

    /**
     * {@inheritedDoc}
     */
    @Override
    public void adicionar(ContaBancaria cb) {
        if (cb != null && !this.al.contains(cb)) {
            this.al.add(cb);
            model.addElement(cb);
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