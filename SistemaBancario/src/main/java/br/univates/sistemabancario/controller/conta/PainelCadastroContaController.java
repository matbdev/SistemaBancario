package br.univates.sistemabancario.controller.conta;

import br.univates.alexandria.exceptions.DataBaseException;
import br.univates.alexandria.exceptions.DuplicatedKeyException;
import br.univates.alexandria.exceptions.RecordNotReady;
import br.univates.alexandria.interfaces.IDao;
import br.univates.alexandria.models.CPF;
import br.univates.alexandria.models.Pessoa;
import br.univates.sistemabancario.controller.elements.PessoaComboBoxController;
import br.univates.sistemabancario.exceptions.SaldoInvalidoException;
import br.univates.sistemabancario.model.ContaBancaria;
import br.univates.sistemabancario.model.ContaBancariaEspecial;
import br.univates.sistemabancario.view.tela.conta.PainelCadastroConta;

/**
 * Controller responsável por gerenciar o painel de cadastro de usuário
 * @author mateus.brambilla
 */
public class PainelCadastroContaController {
    private final IDao<ContaBancaria, Integer> cbdao;
    private final PainelCadastroConta view;
    private final PessoaComboBoxController pbbController;
    
    public PainelCadastroContaController(
            IDao<ContaBancaria, Integer> cbdao, 
            IDao<Pessoa, CPF> cdao, 
            PainelCadastroConta view
    ){
        this.view = view;
        this.cbdao = cbdao;
        this.view.adicionarAcaoBotao(e -> cadastrarUsuario());
        
        this.pbbController = new PessoaComboBoxController(
            view.getCbCorrentista(),
            cdao
        );
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
     * Lógica de cadastrar usuário
     */
    private void cadastrarUsuario() {
        try {
            Pessoa correntista = (Pessoa) this.view.getCbCorrentista().getSelectedItem();
            if (correntista == null) {
                throw new IllegalArgumentException("Por favor, selecione um correntista.");
            }

            String limiteStr = this.view.getTfLimite().getText();

            // TypeCasting
            double limite = limiteStr.isBlank() ? 0.0 : Double.parseDouble(limiteStr);

            ContaBancaria cb;
            if (limite != 0) { // conta especial
                cb = new ContaBancariaEspecial(correntista, limite, 0);
            } else { // conta normal
                cb = new ContaBancaria(correntista, 0);
            }

            cbdao.create(cb);
            
            this.view.exibirSucesso("Conta bancária adicionada com sucesso!");

            this.view.getTfLimite().setText("");
            this.view.getCbCorrentista().setSelectedIndex(0);

        } catch (DataBaseException e) {
            this.view.exibirErro("Erro de conexão com o banco de dados: " + e.getMessage());
        } catch (DuplicatedKeyException e) {
            this.view.exibirErro("Uma conta com este número já existe no banco de dados");
        } catch (NumberFormatException | SaldoInvalidoException e) {
            this.view.exibirErro("O limite e o saldo deve ser um valor numérico válido!");
        } catch (IllegalArgumentException e) {
            this.view.exibirErro(e.getMessage());
        }
    }
}
