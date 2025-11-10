package br.univates.sistemabancario.controller;

import br.univates.alexandria.interfaces.IDao;
import br.univates.alexandria.models.CPF;
import br.univates.alexandria.models.Pessoa;
import br.univates.sistemabancario.controller.autoatendimento.PainelCorrentistaContaBancariaController;
import br.univates.sistemabancario.controller.autoatendimento.PainelMovimentacaoContaController;
import br.univates.sistemabancario.controller.autoatendimento.PainelTransferenciaContaController;
import br.univates.sistemabancario.controller.autoatendimento.PainelVisualizarExtratoController;
import br.univates.sistemabancario.controller.conta.PainelCadastroContaController;
import br.univates.sistemabancario.controller.conta.PainelVisualizarContasController;
import br.univates.sistemabancario.controller.correntista.PainelCadastroUsuarioController;
import br.univates.sistemabancario.controller.correntista.PainelEditarUsuarioController;
import br.univates.sistemabancario.controller.correntista.PainelRemoverUsuarioController;
import br.univates.sistemabancario.controller.correntista.PainelVisualizarUsuariosController;
import br.univates.sistemabancario.controller.elements.ContaBancariaComboBoxController;
import br.univates.sistemabancario.controller.elements.PessoaComboBoxController;
import br.univates.sistemabancario.repository.DAOFactory;
import br.univates.sistemabancario.repository.interfaces.IDaoTransacao;
import br.univates.sistemabancario.model.ContaBancaria;
import br.univates.sistemabancario.view.tela.autoatendimento.PainelMovimentacaoConta;
import br.univates.sistemabancario.view.tela.autoatendimento.PainelTransferenciaConta;
import br.univates.sistemabancario.view.tela.autoatendimento.PainelVisualizarExtrato;
import br.univates.sistemabancario.view.tela.conta.PainelCadastroConta;
import br.univates.sistemabancario.view.tela.conta.PainelVisualizarContas;
import br.univates.sistemabancario.view.tela.correntista.PainelCadastroUsuario;
import br.univates.sistemabancario.view.tela.correntista.PainelEditarUsuario;
import br.univates.sistemabancario.view.tela.correntista.PainelRemoverUsuario;
import br.univates.sistemabancario.view.tela.correntista.PainelVisualizarUsuarios;
import br.univates.sistemabancario.view.tela.navegacao.FramePrincipal;
import br.univates.sistemabancario.view.tela.navegacao.PainelInicial;
import javax.swing.JPanel;

/**
 * Controller principal que liga todas as telas a seus controllers
 * Também define lógica de clique nos itens do menu da tela principal
 * @author mateus.brambilla
 */
public class FramePrincipalController {
    // Elementos do frame
    private final FramePrincipal view;
    private final java.awt.CardLayout cardLayout;
    
    // DAOs
    private IDao<Pessoa, CPF> cdao;
    private IDao<ContaBancaria, Integer> cbdao;
    private IDaoTransacao tdao;
    
    // Controllers - autoatendimento
    private PainelCorrentistaContaBancariaController painelCorrentistaContaBancariaController;
    private PainelMovimentacaoContaController painelSacarController;
    private PainelMovimentacaoContaController painelDepositarController;
    private PainelVisualizarExtratoController painelVisualizarExtratoController;
    private PainelTransferenciaContaController painelTransferenciaContaController;
    
    // Controllers - conta
    private PainelCadastroContaController painelCadastroContaController;
    private PainelVisualizarContasController painelVisualizarContasController;
    
    // Controllers - correntista
    private PainelCadastroUsuarioController painelCadastroUsuarioController;
    private PainelEditarUsuarioController painelEditarUsuarioController;
    private PainelRemoverUsuarioController painelRemoverUsuarioController;
    private PainelVisualizarUsuariosController painelVisualizarUsuariosController;
    
    // Controllers - elements
    private ContaBancariaComboBoxController contaBancariaComboBoxController;
    private PessoaComboBoxController pessoaComboBoxController;
    
    // Constructor
    public FramePrincipalController(FramePrincipal view) {
        this.view = view;
        this.cardLayout = (java.awt.CardLayout) view.getPainelPrincipal().getLayout();

        try {
            this.cdao = DAOFactory.getCorrentistaDAO();
            this.cbdao = DAOFactory.getContaBancariaDAO();
            this.tdao = DAOFactory.getTransacaoDAO();

            inicializarPaineis();
            vincularAcoesMenu();
            
            cardLayout.show(view.getPainelPrincipal(), "inicio");

        } catch (Exception e) {
            System.err.println("Falha ao iniciar o aplicativo: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    /**
     * Método que inicializa todas as telas e controllers e adiciona os paineis
     * dentro do cardlayout
     */
    private void inicializarPaineis() {
        JPanel painelPrincipal = view.getPainelPrincipal();

        JPanel painelHome = new PainelInicial();
        painelPrincipal.add(painelHome, "inicio");
        
        // Paineis referentes a correntista
        PainelCadastroUsuario painelCadastroUsuario = new PainelCadastroUsuario();
        this.painelCadastroUsuarioController = new PainelCadastroUsuarioController(cdao, painelCadastroUsuario);
        painelPrincipal.add(painelCadastroUsuario, "cadastroUser");

        PainelEditarUsuario painelEditarUsuario = new PainelEditarUsuario();
        this.painelEditarUsuarioController = new PainelEditarUsuarioController(cdao, painelEditarUsuario);
        painelPrincipal.add(painelEditarUsuario, "editarUser");

        PainelRemoverUsuario painelRemoverUsuario = new PainelRemoverUsuario();
        this.painelRemoverUsuarioController = new PainelRemoverUsuarioController(cdao, painelRemoverUsuario);
        painelPrincipal.add(painelRemoverUsuario, "removerUser");
        
        PainelVisualizarUsuarios painelVisualizarUsuarios = new PainelVisualizarUsuarios();
        this.painelVisualizarUsuariosController = new PainelVisualizarUsuariosController(cdao, painelVisualizarUsuarios);
        painelPrincipal.add(painelVisualizarUsuarios, "visualizarUser");
        
        
        // Paineis referentes a conta bancária
        PainelCadastroConta painelCadastroConta = new PainelCadastroConta();
        this.painelCadastroContaController = new PainelCadastroContaController(cbdao, cdao, painelCadastroConta);
        painelPrincipal.add(painelCadastroConta, "cadastroConta");

        PainelVisualizarContas painelVisualizarContas = new PainelVisualizarContas();
        this.painelVisualizarContasController = new PainelVisualizarContasController(cbdao, painelVisualizarContas);
        painelPrincipal.add(painelVisualizarContas, "visualizarContas");
        
        
        // Paineis referentes a autoatendimento
        PainelMovimentacaoConta painelMovimentacaoContaSacar = new PainelMovimentacaoConta();
        this.painelSacarController = new PainelMovimentacaoContaController(cbdao, cdao, tdao, painelMovimentacaoContaSacar, PainelMovimentacaoContaController.OPERACAO_DEBITO);
        painelPrincipal.add(painelMovimentacaoContaSacar, "movimentacaoContaSacar");

        PainelMovimentacaoConta painelMovimentacaoContaDepositar = new PainelMovimentacaoConta();
        this.painelDepositarController = new PainelMovimentacaoContaController(cbdao, cdao, tdao, painelMovimentacaoContaDepositar, PainelMovimentacaoContaController.OPERACAO_CREDITO); // <--- CORREÇÃO AQUI
        painelPrincipal.add(painelMovimentacaoContaDepositar, "movimentacaoContaDepositar");

        PainelVisualizarExtrato painelVisualizarExtrato = new PainelVisualizarExtrato();
        this.painelVisualizarExtratoController = new PainelVisualizarExtratoController(cbdao, cdao, tdao, painelVisualizarExtrato);
        painelPrincipal.add(painelVisualizarExtrato, "visualizarExtrato");
        
        PainelTransferenciaConta painelTransferenciaConta = new PainelTransferenciaConta();
        this.painelTransferenciaContaController = new PainelTransferenciaContaController(cbdao, cdao, tdao, painelTransferenciaConta);
        painelPrincipal.add(painelTransferenciaConta, "transferirConta");
    }
    
    /**
     * Método que vincula os cliques nos itens do menu a uma ação (mostrar um painel)
     */
    private void vincularAcoesMenu() {
        // Navegação
        view.getPaginaInicialMenuItem().addActionListener(e -> cardLayout.show(view.getPainelPrincipal(), "inicio"));
        view.getSairMenuItem().addActionListener(e -> System.exit(0));
        
        // Correntista
        view.getCadastroCorrentistaMenuItem().addActionListener(e -> cardLayout.show(view.getPainelPrincipal(), "cadastroUser"));
        view.getEditarCorrentistaMenuItem().addActionListener(e -> this.painelEditarUsuarioController.carregarDados());
        view.getDeletarCorrentistaMenuItem().addActionListener(e -> cardLayout.show(view.getPainelPrincipal(), "removerUser"));

        view.getVisualizarCorrentistasMenuItem().addActionListener(e -> {
            try {
                this.painelVisualizarUsuariosController.carregarDados();
            } catch (Exception ex) {
                view.exibirErro("Erro ao carregar dados: " + ex.getMessage());
            }
            cardLayout.show(view.getPainelPrincipal(), "visualizarUser");
        });

        // Conta Bancária
        view.getAdicionarContaMenuItem().addActionListener(e -> cardLayout.show(view.getPainelPrincipal(), "cadastroConta"));

        view.getListarContasMenuItem().addActionListener(e -> {
            try {
                this.painelVisualizarContasController.carregarDados();
            } catch (Exception ex) {
                view.exibirErro("Erro ao carregar dados: " + ex.getMessage());
            }
            cardLayout.show(view.getPainelPrincipal(), "visualizarContas");
        });

        // Autoatendimento
        view.getDepositarMenuItem().addActionListener(e -> cardLayout.show(view.getPainelPrincipal(), "movimentacaoContaDepositar"));
        view.getPagarMenuItem().addActionListener(e -> cardLayout.show(view.getPainelPrincipal(), "movimentacaoContaSacar"));
        view.getTransferenciaMenuItem().addActionListener(e -> cardLayout.show(view.getPainelPrincipal(), "transferirConta"));

        view.getExtratoMenuItem().addActionListener(e -> {
            try {
                this.painelVisualizarExtratoController.carregarDados();
            } catch (Exception ex) {
                view.exibirErro("Erro ao carregar dados: " + ex.getMessage());
            }
            cardLayout.show(view.getPainelPrincipal(), "visualizarExtrato");
        });
    }
}
