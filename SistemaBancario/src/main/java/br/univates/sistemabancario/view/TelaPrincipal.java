package br.univates.sistemabancario.view;

import br.univates.alexandria.util.Messages;
import br.univates.sistemabancario.repository.ContaBancariaDAO;
import br.univates.sistemabancario.repository.CorrentistaDAO;

/**
 * Tela que serve para garantir estrutura da arquitetura do projeto
 * @author mateus.brambilla
 */
public class TelaPrincipal {
    public void iniciarMenuContas(){
        // Ingestão de dependências
        CorrentistaDAO cdao = new CorrentistaDAO();
        ContaBancariaDAO cbdao = new ContaBancariaDAO(cdao);

        MenuContasCorrentistas m = new MenuContasCorrentistas(cdao, cbdao);
        m.gerarMenu();

        Messages.infoMessage("Saindo da aplicação...");
    }
}
