package br.univates.sistemabancario.controller.elements;

import br.univates.alexandria.exceptions.DataBaseException;
import br.univates.alexandria.exceptions.RecordNotReady;
import br.univates.alexandria.interfaces.IDao;
import br.univates.sistemabancario.model.ContaBancaria;
import br.univates.sistemabancario.view.elements.combobox.ContaBancariaComboBox;
import java.util.ArrayList;

/**
 * Controller responsável pelo combobox de contabancaria
 */
public class ContaBancariaComboBoxController {

    private final ContaBancariaComboBox comboBoxView;
    private final IDao<ContaBancaria, Integer> cbdao;

    // Construtor
    public ContaBancariaComboBoxController(ContaBancariaComboBox comboBoxView, IDao<ContaBancaria, Integer> cbdao) {
        this.comboBoxView = comboBoxView;
        this.cbdao = cbdao;
    }
    
    /**
     * Define lista de contas bancárias (com base na seleção de uma pessoa)
     * @param contas - lista de contas bancárias filtradas
     */
    public void setDados(ArrayList<ContaBancaria> contas) {
        comboBoxView.setDados(contas);
    }

    /**
     * Método para carregar os dados do DAO
     * @throws DataBaseException - erro de banco de dados
     * @throws RecordNotReady - registro com algum erro
     */
    public void carregarDados() throws DataBaseException, RecordNotReady {
        ArrayList<ContaBancaria> cbList = cbdao.readAll();
        comboBoxView.setDados(cbList);
    }

    /**
     * Retorna a pessoa selecionada no ComboBox.
     * @return pessoa selecionada
     */
    public ContaBancaria getContaSelecionada() {
        return comboBoxView.getSelecionado();
    }

    /**
     * Define qual conta bancaria deve ser selecionada no ComboBox.
     * @param cb - conta bancaria a ser selecionada
     */
    public void setPessoaSelecionada(ContaBancaria cb) {
        comboBoxView.setSelecionado(cb);
    }
}