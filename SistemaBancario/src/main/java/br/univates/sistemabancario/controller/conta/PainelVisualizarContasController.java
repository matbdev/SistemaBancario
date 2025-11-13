package br.univates.sistemabancario.controller.conta;

import br.univates.alexandria.exceptions.DataBaseException;
import br.univates.alexandria.exceptions.RecordNotReady;
import br.univates.alexandria.interfaces.IDao;
import br.univates.sistemabancario.model.ContaBancaria;
import br.univates.sistemabancario.view.components.ContaTableModel;
import br.univates.sistemabancario.view.tela.conta.PainelVisualizarContas;

/**
 * Controller do painel de visualização de usuários cadastrados
 * @author mateus.brambilla
 */
public class PainelVisualizarContasController {
    private final IDao<ContaBancaria, Integer> cbdao;
    private final PainelVisualizarContas view;
    
    public PainelVisualizarContasController(IDao<ContaBancaria, Integer> cbdao, PainelVisualizarContas view) {
        this.cbdao = cbdao;
        this.view = view;
    }
    
    /**
     * Busca os dados no DAO e os carrega no jtable
     * @throws DataBaseException - erro de conexão com o banco de dados
     * @throws RecordNotReady - erro de atributo no registro
     */
    public void carregarDados() throws DataBaseException, RecordNotReady {
        ContaTableModel tableModel = new ContaTableModel(cbdao.readAll());
        view.getTable().setModel(tableModel); 
    }
}
