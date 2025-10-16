package br.univates.sistemabancario.view;

import java.util.ArrayList;

import javax.swing.JDialog;
import javax.swing.SwingUtilities;

import br.univates.alexandria.exceptions.NullInputException;
import br.univates.alexandria.models.Pessoa;
import br.univates.alexandria.util.Inputs;
import br.univates.alexandria.util.Messages;
import br.univates.alexandria.view.MenuDialog;
import br.univates.sistemabancario.exceptions.SaldoInvalidoException;
import br.univates.sistemabancario.repository.ContaBancariaDAO;
import br.univates.sistemabancario.repository.CorrentistaDAO;
import br.univates.sistemabancario.repository.TransacaoDAO;
import br.univates.sistemabancario.service.ContaBancaria;
import br.univates.sistemabancario.service.Transacao;

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

    /**
     * Menu que separa as funções de correntista e conta bancária
     * @author mateus.brambilla
     */
    public class MenuContasCorrentistas extends MenuDialog {
        private final CorrentistaDAO cdao;
        private final ContaBancariaDAO cbdao;

        /**
         * Construtor que inicializa a classe
         * Recebe objetos dos DAOs para preservar itens estáticos (arquivo)
         */
        public MenuContasCorrentistas(CorrentistaDAO cdao, ContaBancariaDAO cbdao){
            super(null, true);

            this.cdao = cdao;
            this.cbdao = cbdao;

            setTitulo("Menu: Fluxos");
            setSubtitulo("Escolha entre manusear correntistas ou contas");
            
            adicionarOpcoes();
        }
        
        /**
         * Método privado que adiciona as opções a este menu
         */
        private void adicionarOpcoes(){
            addOption("Correntistas", () -> {
                SwingUtilities.invokeLater(() -> {
                    MenuCorrentistas mc = new MenuCorrentistas(this.cdao, this.cbdao);
                    mc.exibir();
                });
            });
            addOption("Contas Bancárias", () -> {
                SwingUtilities.invokeLater(() -> {
                    MenuContasBancarias mcb = new MenuContasBancarias(this.cdao, this.cbdao);
                    mcb.exibir();
                });
            });
            addCustomLastOption("Encerrar o programa", () -> this.dispose());
        }
    }

    /**
     * Menu responsável por mostrar opções referentes à contas bancárias
     * @author mateus.brambilla
     */
    public class MenuContasBancarias extends MenuDialog {
        private final CorrentistaDAO cdao;
        private final ContaBancariaDAO cbdao;
        
        // Construtor
        public MenuContasBancarias(CorrentistaDAO cdao, ContaBancariaDAO cbdao){
            super(null, true);

            this.cdao = cdao;
            this.cbdao = cbdao;

            setTitulo("Menu: Conta Bancária");
            setSubtitulo("Opções relacionadas à Conta Bancária");
            adicionarOpcoes();
        }
        
        /**
         * Método privado responsável por adicionar as opções no menu
         */
        private void adicionarOpcoes(){
            addOption("Cadastrar Conta Bancária", () -> cadastrarContaBancaria());
            addOption("Manusear Contas Bancárias", () -> manusearContas());
            addLastOption();
        }
        
        /**
         * Método privado responsável por abrir um dialog de cadastro de conta
         * Só abre se houver um correntista
         */
        private void cadastrarContaBancaria(){
            if(cdao.readAll().isEmpty()){
                Messages.infoMessage("Não há correntistas cadastrados");
            }else{
                SwingUtilities.invokeLater(() -> {
                    JDialog dialog = new TelaCadastroConta(null, this.cdao, this.cbdao);
                    dialog.setVisible(true);
                });
            }
        }
        
        /**
         * Método privado responsável por abrir o menu de controle de contas
         * Só abre se houver uma conta cadastrada
         */
        private void manusearContas(){
            if(cbdao.readAll().isEmpty()){
                Messages.infoMessage("Não há contas bancárias cadastradas");
            }else{
                SwingUtilities.invokeLater(() -> {
                    MenuContas mc = new MenuContas(this.cbdao);
                    mc.exibir();
                });
            }
        }
    }

    /**
     * Menu responsável por mostrar opções referentes aos correntistas
     * @author mateus.brambilla
     */
    public class MenuCorrentistas extends MenuDialog {
        private final CorrentistaDAO cdao;
        private final ContaBancariaDAO cbdao;
        
        // Construtor
        public MenuCorrentistas(CorrentistaDAO cdao, ContaBancariaDAO cbdao){
            super(null, true);

            this.cdao = cdao;
            this.cbdao = cbdao;

            setTitulo("Menu: Correntista");
            setSubtitulo("Opções relacionadas à Correntista");
            adicionarOpcoes();
        }
        
        /**
         * Método privado responsável por adicionar as opções no menu
         */
        private void adicionarOpcoes(){
            addOption("Cadastrar Correntista", () -> cadastrarCorrentista());
            addOption("Editar Correntista", () -> editarCorrentista());
            addOption("Deletar Correntista", () -> deletarCorrentista());
            addOption("Visualizar Correntistas", () -> visualizarCorrentistas());
            addLastOption();
        }
        
        /**
         * Método privado responsável por abrir um dialog de cadastro de correntista
         */
        private void cadastrarCorrentista(){
            SwingUtilities.invokeLater(() -> {
                JDialog dialog = new TelaCadastroCorrentista(null, this.cdao);
                dialog.setVisible(true);
            });
        }
        
        /**
         * Método que verifica se há algum correntista e, se houver, abre a tela de edição
         */
        private void editarCorrentista(){
            ArrayList<Pessoa> cList = cdao.readAll();
            
            if(cList.isEmpty()){
                Messages.infoMessage("Não há correntistas cadastrados");
            }else{
                SwingUtilities.invokeLater(() -> {
                    TelaEditarUsuario teu = new TelaEditarUsuario(null, this.cdao);
                    teu.setVisible(true);
                });
            }
        }
        
        /**
         * Método que faz algumas verificações das pessoas e, se houver alguma sem conta,
         * abre uma tela para deletar
         */
        private void deletarCorrentista(){
            ArrayList<Pessoa> cList = cdao.readAll();
            ArrayList<ContaBancaria> cbList = cbdao.readAll();
            ArrayList<Pessoa> filteredList = new ArrayList<>();
            
            if(cList.isEmpty()){
                Messages.infoMessage("Não há correntistas cadastrados");
            }else{
                for(Pessoa p : cList){
                    boolean temConta = false;
                    
                    for(ContaBancaria cb : cbList){
                        if(cb.getPessoa().equals(p)){
                            temConta = true;
                            break;
                        }
                    }
                    
                    if(!temConta){
                        filteredList.add(p);
                    }
                }
                
                if(filteredList.isEmpty()){
                    Messages.infoMessage("Não há correntistas sem conta bancária cadastrada");
                }else{
                    SwingUtilities.invokeLater(() -> {
                        TelaDeletarUsuario tdu = new TelaDeletarUsuario(null, this.cdao, filteredList);
                        tdu.setVisible(true);
                    });
                }
            }
        }

        /**
         * Classe que itera por todos os registros de clientes e os lista
         */
        private void visualizarCorrentistas(){
            ArrayList<Pessoa> cList = cdao.readAll();

            if(cList.isEmpty()){
                Messages.infoMessage("Não há nenhum correntista cadastrado");
            }else{
                StringBuilder sb = new StringBuilder();

                sb.append("== Correntistas Cadastrados ==\n\n");

                
                for(Pessoa p : cList) {
                    sb.append(p);
                    
                    if(cList.indexOf(p) != cList.size() - 1) sb.append("\n");
                }

                Messages.infoMessage(sb.toString());
            }
        }
    }

    /**
     * Classe que representa o menu de contas para escolha do usuário
     * @author mateus.brambilla
     */
    public class MenuContas extends MenuDialog {
        private final ContaBancariaDAO cbdao;
        
        public MenuContas(ContaBancariaDAO cbdao){
            super(null, true);

            this.cbdao = cbdao;
            
            setTitulo("Menu: Central de Contas");
            setSubtitulo("Listagem de contas bancárias");
            adicionarContas();
        }
        
        /**
         * Carrega as contas do DAO e as adiciona como opções no menu.
         */
        private void adicionarContas(){
            ArrayList<ContaBancaria> cbList = cbdao.readAll();
            
            for (ContaBancaria cb : cbList) {
                addOption(cb.toString(), () -> rodaMenuBanco(cb));
            }
            addLastOption();
        }
        
        /**
         * Recebe uma conta bancária e abre o MenuBanco para ela.
         * @param conta - conta bancária selecionada pelo usuário.
         */
        private void rodaMenuBanco(ContaBancaria conta) {
            if (conta != null) {
                SwingUtilities.invokeLater(() -> {
                    MenuBanco m = new MenuBanco(conta, this.cbdao);
                    m.exibir();
                });
                cbdao.update(conta);
            }
        }
    }

    /**
     * Classe que herda de menu, representando a view do nosso banco
     * @author mateus.brambilla
     */
    public class MenuBanco extends MenuDialog {
        private final ContaBancaria cb;
        private final TransacaoDAO tdao = new TransacaoDAO();
        private final ContaBancariaDAO cbdao;
        
        /**
         * Construtor que recebe cpf, nome, endereco e saldo e instancia classe
         * @param cb - conta bancária já instanciada
         */
        public MenuBanco(ContaBancaria cb, ContaBancariaDAO cbdao){
            super(null, true);

            this.cb = cb;
            this.cbdao = cbdao;

            setTitulo("Menu: Operações Bancárias");
            setSubtitulo("Operações de movimentação e visualização de conta");
            adicionarOpcoes();
        }
        
        /**
         * Método auxilair que adiciona as opções ao menu
         */
        private void adicionarOpcoes(){
            addOption("Depositar valor", () -> depositarValor());
            addOption("Sacar valor", () -> sacarValor());
            addOption("Verificar status", () -> verificarStatus());
            addOption("Consultar logs", () -> consultarLogs());
            addLastOption();
        }
        
        /**
         * Método que realiza um depósito de modo totalmente autônomo
         */
        private void depositarValor(){
            try{
                double dQtde = Inputs.Double(
                        "Infome a quantidade a ser depositada",
                        "DEPÓSITO"
                );
                
                this.cb.depositaValor(dQtde);
                this.tdao.create(
                        new Transacao(
                                dQtde, 
                                Transacao.DEFAULT_DESC, 
                                Transacao.MOV_CREDITO, 
                                this.cb.getNumeroConta()
                        )
                );
                this.cbdao.update(this.cb); // atualiza conta no arquivo

                verificarStatus();
                        
            }catch(SaldoInvalidoException e){
                Messages.errorMessage(e);
            }catch(NullInputException e){
                Messages.infoMessage("Cancelando operação...");
            }
        }
        
        /**
         * Método que realiza um saque de modo totalmente autônomo
         */
        private void sacarValor(){
            try{
                double dQtde = Inputs.Double(
                        "Informe a quantidade a ser sacada",
                        "SAQUE"
                );
                        
                this.cb.sacarValor(dQtde);
                
                this.tdao.create(
                        new Transacao(
                                dQtde, 
                                Transacao.DEFAULT_DESC, 
                                Transacao.MOV_DEBITO, 
                                this.cb.getNumeroConta()
                        )
                );
                this.cbdao.update(this.cb);
                
                verificarStatus();
        
            }catch(SaldoInvalidoException e){
                Messages.errorMessage(e);
            }catch(NullInputException e){
                Messages.infoMessage("Cancelando operação...");
            }
        }
        
        /**
         * Método que informa o status da conta
         */
        private void verificarStatus(){
            Messages.infoMessage(this.cb.consultarStatus());
        }
        
        /**
         * Método que informa os logs de movimentação da conta
         * Se não houve nenhuma movimentação naquela conta, exibe mensagem informativa
         */
        private void consultarLogs(){
            StringBuilder sb = new StringBuilder();
            ArrayList<Transacao> tList = this.tdao.read(this.cb.getNumeroConta());
            
            if(tList.isEmpty()){
                Messages.infoMessage("Não há nenhum registro de movimentação nessa conta");
            }else{
            
                for(Transacao t : tList){
                    sb.append(t);

                    if(tList.indexOf(t) != tList.size() - 1){
                        sb.append("\n");
                    }
                }

                Messages.infoMessage(sb.toString());
            }
        }
    }
}
