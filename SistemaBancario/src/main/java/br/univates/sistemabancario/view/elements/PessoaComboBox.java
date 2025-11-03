package br.univates.sistemabancario.view.elements;

import java.util.ArrayList;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;

import br.univates.alexandria.exceptions.DataBaseException;
import br.univates.alexandria.exceptions.RecordNotReady;
import br.univates.alexandria.interfaces.IDao;
import br.univates.alexandria.models.CPF;
import br.univates.alexandria.models.Pessoa;

public class PessoaComboBox extends JComboBox<Pessoa> {
    private final DefaultComboBoxModel<Pessoa> model;
    private ArrayList<Pessoa> al;
    private IDao<Pessoa, CPF> cdao;

    /**
     * Construtor que recebe uma lista com os itens da combobox
     * 
     * @param al - lista com os itens (objetos de Pessoa)
     */
    public PessoaComboBox(ArrayList<Pessoa> al) {
        this.model = new DefaultComboBoxModel<>();
        this.al = al;
        setModel(model);
        carregarCorrentistas();
    }

    /**
     * Construtor que recebe uma lista com os itens da combobox
     * 
     * @param cdao - dao responsável pela persistência dos correntistas
     */
    public PessoaComboBox(IDao<Pessoa, CPF> cdao) throws RecordNotReady, DataBaseException {
        this.model = new DefaultComboBoxModel<>();
        this.cdao = cdao;
        this.al = cdao.readAll();
        setModel(model);
        carregarCorrentistas();
    }

    /**
     * Método que carrega todos os itens da lista passada no construtor para o
     * combobox
     * Coloca um valor nulo no início
     */
    public void carregarCorrentistas() {
        model.removeAllElements();
        model.addElement(null);

        for (Pessoa p : this.al) {
            model.addElement(p);
        }
    }

    /**
     * Método que carrega todos os itens da lista passada no construtor para o
     * combobox
     * 
     * @param correntista - objeto selecionado
     */
    public void carregarCorrentistas(Pessoa correntista) {
        carregarCorrentistas();

        if (correntista != null) {
            model.setSelectedItem(correntista);
        }
    }

    /**
     * Deleta o correntista da lista passada para o combobox
     * Levanta erro em caso de não existência
     * 
     * @param p - objeto de pessoa (deve estar na lista)
     */
    public void deletar(Pessoa p) {
        if (this.al.indexOf(p) == -1) {
            throw new IllegalArgumentException("Essa pessoa não está na lista de pessoas da combobox.");
        }
        this.al.remove(p);
    }

    /**
     * Retorna a quantidade de itens na lista
     */
    public int getTamanho() {
        return this.al.size();
    }

    /**
     * Recarrega a lista do banco de dados.
     */
    public void recarregarDoBanco() throws RecordNotReady, DataBaseException {
        if (this.cdao != null) {
            this.al = this.cdao.readAll();
        }
    }
}