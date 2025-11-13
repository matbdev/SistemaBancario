package br.univates.alexandria.components.menu;

import java.util.ArrayList;

import javax.swing.JOptionPane;

import br.univates.alexandria.models.Opcao;
import br.univates.alexandria.util.FormatadorTexto;
import br.univates.alexandria.util.Messages;
import br.univates.alexandria.util.Verificador;

/**
 * Classe que gera um modelo padrão para criação de menus
 * @author mateus.brambilla
 */
public abstract class Menu{   
    private String titulo;
    private String subtitulo;
    private final ArrayList<Opcao> opcoes = new ArrayList<>();

    /**
     * Construtor que não recebe nada mas seta um título padrão
     */
    public Menu(){
        this.titulo = "Menu";
    }
    
    /**
     * Construtor que recebe subtitulo recebe nada mas seta um título padrão
     * @param subtitulo - texto adicional
     */
    public Menu(String subtitulo){
        this.titulo = "Menu";
        this.subtitulo = "Subtitulo";
    }
    
    // Getters
    public String getTitulo() {
        return this.titulo;
    }
    
    public String getSubtitulo() {
        return this.subtitulo;
    }


    /**
     * Define ou atualiza o título do menu.
     * @param titulo - titulo a ser setado
     */
    public void setTitulo(String titulo) {
        Verificador.verificaVazio(titulo, "O título não pode estar vazio.");
        this.titulo = FormatadorTexto.converteTitleCase(titulo);
    }
    
    /**
     * Define ou atualiza o subtítulo do menu.
     * @param subtitulo - subtítulo a ser setado
     */
    public void setSubtitulo(String subtitulo) {
        Verificador.verificaVazio(subtitulo, "O subtítulo não pode estar vazio.");
        this.subtitulo = subtitulo;
    }

    /**
     * Adiciona uma opção completa ao menu, associando um ícone a um texto.
     * @param text O texto descritivo da opção.
     * @param icon O caractere que servirá como ícone/atalho.
     * @param acao A ação executada pela opção
     */
    public void addOption(String text, char icon, Runnable acao) {
        opcoes.add(new Opcao(icon, text, acao));
    }
    
    /**
     * Método que envia a opção de saída, que o menu já realiza
     * Adicina por último
     */
    public void addLastOption(){
        addOption(
                "Voltar à página anterior", 
                'x', 
                () -> {}
        );
    }
    
    /**
     * Monta e retorna a string formatada do menu para exibição.
     * O formato de cada opção é "[ícone] texto da opção".
     * @return um StringBuilder contendo o menu completo e formatado.
     */
    public String getMenu(){
        StringBuilder sb = new StringBuilder();
        sb.append(this.getSubtitulo()).append("\n\n");

        // Itera sobre todas as opções do modelo para formatá-las.
        for(int i = 0; i < getTamanho(); i++){
            sb.append("[").append(getIcone(i)).append("]"); // Adiciona item entre colchetes
            sb.append(" ").append(getDescricao(i)).append("\n"); // Adiciona o texto e uma quebra de linha.
        }

        return sb.toString();
    }
    
    /**
     * Retorna a lista completa de textos das opções do menu.
     * @return - arraylist das opções
     */
    public ArrayList<Opcao> getOptions(){
        return this.opcoes;
    }
    
    /**
     * Método que recebe um índice e retorna o objeto naquela posição
     * Verifica se está vazio
     * @param i - index desejado
     * @return - objeto na posição
     */
    public Opcao getOption(int i){
        return this.opcoes.get(i);
    }

    /**
     * Retorna o texto de uma opção específica pelo seu índice.
     * @param i O índice da opção desejada.
     * @return O texto da opção.
     */
    public String getDescricao(int i){
        return getOption(i).getDescricao();
    }

    /**
     * Retorna o ícone de uma opção específica pelo seu índice.
     * @param i O índice do ícone desejado.
     * @return O caractere do ícone.
     */
    public char getIcone(int i){
        return getOption(i).getIcone();
    }
    
    /**
     * Executa a ação de uma opção específica pelo seu índice.
     * Percorre todos os itens do arraylist para encontrar o char correspondente
     * @param c O índice da ação desejada.
     */
    public void executarAcao(char c){
        boolean ok = false;
        for(Opcao op : opcoes){
            if(op.getIcone() == c){
                op.executarAcao();
                ok = true;
                break;
            }
        }
        if(!ok)
            throw new IllegalArgumentException("Informe uma opção válida.");
    }
    
    /**
     * Método que retorna o tamanho total do array
     * @return - tamanho total do array
     */
    public int getTamanho(){
        Verificador.verificaVazio(this.opcoes, "Nenhuma opção existe");
        return this.opcoes.size();
    }
    
    /**
     * Função que seta o mostra o título e envia a opção escolhida
     */
    public void gerarMenu(){
        String op;
        char opChar;
        
        while (true){
            op = JOptionPane.showInputDialog(
                    null,
                    getMenu(),
                    getTitulo(),
                    JOptionPane.QUESTION_MESSAGE
            );
            
            if (op == null) {
                break;
            }
           
            if(op.isBlank()){
                Messages.errorMessage("Informe uma opção");
                continue;
            }
            
            opChar = Character.toUpperCase(op.charAt(0));

            if(opChar != 'X'){
                try{
                   executarAcao(opChar); 
                }catch(IllegalArgumentException e){
                    Messages.errorMessage(e);
                }
            }else break;
        }
    }
}
