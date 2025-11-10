package br.univates.sistemabancario.controller.correntista;

import br.univates.alexandria.exceptions.DataBaseException;
import br.univates.alexandria.exceptions.RecordNotReady;
import br.univates.alexandria.interfaces.IDao;
import br.univates.alexandria.models.CPF;
import br.univates.alexandria.models.Pessoa;
import br.univates.sistemabancario.view.elements.tables.PessoaTableModel;
import br.univates.sistemabancario.view.tela.correntista.PainelVisualizarUsuarios;

/**
 * Controller do painel de visualização de usuários cadastrados
 * @author mateus.brambilla
 */
public class PainelVisualizarUsuariosController {
    private final IDao<Pessoa, CPF> cdao;
    private final PainelVisualizarUsuarios view;
    
    public PainelVisualizarUsuariosController(IDao<Pessoa, CPF> cdao, PainelVisualizarUsuarios view) {
        this.cdao = cdao;
        this.view = view;
    }
    
    /**
     * Busca os dados no DAO e os carrega na JTable da View.
     * @throws DataBaseException - erro de conexão com o banco de dados
     * @throws RecordNotReady - erro de atributo no registro
     */
    public void carregarDados() throws DataBaseException, RecordNotReady{
        PessoaTableModel tableModel = new PessoaTableModel(cdao.readAll());
        view.getTable().setModel(tableModel); 
    }
}
