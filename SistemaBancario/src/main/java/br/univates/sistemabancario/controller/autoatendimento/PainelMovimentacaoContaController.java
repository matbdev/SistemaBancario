package br.univates.sistemabancario.controller.autoatendimento;

import br.univates.alexandria.exceptions.DataBaseException;
import br.univates.alexandria.exceptions.RecordNotFoundException;
import br.univates.alexandria.exceptions.RecordNotReady;
import br.univates.alexandria.interfaces.IDao;
import br.univates.alexandria.models.CPF;
import br.univates.alexandria.models.Pessoa;
import br.univates.sistemabancario.exceptions.SaldoInvalidoException;
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
            if(operacao == 0){
                this.view.adicionarAcaoBotao(e -> sacarValor());
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
                this.view.adicionarAcaoBotao(e -> depositarValor());
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
     * Método responsável por depositar um valor na conta selecionada
     */
    private void depositarValor() {
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

            this.tdao.create(t);
            this.cbdao.update(cb);

            this.view.exibirSucesso("Sucesso na transação! \n\n" + cb.consultarStatus());

        } catch (DataBaseException e) {
            this.view.exibirErro("Erro de conexão com o banco de dados.");
        } catch (RecordNotFoundException e) {
            this.view.exibirErro("Conta bancária não encontrada.");
        } catch (SaldoInvalidoException | IllegalArgumentException e){
            this.view.exibirErro(e.getMessage());
        }
    }
    
    /**
     * Método responsável por sacar um valor da conta selecionada
     */
    private void sacarValor() {
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

            this.tdao.create(t);
            this.cbdao.update(cb);

            this.view.exibirSucesso("Sucesso na transação! \n\n" + cb.consultarStatus());

        } catch (DataBaseException e) {
            this.view.exibirErro("Erro de conexão com o banco de dados.");
        } catch (RecordNotFoundException e) {
            this.view.exibirErro("Conta bancária não encontrada.");
        } catch (SaldoInvalidoException | IllegalArgumentException e){
            this.view.exibirErro(e.getMessage());
        }
    }
}
