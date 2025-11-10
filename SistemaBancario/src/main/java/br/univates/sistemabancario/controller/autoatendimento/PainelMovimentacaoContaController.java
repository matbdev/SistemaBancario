package br.univates.sistemabancario.controller.autoatendimento;

import br.univates.alexandria.exceptions.DataBaseException;
import br.univates.alexandria.exceptions.RecordNotFoundException;
import br.univates.alexandria.exceptions.RecordNotReady;
import br.univates.alexandria.interfaces.IDao;
import br.univates.alexandria.models.CPF;
import br.univates.alexandria.models.Pessoa;
import br.univates.alexandria.repository.DataBaseConnectionManager;
import br.univates.sistemabancario.exceptions.SaldoInvalidoException;
import br.univates.sistemabancario.repository.DAOFactory;
import br.univates.sistemabancario.repository.interfaces.IDaoTransacao;
import br.univates.sistemabancario.model.ContaBancaria;
import br.univates.sistemabancario.model.Transacao;
import br.univates.sistemabancario.view.tela.autoatendimento.PainelMovimentacaoConta;
import java.util.Calendar;

/**
 * Controller responsável por administrar a tela de movimentações
 * @author mateus.brambilla
 */
public class PainelMovimentacaoContaController {
    // Constantes
    public static final int OPERACAO_CREDITO = 1;
    public static final int OPERACAO_DEBITO = 0;
    
    private final PainelMovimentacaoConta view;
    private final PainelCorrentistaContaBancariaController pccController;
    private final IDao<ContaBancaria, Integer> cbdao;
    private final IDaoTransacao tdao;
    private final Calendar c = Calendar.getInstance();
    
    public PainelMovimentacaoContaController(
            IDao<ContaBancaria, Integer> cbdao, 
            IDao<Pessoa, CPF> cdao, 
            IDaoTransacao tdao, 
            PainelMovimentacaoConta view, 
            int operacao
    ){
        this.tdao = tdao;
        this.view = view;
        this.cbdao = cbdao;
        
        this.pccController = new PainelCorrentistaContaBancariaController(
                view.getPainelCorrentistaContaBancaria(),
                cdao,
                cbdao
        ); 
        
        // Verifica validade da operação
        if(operacao > 1){
            throw new IllegalArgumentException("Indicador inválido fornecido");
        } else {
            this.view.adicionarAcaoBotao(e -> realizarMovimentacao(operacao));

            if(operacao == 0){
                this.view.getButton().setText("Sacar");
                this.view.getTitleLabel().setText("Sacar");
                
                this.view.getPainelCorrentistaContaBancaria().getCbContaBancaria().addActionListener(
                    e -> {
                        ContaBancaria cb = this.pccController.getContaSelecionada();
                                
                        if (cb == null){
                            this.view.getTitleLabel().setText("Sacar");
                        } else {
                            this.view.getTitleLabel().setText("Sacar | Conta: " + cb.getNumeroConta().getNumero());
                        }
                    }
                );
            } else {
                this.view.getButton().setText("Depositar");
                this.view.getTitleLabel().setText("Depositar");
                
                this.view.getPainelCorrentistaContaBancaria().getCbContaBancaria().addActionListener(
                    e -> {
                        ContaBancaria cb = this.pccController.getContaSelecionada();
                                
                        if (cb == null){
                            this.view.getTitleLabel().setText("Depositar");
                        } else {
                            this.view.getTitleLabel().setText("Depositar | Conta: " + cb.getNumeroConta().getNumero());
                        }
                    }
                );
            }
        }
        
        carregarDadosCombobox();
    }
    
    /**
     * Carrega os dados dos comboboxes personalizados
     */
    public void carregarDadosCombobox() {
        try {
            this.pccController.carregarDados();
        } catch (RecordNotReady | DataBaseException ex) {
            this.view.exibirErro("Falha ao carregar correntistas: " + ex.getMessage());
        }
    }

    /**
     * Classe auxiliar que representa os dados de uma movimentação
     */
    private class InfoMovimentacao {
        private Transacao t;
        private ContaBancaria cb;

        public InfoMovimentacao(Transacao t, ContaBancaria cb){
            this.t = t;
            this.cb = cb;
        }

        // Getters
        public Transacao getTransacao(){
            return this.t;
        }

        public ContaBancaria getContaBancaria(){
            return this.cb;
        }
    }

    /**
     * Método auxiliar que unifica as chamadas de métodos de movimentação
     */
    private void realizarMovimentacao(int operacao){
        // Retorna uma conta e uma transação
        InfoMovimentacao im = null;

        if (operacao == 0) {
            im = prepararSaque();
        } else {
            im = prepararDeposito();
        }

        if (im != null){
            DataBaseConnectionManager db = null;
            ContaBancaria cb = im.getContaBancaria();
            Transacao t = im.getTransacao();
    
            try{
                db = DAOFactory.getDataBaseConnectionManager();

                // Início da transação
                db.runSQL("BEGIN TRANSACTION;");
                this.tdao.create(t, db);
                this.cbdao.update(cb, db);

                // Em caso de sucesso
                db.runSQL("COMMIT;");
                this.view.exibirSucesso("Sucesso na transação! \n\n" + cb.consultarStatus());
                this.view.getQuantidadeTf().setText("");
                this.view.getPainelCorrentistaContaBancaria().getCbContaBancaria().setSelecionado(null);
                this.view.getPainelCorrentistaContaBancaria().getCbCorrentista().setSelecionado(null);
    
            } catch (DataBaseException | RecordNotFoundException e) {
                // Em caso de erro
                this.view.exibirErro("Erro de banco de dados: " + e.getMessage());
                if (db != null) {
                    try {
                        db.runSQL("ROLLBACK;");
                    } catch (DataBaseException e2) {
                        this.view.exibirErro("Erro crítico ao reverter a transação: " + e2.getMessage());
                    }
                }
    
                // Reverte alterações em memória
                try {
                    if (operacao == 0) {
                        cb.depositaValor(t.getValor());
                    } else {
                        cb.sacarValor(t.getValor());
                    }
                } catch (SaldoInvalidoException ex) {
                    // Não acontece
                }
                
            } finally {
                // Sempre fecha a conexão
                try {
                    if (db != null) {
                        db.closeConnection();
                    }
                } catch (DataBaseException e) {
                    this.view.exibirErro("Erro crítico ao encerrar conexão com o banco: " + e.getMessage());
                }
            }
        }
    }
    
    
    /**
     * Método responsável por preparar um depósito de um valor na conta selecionada
     * @return - objeto de InfoMovimentacao com conta e transação
     */
    private InfoMovimentacao prepararDeposito() {
        try {
            String strQtd = this.view.getQuantidadeTf().getText();
            Double dQtde = Double.valueOf(strQtd);
            ContaBancaria cb = this.pccController.getContaSelecionada();
            
            if (cb == null){
                throw new IllegalArgumentException("Escolha uma conta");
            }
            
            cb.depositaValor(dQtde);
            
            Transacao t = new Transacao(
                    dQtde,
                    cb.getSaldo(),
                    Transacao.DEFAULT_DESC,
                    Transacao.MOV_CREDITO,
                    c.getTime(),
                    cb.getNumeroConta()
            );

            return new InfoMovimentacao(t, cb);

        } catch (SaldoInvalidoException | IllegalArgumentException e){
            this.view.exibirErro(e.getMessage());
        }

        return null;
    }
    
    /**
     * Método responsável por preparar um saque de um valor da conta selecionada
     * @return - objeto de InfoMovimentacao com conta e transação
     */
    private InfoMovimentacao prepararSaque() {
        try {
            String strQtd = this.view.getQuantidadeTf().getText();
            Double dQtde = Double.valueOf(strQtd);
            ContaBancaria cb = this.pccController.getContaSelecionada();
            
            if (cb == null){
                throw new IllegalArgumentException("Escolha uma conta");
            }
            
            cb.sacarValor(dQtde);
            
            Transacao t = new Transacao(
                    dQtde,
                    cb.getSaldo(),
                    Transacao.DEFAULT_DESC,
                    Transacao.MOV_DEBITO,
                    c.getTime(),
                    cb.getNumeroConta()
            );

            return new InfoMovimentacao(t, cb);

        } catch (SaldoInvalidoException | IllegalArgumentException e){
            this.view.exibirErro(e.getMessage());
        }

        return null;
    }
}
