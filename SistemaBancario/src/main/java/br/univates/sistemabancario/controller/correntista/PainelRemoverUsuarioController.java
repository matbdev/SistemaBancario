package br.univates.sistemabancario.controller.correntista;

import java.util.ArrayList;

import br.univates.alexandria.exceptions.DataBaseException;
import br.univates.alexandria.exceptions.RecordNotFoundException;
import br.univates.alexandria.exceptions.RecordNotReady;
import br.univates.alexandria.interfaces.IDao;
import br.univates.alexandria.models.CPF;
import br.univates.alexandria.models.Pessoa;
import br.univates.sistemabancario.controller.elements.PessoaComboBoxController;
import br.univates.sistemabancario.model.ContaBancaria;
import br.univates.sistemabancario.view.tela.correntista.PainelRemoverUsuario;

/**
 * Controller para o PainelRemoverUsuario.
 * Ele gerencia a lógica do painel.
 */
public class PainelRemoverUsuarioController {
    private final PainelRemoverUsuario view;
    private final IDao<Pessoa, CPF> cdao;
    private final IDao<ContaBancaria, Integer> cbdao;
    private final PessoaComboBoxController pbbController;

    public PainelRemoverUsuarioController(IDao<Pessoa, CPF> cdao, IDao<ContaBancaria, Integer> cbdao, PainelRemoverUsuario view) {
        this.view = view;
        this.cdao = cdao;
        this.cbdao = cbdao;
        
        this.pbbController = new PessoaComboBoxController(
            view.getCbCorrentista(),
            cdao
        );
        
        this.view.adicionarAcaoBotao(e -> removerUsuario());
    }
    
    /**
     * Busca e filtra a lista de correntistas que não possuem conta bancária
     * @return lista de correntistas sem conta.
     * @throws DataBaseException - erro de conexão com a base de dados
     * @throws RecordNotReady - algum erro de atributo
     */
    public ArrayList<Pessoa> getCorrentistasSemConta() throws DataBaseException, RecordNotReady {
        ArrayList<Pessoa> todosCorrentistas = this.cdao.readAll();
        
        ArrayList<Pessoa> correntistasSemConta = new ArrayList<>();
        
        for (Pessoa pessoa : todosCorrentistas){
            ArrayList<ContaBancaria> cbList = cbdao.readAll(
                conta -> conta.getPessoa().equals(pessoa)
            );

            if (cbList.isEmpty()) {
                correntistasSemConta.add(pessoa);
            }
        }
        
        // CbCorrentista burlado
        return correntistasSemConta;
    }

    /**
     * Carrega os dados no ComboBox, exibindo apenas correntistas sem conta.
     * @throws DataBaseException - erro de conexão com o banco de dados
     * @throws RecordNotReady - erro de atributo no registro
     */
    public void carregarDados() throws RecordNotReady, DataBaseException {
        ArrayList<Pessoa> correntistasSemConta = getCorrentistasSemConta();
        this.pbbController.setDados(correntistasSemConta);
    }
    
    /**
     * Lógica de remover usuário
     */
    private void removerUsuario() {
        try {
            Pessoa correntista = this.pbbController.getPessoaSelecionada();
            
            if (correntista == null) {
                throw new IllegalArgumentException("Por favor, selecione um correntista.");
            }

            this.cdao.delete(correntista);
            this.view.exibirSucesso("Correntista deletado com sucesso!");
            this.carregarDados();
            
        } catch (RecordNotFoundException e) {
            this.view.exibirErro("O usuário não foi encontrado");
        } catch (DataBaseException | RecordNotReady e) {
            this.view.exibirErro("Erro de banco de dados: " + e.getMessage());
        } catch (IllegalArgumentException e) {
            this.view.exibirErro(e.getMessage());
        }
    }
}