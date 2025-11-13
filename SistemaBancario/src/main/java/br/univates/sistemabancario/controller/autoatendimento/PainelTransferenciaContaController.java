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
import br.univates.sistemabancario.view.tela.autoatendimento.PainelTransferenciaConta;
import java.util.Calendar;

/**
 * Controller responsável por administrar a tela de movimentações
 * @author mateus.brambilla
 */
public class PainelTransferenciaContaController {    
    private final PainelTransferenciaConta view;
    private final PainelCorrentistaContaBancariaController pccControllerOrigem;
    private final PainelCorrentistaContaBancariaController pccControllerDestino;
    private final IDao<ContaBancaria, Integer> cbdao;
    private final IDaoTransacao tdao;
    private final Calendar c = Calendar.getInstance();
    
    public PainelTransferenciaContaController(
            IDao<ContaBancaria, Integer> cbdao, 
            IDao<Pessoa, CPF> cdao, 
            IDaoTransacao tdao, 
            PainelTransferenciaConta view
    ){
        this.tdao = tdao;
        this.view = view;
        this.cbdao = cbdao;
        
        this.pccControllerOrigem = new PainelCorrentistaContaBancariaController(
                view.getPainelCorrentistaContaBancariaOrigem(),
                cdao,
                cbdao
        ); 
        
        this.pccControllerDestino = new PainelCorrentistaContaBancariaController(
                view.getPainelCorrentistaContaBancariaDestino(),
                cdao,
                cbdao
        );
        
        this.view.adicionarAcaoBotao(e -> transferirValor());
        carregarDadosCombobox();
    }
    
    /**
     * Carrega os dados dos comboboxes personalizados
     */
    public void carregarDadosCombobox() {
        try {
            this.pccControllerOrigem.carregarDados();
            this.pccControllerDestino.carregarDados();
        } catch (RecordNotReady | DataBaseException ex) {
            this.view.exibirErro("Falha ao carregar correntistas: " + ex.getMessage());
        }
    }
    
    
    /**
     * Método responsável por depositar um valor na conta selecionada
     */
    private void transferirValor() {
        Double dQtde;
        ContaBancaria cbOrigem;
        ContaBancaria cbDestino;
        Transacao tOrigem;
        Transacao tDestino;

        try {
            dQtde = this.view.getQuantidadeTf().getDouble();
            
            cbOrigem = this.pccControllerOrigem.getContaSelecionada();
            cbDestino = this.pccControllerDestino.getContaSelecionada();
            
            if (cbOrigem == null || cbDestino == null){
                throw new IllegalArgumentException("Escolha duas contas");
            }
            
            if (cbOrigem == cbDestino){
                throw new IllegalArgumentException("Escolha duas contas diferentes");
            }
            
            cbOrigem.sacarValor(dQtde);
            cbDestino.depositaValor(dQtde);
            
            tOrigem = new Transacao(
                    dQtde,
                    cbOrigem.getSaldo(),
                    Transacao.DEFAULT_DESC,
                    Transacao.MOV_DEBITO,
                    c.getTime(),
                    cbOrigem.getNumeroConta()
            );
            
            tDestino = new Transacao(
                    dQtde,
                    cbDestino.getSaldo(),
                    Transacao.DEFAULT_DESC,
                    Transacao.MOV_CREDITO,
                    c.getTime(),
                    cbDestino.getNumeroConta()
            );
            
        } catch (SaldoInvalidoException | IllegalArgumentException e){
            this.view.exibirErro(e.getMessage());
            return;
        }

        // Lógica de banco de dados
        DataBaseConnectionManager db = null;
        try{
            db = DAOFactory.getDataBaseConnectionManager();

            // Uma única grande operação
            db.runSQL("BEGIN TRANSACTION;");
            
            this.tdao.create(tOrigem, db);
            this.tdao.create(tDestino, db);
            this.cbdao.update(cbOrigem, db);
            this.cbdao.update(cbDestino, db);

            // Em caso de sucesso
            db.runSQL("COMMIT;");

            this.view.exibirSucesso("Sucesso na transação!");
            this.view.exibirSucesso("Conta de origem: \n\n" + cbOrigem.consultarStatus());
            this.view.exibirSucesso("Conta de destino: \n\n" + cbDestino.consultarStatus());
            
            this.view.getQuantidadeTf().setDouble(0);
            this.view.getPainelCorrentistaContaBancariaOrigem().getCbContaBancaria().setSelecionado(null);
            this.view.getPainelCorrentistaContaBancariaOrigem().getCbCorrentista().setSelecionado(null);
            this.view.getPainelCorrentistaContaBancariaDestino().getCbContaBancaria().setSelecionado(null);
            this.view.getPainelCorrentistaContaBancariaDestino().getCbCorrentista().setSelecionado(null);

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
                cbOrigem.depositaValor(dQtde);
                cbDestino.sacarValor(dQtde);
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
