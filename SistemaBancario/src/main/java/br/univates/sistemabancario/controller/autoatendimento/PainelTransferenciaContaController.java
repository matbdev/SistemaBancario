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
        try {
            String strQtd = this.view.getQuantidadeTf().getText();
            Double dQtde = Double.valueOf(strQtd);
            
            ContaBancaria cbOrigem = this.pccControllerOrigem.getContaSelecionada();
            ContaBancaria cbDestino = this.pccControllerDestino.getContaSelecionada();
            
            if (cbOrigem == null || cbDestino == null){
                throw new IllegalArgumentException("Escolha duas contas");
            }
            
            if (cbOrigem == cbDestino){
                throw new IllegalArgumentException("Escolha duas contas diferentes");
            }
            
            cbOrigem.sacarValor(dQtde);
            cbDestino.depositaValor(dQtde);
            
            Transacao tOrigem = new Transacao(
                    dQtde,
                    cbOrigem.getSaldo(),
                    Transacao.DEFAULT_DESC,
                    Transacao.MOV_DEBITO,
                    c.getTime(),
                    cbOrigem.getNumeroConta()
            );
            
            Transacao tDestino = new Transacao(
                    dQtde,
                    cbDestino.getSaldo(),
                    Transacao.DEFAULT_DESC,
                    Transacao.MOV_CREDITO,
                    c.getTime(),
                    cbDestino.getNumeroConta()
            );

            this.tdao.create(tOrigem);
            this.tdao.create(tDestino);
            this.cbdao.update(cbOrigem);
            this.cbdao.update(cbDestino);

            this.view.exibirSucesso("Sucesso na transação!");
            this.view.exibirSucesso("Conta de origem: \n\n" + cbOrigem.consultarStatus());
            this.view.exibirSucesso("Conta de destino: \n\n" + cbDestino.consultarStatus());

        } catch (DataBaseException e) {
            this.view.exibirErro("Erro de conexão com o banco de dados.");
        } catch (RecordNotFoundException e) {
            this.view.exibirErro("Conta bancária não encontrada.");
        } catch (SaldoInvalidoException | IllegalArgumentException e){
            this.view.exibirErro(e.getMessage());
        }
    }
}
