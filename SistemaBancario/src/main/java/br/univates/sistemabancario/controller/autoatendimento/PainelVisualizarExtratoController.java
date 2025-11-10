package br.univates.sistemabancario.controller.autoatendimento;

import br.univates.alexandria.exceptions.DataBaseException;
import br.univates.alexandria.exceptions.RecordNotFoundException;
import br.univates.alexandria.exceptions.RecordNotReady;
import br.univates.alexandria.interfaces.IDao;
import br.univates.alexandria.models.CPF;
import br.univates.alexandria.models.Pessoa;
import br.univates.sistemabancario.repository.interfaces.IDaoTransacao;
import br.univates.sistemabancario.model.ContaBancaria;
import br.univates.sistemabancario.view.elements.tables.ExtratoTableModel;
import br.univates.sistemabancario.view.tela.autoatendimento.PainelCorrentistaContaBancaria;
import br.univates.sistemabancario.view.tela.autoatendimento.PainelVisualizarExtrato;

/**
 * Controller do painel de visualização de usuários cadastrados
 * @author mateus.brambilla
 */
public class PainelVisualizarExtratoController {
    private final PainelCorrentistaContaBancariaController pccController;
    private final IDaoTransacao tdao;
    private final PainelVisualizarExtrato view;
    PainelCorrentistaContaBancaria painel;
    
    public PainelVisualizarExtratoController(
            IDao<ContaBancaria, Integer> cbdao, 
            IDao<Pessoa, CPF> cdao, 
            IDaoTransacao tdao, 
            PainelVisualizarExtrato view
    ) {
        
        this.pccController = new PainelCorrentistaContaBancariaController(
                view.getPainelCorrentistaContaBancaria(),
                cdao,
                cbdao
        );
        
        this.tdao = tdao;
        this.view = view;
        this.painel = view.getPainelCorrentistaContaBancaria();
        this.view.getTitleLabel().setText("Movimentações");
        
        this.view.getPainelCorrentistaContaBancaria().getCbContaBancaria().addActionListener(
            e -> {
                carregarDados();
                ContaBancaria cb = this.pccController.getContaSelecionada();
                                
                if (cb == null){
                    this.view.getTitleLabel().setText("Movimentações");
                } else {
                    this.view.getTitleLabel().setText("Movimentações | Conta: " + cb.getNumeroConta().getNumero());
                }
            }
        );
        
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
     * Busca os dados no DAO e os carrega no JTable
     */
    public void carregarDados() {
        ContaBancaria contaSelecionada = painel.getCbContaBancaria().getSelecionado();
        
        if (contaSelecionada != null){
            int numeroContaInt = contaSelecionada.getNumeroConta().getNumeroInt();
            
            try {
                ExtratoTableModel tableModel = new ExtratoTableModel(tdao.read(numeroContaInt));
                view.getTable().setModel(tableModel); 
            } catch (DataBaseException | RecordNotFoundException ex) {
                view.exibirErro("Erro ao carregar usuários: " + ex.getMessage());
            }
        } else {
            view.getTable().setModel(new ExtratoTableModel(new java.util.ArrayList<>()));
        }
    }
}
