package br.univates.sistemabancario.controller;

import br.univates.alexandria.exceptions.DataBaseException;
import br.univates.alexandria.exceptions.RecordNotReady;
import br.univates.alexandria.interfaces.IDao;
import br.univates.alexandria.models.CPF;
import br.univates.alexandria.models.Pessoa;
import br.univates.alexandria.util.Messages;
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

import java.util.ArrayList;

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

        PainelInicial painelHome = new PainelInicial();
        painelHome.adicionarAcaoBotao(e -> {
            Messages.infoMessage(painelHome, "Saindo da aplicação...");
            System.exit(0);
        });
        painelPrincipal.add(painelHome, "inicio");
        
        // Paineis referentes a correntista
        PainelCadastroUsuario painelCadastroUsuario = new PainelCadastroUsuario();
        this.painelCadastroUsuarioController = new PainelCadastroUsuarioController(cdao, painelCadastroUsuario);
        painelPrincipal.add(painelCadastroUsuario, "cadastroUser");

        PainelEditarUsuario painelEditarUsuario = new PainelEditarUsuario();
        this.painelEditarUsuarioController = new PainelEditarUsuarioController(cdao, painelEditarUsuario);
        painelPrincipal.add(painelEditarUsuario, "editarUser");

        PainelRemoverUsuario painelRemoverUsuario = new PainelRemoverUsuario();
        this.painelRemoverUsuarioController = new PainelRemoverUsuarioController(cdao, cbdao, painelRemoverUsuario);
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
        
        // CORRENTISTA
        // Cadastrar
        view.getCadastroCorrentistaMenuItem().addActionListener(e -> cardLayout.show(view.getPainelPrincipal(), "cadastroUser"));

        // Editar
        view.getEditarCorrentistaMenuItem().addActionListener(e -> {
            try{
                verificarCorrentistasVazio();
                this.painelEditarUsuarioController.carregarDados();
                cardLayout.show(view.getPainelPrincipal(), "editarUser");
            } catch (DataBaseException | RecordNotReady | RuntimeException ex) {
                view.exibirErro("Erro ao verificar correntistas: " + ex.getMessage());
            }
        });

        // Deletar
        view.getDeletarCorrentistaMenuItem().addActionListener(e -> {
            try {
                ArrayList<Pessoa> correntistasParaRemover = this.painelRemoverUsuarioController.getCorrentistasSemConta();
                
                if (correntistasParaRemover.isEmpty()) {
                    view.exibirErro("Não há correntistas sem conta para remover.");
                } else {
                    this.painelRemoverUsuarioController.carregarDados();
                    cardLayout.show(view.getPainelPrincipal(), "removerUser");
                }
            } catch (DataBaseException | RecordNotReady | RuntimeException ex) {
                view.exibirErro("Erro ao verificar correntistas: " + ex.getMessage());
            }
        });

        // Visualizar
        view.getVisualizarCorrentistasMenuItem().addActionListener(e -> {
            try {
                verificarCorrentistasVazio();
                this.painelVisualizarUsuariosController.carregarDados();
                cardLayout.show(view.getPainelPrincipal(), "visualizarUser");
            } catch (DataBaseException | RecordNotReady | RuntimeException ex) {
                view.exibirErro("Erro ao carregar dados: " + ex.getMessage());
            }
        });

        // CONTA BANCÁRIA
        // Cadastro
        view.getAdicionarContaMenuItem().addActionListener(e -> {
            try{
                verificarCorrentistasVazio();
                this.painelCadastroContaController.carregarDados();
                cardLayout.show(view.getPainelPrincipal(), "cadastroConta");
            } catch (DataBaseException | RecordNotReady |RuntimeException ex) {
                view.exibirErro("Erro ao verificar correntistas: " + ex.getMessage());
            }
        });

        // Visualização
        view.getListarContasMenuItem().addActionListener(e -> {
            try {
                verificarContasVazio();
                this.painelVisualizarContasController.carregarDados();
                cardLayout.show(view.getPainelPrincipal(), "visualizarContas");
            } catch (DataBaseException | RecordNotReady | RuntimeException ex) {
                view.exibirErro("Erro ao carregar dados: " + ex.getMessage());
            }
        });

        // AUTOATENDIMENTO
        // Depósito
        view.getDepositarMenuItem().addActionListener(e -> {
            try{
                verificarContasVazio();
                cardLayout.show(view.getPainelPrincipal(), "movimentacaoContaDepositar");
            } catch (DataBaseException | RecordNotReady | RuntimeException ex) {
                view.exibirErro("Erro ao carregar dados: " + ex.getMessage());
            }
        });

        // Saque
        view.getPagarMenuItem().addActionListener(e -> {
            try{
                verificarContasVazio();
                cardLayout.show(view.getPainelPrincipal(), "movimentacaoContaSacar");
            } catch (DataBaseException | RecordNotReady | RuntimeException ex) {
                view.exibirErro("Erro ao carregar dados: " + ex.getMessage());
            }
        });

        // Transferência
        view.getTransferenciaMenuItem().addActionListener(e -> {
            try{
                ArrayList<ContaBancaria> cbList = cbdao.readAll();
        
                if (cbList.size() < 2) {
                    throw new RuntimeException("Não há contas bancárias o suficiente para transferência.");
                }
                cardLayout.show(view.getPainelPrincipal(), "transferirConta");
            } catch (DataBaseException | RecordNotReady | RuntimeException ex) {
                view.exibirErro("Erro ao carregar dados: " + ex.getMessage());
            }
        });

        // Extrato
        view.getExtratoMenuItem().addActionListener(e -> {
            try {
                verificarContasVazio();
                this.painelVisualizarExtratoController.carregarDados();
                cardLayout.show(view.getPainelPrincipal(), "visualizarExtrato");
            } catch (DataBaseException | RecordNotReady | RuntimeException ex) {
                view.exibirErro("Erro ao carregar dados: " + ex.getMessage());
            }
        });
    }

    /**
     * Método auxiliar para verificação de correntista
     * Verifica se há algum correntista cadastrado
     * @throws DataBaseException - erro de conexão com o banco de dados
     * @throws RecordNotReady - erro de atributo no registro
     */
    private void verificarCorrentistasVazio() throws DataBaseException, RecordNotReady{
        ArrayList<Pessoa> cList = cdao.readAll();
        
        if (cList.isEmpty()) {
            throw new RuntimeException("Não há correntistas para exibir.");
        }
    }

    /**
     * Método auxiliar para verificação de conta
     * Verifica se há alguma conta bancária cadastrada
     * @throws DataBaseException - erro de conexão com o banco de dados
     * @throws RecordNotReady - erro de atributo no registro
     */
    private void verificarContasVazio() throws DataBaseException, RecordNotReady{
        ArrayList<ContaBancaria> cbList = cbdao.readAll();
        
        if (cbList.isEmpty()) {
            throw new RuntimeException("Não há contas bancárias para exibir.");
        }
    }
}
