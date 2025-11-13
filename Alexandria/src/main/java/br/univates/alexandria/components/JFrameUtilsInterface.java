package br.univates.alexandria.components;

/**
 * Interface com método para definir ação do botão
 * @author mateus.brambilla
 */
public interface JFrameUtilsInterface {
    /**
     * Método responsável por adicionar uma ação ao botão da página
     * @param listener - action listener
     */
    public void adicionarAcaoBotao(java.awt.event.ActionListener listener);

    /**
     * Método responsável por exibir uma mensagem de erro
     * @param mensagem - corpo da mensagem
     */
    public void exibirErro(String mensagem);
    
    /**
     * Método responsável por exibir uma mensagem de sucesso
     * @param mensagem - corpo da mensagem
     */
    public void exibirSucesso(String mensagem);

}
