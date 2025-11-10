package br.univates.sistemabancario.controller.autoatendimento;

import br.univates.alexandria.exceptions.DataBaseException;
import br.univates.alexandria.exceptions.RecordNotReady;
import br.univates.alexandria.interfaces.IDao;
import br.univates.alexandria.models.CPF;
import br.univates.alexandria.models.Pessoa;
import br.univates.sistemabancario.controller.elements.ContaBancariaComboBoxController;
import br.univates.sistemabancario.controller.elements.PessoaComboBoxController;
import br.univates.sistemabancario.model.ContaBancaria;
import br.univates.sistemabancario.view.tela.autoatendimento.PainelCorrentistaContaBancaria;
import java.util.ArrayList;

/**
 * Controller responsável pelo painel com os dois combobox personalizados
 * @author mateus.brambilla
 */
public class PainelCorrentistaContaBancariaController {

    private final PainelCorrentistaContaBancaria view;
    
    // DAOs
    private final IDao<ContaBancaria, Integer> cbdao;
    
    // Controllers
    private final PessoaComboBoxController pessoaComboController;
    private final ContaBancariaComboBoxController contaComboController;

    // Construtor
    public PainelCorrentistaContaBancariaController(
            PainelCorrentistaContaBancaria view, 
            IDao<Pessoa, CPF> cdao, 
            IDao<ContaBancaria, Integer> cbdao
    ) {
        this.view = view;
        this.cbdao = cbdao;

        // Controllers
        this.pessoaComboController = new PessoaComboBoxController(
            view.getCbCorrentista(), cdao
        );
        
        this.contaComboController = new ContaBancariaComboBoxController(
            view.getCbContaBancaria(), cbdao
        );

        this.view.setActionPessoaComboBox(e -> onPessoaSelecionada());
    }

    /**
     * Método público para carregar os dados iniciais.
     * @throws DataBaseException - erro de conexão
     * @throws RecordNotReady - quando um elemento não está de acordo
     */
    public void carregarDados() throws DataBaseException, RecordNotReady {
        this.pessoaComboController.carregarDados();
        onPessoaSelecionada();
    }
    
    /**
     * Método que filtra as contas com base na pessoa selecionada
     */
    private void onPessoaSelecionada() {
        try {
            Pessoa pessoa = pessoaComboController.getPessoaSelecionada();
            
            if (pessoa != null) {
                ArrayList<ContaBancaria> cbList = this.cbdao.readAll(
                        conta -> conta.getPessoa().equals(pessoa)
                );
                
                this.contaComboController.setDados(cbList);
            } else {
                this.contaComboController.setDados(new ArrayList<>());
            }
        } catch (DataBaseException | RecordNotReady ex) {
            this.view.exibirErro("Erro ao filtrar contas: " + ex.getMessage());
        }
    }
    
    // Getters
    public Pessoa getPessoaSelecionada() {
        return this.pessoaComboController.getPessoaSelecionada();
    }
    
    public ContaBancaria getContaSelecionada() {
        return this.contaComboController.getContaSelecionada();
    }
}
