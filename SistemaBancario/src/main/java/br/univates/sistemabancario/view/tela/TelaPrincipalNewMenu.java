package br.univates.sistemabancario.view.tela;

import javax.swing.SwingUtilities;

import br.univates.alexandria.util.Messages;
import br.univates.sistemabancario.repository.ContaBancariaDAO;
import br.univates.sistemabancario.repository.CorrentistaDAO;
import br.univates.sistemabancario.view.menu.MenuContasCorrentistas;

/**
 * Tela que serve para garantir estrutura da arquitetura do projeto
 * @author mateus.brambilla
 */
public class TelaPrincipalNewMenu {
    public void iniciarMenuContas(){
        // Ingestão de dependências
        CorrentistaDAO cdao = new CorrentistaDAO();
        ContaBancariaDAO cbdao = new ContaBancariaDAO(cdao);

        SwingUtilities.invokeLater(() -> {
            MenuContasCorrentistas m = new MenuContasCorrentistas(cdao, cbdao);
            m.exibir();
            Messages.infoMessage("Saindo da aplicação...");
        });
    }
}
