package br.univates.sistemabancario.controller.correntista;

import br.univates.alexandria.exceptions.DataBaseException;
import br.univates.alexandria.exceptions.RecordNotFoundException;
import br.univates.alexandria.exceptions.RecordNotReady;
import br.univates.alexandria.interfaces.IDao;
import br.univates.alexandria.models.CPF;
import br.univates.alexandria.models.Pessoa;
import br.univates.sistemabancario.controller.elements.PessoaComboBoxController;
import br.univates.sistemabancario.view.tela.correntista.PainelRemoverUsuario;

/**
 * Controller para o PainelRemoverUsuario.
 * Ele gerencia a lógica do painel.
 */
public class PainelRemoverUsuarioController {
    private final PainelRemoverUsuario view;
    private final IDao<Pessoa, CPF> cdao;
    private final PessoaComboBoxController pbbController;

    public PainelRemoverUsuarioController(IDao<Pessoa, CPF> cdao, PainelRemoverUsuario view) {
        this.view = view;
        this.cdao = cdao;
        
        this.pbbController = new PessoaComboBoxController(
            view.getCbCorrentista(),
            cdao
        );
        
        this.view.adicionarAcaoBotao(e -> removerUsuario());
        carregarDados();
    }
    
    /**
     * Carrega os dados no ComboBox usando o controller focado.
     */
    public void carregarDados() {
        try {
            this.pbbController.carregarDados();
        } catch (RecordNotReady | DataBaseException ex) {
            this.view.exibirErro("Falha ao carregar correntistas: " + ex.getMessage());
        }
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
        } catch (DataBaseException e) {
            this.view.exibirErro("Erro de banco de dados: " + e.getMessage());
        } catch (IllegalArgumentException e) {
            this.view.exibirErro(e.getMessage());
        }
    }
}