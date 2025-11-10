package br.univates.sistemabancario.controller.correntista;

import br.univates.alexandria.exceptions.CpfInvalidoException;
import br.univates.alexandria.exceptions.DataBaseException;
import br.univates.alexandria.exceptions.RecordNotFoundException;
import br.univates.alexandria.exceptions.RecordNotReady;
import br.univates.alexandria.interfaces.IDao;
import br.univates.alexandria.models.CPF;
import br.univates.alexandria.models.Pessoa;
import br.univates.sistemabancario.controller.elements.PessoaComboBoxController;
import br.univates.sistemabancario.view.tela.correntista.PainelEditarUsuario;

/**
 * Controller para o painel de ediçäo de usuário.
 * Ele gerencia a lógica do painel.
 */
public class PainelEditarUsuarioController {
    private final PainelEditarUsuario view;
    private final IDao<Pessoa, CPF> cdao;
    private final PessoaComboBoxController pbbController;

    public PainelEditarUsuarioController(IDao<Pessoa, CPF> cdao, PainelEditarUsuario view) {
        this.view = view;
        this.cdao = cdao;
        
        this.pbbController = new PessoaComboBoxController(
            view.getCbCorrentista(),
            cdao
        );
        
        this.view.adicionarAcaoBotao(e -> editarUsuario());
        this.view.adicionarAcaoCombobox(e -> carregarDadosUsuario());
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
    
    // Carrega os dados do usuário com base na seleção
    private void carregarDadosUsuario() {
        Pessoa correntista = this.pbbController.getPessoaSelecionada();
        
        if (correntista != null) {
            this.view.getEndereco().setText(correntista.getEndereco());
            this.view.getNome().setText(correntista.getNome());
            this.view.getCpf().setText(correntista.getCpfFormatado());
        } else {
            this.view.getEndereco().setText("");
            this.view.getEndereco().setEditable(false);
            this.view.getNome().setText("");
            this.view.getNome().setEditable(false);
            this.view.getCpf().setText("");
        }
    }
    
    /**
     * Lógica de editar usuário
     */
    private void editarUsuario() {
        try {
            Pessoa correntistaSelecionado = this.pbbController.getPessoaSelecionada();
            
            if (correntistaSelecionado == null) {
                throw new IllegalArgumentException("Por favor, selecione um correntista.");
            }

            String nome = this.view.getNome().getText();
            String endereco = this.view.getEndereco().getText();

            if (nome.isBlank() && endereco.isBlank()) {
                throw new IllegalArgumentException("Altere pelo menos um dos campos.");
            }

            Pessoa p = new Pessoa(correntistaSelecionado.getCpfNumbers(), nome, endereco);
            this.cdao.update(p);
            
            this.view.exibirSucesso("Correntista editado com sucesso!");

            this.pbbController.carregarDados();
            Pessoa pAtualizada = cdao.read(p.getCPF());
            this.pbbController.setPessoaSelecionada(pAtualizada);
            
        } catch (CpfInvalidoException e) {
            this.view.exibirErro("O CPF é inválido.");
        } catch (RecordNotFoundException e) {
            this.view.exibirErro("Correntista não encontrado para atualizar.");
        } catch (DataBaseException | RecordNotReady e) {
            this.view.exibirErro("Erro de banco de dados: " + e.getMessage());
        } catch (IllegalArgumentException e) {
            this.view.exibirErro(e.getMessage());
        }
    }
}     