package br.univates.sistemabancario.controller.correntista;

import br.univates.alexandria.exceptions.CpfInvalidoException;
import br.univates.alexandria.exceptions.DataBaseException;
import br.univates.alexandria.exceptions.DuplicatedKeyException;
import br.univates.alexandria.interfaces.IDao;
import br.univates.alexandria.models.CPF;
import br.univates.alexandria.models.Pessoa;
import br.univates.sistemabancario.view.tela.correntista.PainelCadastroUsuario;

/**
 * Controller responsável por gerenciar o painel de cadastro de usuário
 * @author mateus.brambilla
 */
public class PainelCadastroUsuarioController {
    private final IDao<Pessoa, CPF> cdao;
    private final PainelCadastroUsuario view;
    
    public PainelCadastroUsuarioController(IDao<Pessoa, CPF> cdao, PainelCadastroUsuario view){
        this.view = view;
        this.cdao = cdao;
        this.view.adicionarAcaoBotao(e -> cadastrarUsuario());
    }
     
    /**
     * Lógica de cadastrar usuário
     */
    private void cadastrarUsuario() {
        try {
            String nome = this.view.getNome().getText();
            CPF cpf = this.view.getCpf().getCpf();
            String endereco = this.view.getEndereco().getText();
            
            if (cpf == null){
                throw new CpfInvalidoException("");
            }

            Pessoa pessoa = new Pessoa(cpf, nome, endereco);
            cdao.create(pessoa);
            
            this.view.exibirSucesso("Correntista adicionado com sucesso!");

            this.view.getNome().setText("");
            this.view.getCpf().setText("");
            this.view.getEndereco().setText("");
        } catch (CpfInvalidoException e) {
            this.view.exibirErro("O CPF informado é inválido.");
        } catch (DuplicatedKeyException e) {
            this.view.exibirErro("Já existe um correntista com este CPF.");
        } catch (DataBaseException e) {
            this.view.exibirErro("Erro de banco de dados: " + e.getMessage());
        } catch (Exception e) {
            this.view.exibirErro("Erro inesperado: " + e.getMessage());
        }
    }
}
