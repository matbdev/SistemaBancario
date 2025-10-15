package br.univates.alexandria.models;

public class Reservatorio {
    private final double capacidade;
    private double quantidade = 0;
    private final String unidade;
    
    /**
     * Constructor overload
     * @param capacidade - capacidade total do reservatório
     * @param quantidade - quantidade atual dentro do reservatório
     */
    public Reservatorio(double capacidade, double quantidade, String unidade){
        if(capacidade <= 0)
            throw new IllegalArgumentException("A capacidade deve ser um valor positivo");
        this.capacidade = capacidade;

        if(quantidade < 0 || quantidade > capacidade)
            throw new IllegalArgumentException("A quantidade inicial é inválida. Deve estar entre 0 e " + capacidade);
        this.quantidade = quantidade;

        if(unidade.isBlank())
            throw new IllegalArgumentException("Unidade informada é inválida");
        this.unidade = unidade;
    }

    public Reservatorio(double capacidade, String unidade) {
        if (capacidade <= 0) 
            throw new IllegalArgumentException("A capacidade deve ser um valor positivo.");
        this.capacidade = capacidade;

        if(unidade.isBlank())
            throw new IllegalArgumentException("Unidade informada é inválida");
        this.unidade = unidade;
    }

    /**
     * Getters e setters
     */
    public double getCapacidade(){
        return this.capacidade;
    }

    public double getQuantidade(){
        return this.quantidade;
    }

    public String getUnidade(){
        return this.unidade;
    }

    public double getEspacoLivre() {
        return this.capacidade - this.quantidade;
    }

    public double getNivelPercentual() {
        return (this.capacidade == 0) ? 0 : (this.quantidade / this.capacidade) * 100;
    }

    /**
     * Método que adiciona quantidade, somente se for possível
     */
    public void addQtd(double quantidade) throws IllegalArgumentException{
        if (quantidade <= 0) 
            throw new IllegalArgumentException("A quantidade a ser adicionada deve ser positiva.");
        
        if (this.quantidade + quantidade > this.capacidade) 
            throw new IllegalArgumentException("Operação inválida: A capacidade máxima do reservatório será excedida.");
        
        this.quantidade += quantidade;
    }

    /**
     * Método que remove quantidade, somente se for possível
     */
    public void removeQtd(double quantidade) throws IllegalArgumentException{
        if (quantidade <= 0) 
            throw new IllegalArgumentException("A quantidade a ser removida deve ser positiva.");
        
        if (this.quantidade - quantidade < 0) 
            throw new IllegalArgumentException("Operação inválida: Não há quantidade suficiente para o consumo desejado.");
        
        this.quantidade -= quantidade;
    }

    /**
     * Método que esvazia o reservatório
     */
    public void esvaziar(){
        this.quantidade = 0;
    }

    /**
     * Método que enche o reservatório
     */
    public void encher(){
        this.quantidade = capacidade;
    }

    /**
     * Método que exibe um relatório sobre a situação atual do reservatório
     */
    public String gerarRelatorio(){
        return String.format(
            """
            Capacidade total: %.2f %s
            Nível atual: %.2f %s
            Espaço livre: %.2f %s
            Percentual de ocupação: %.2f%%
            """,
            getCapacidade(), getUnidade(),
            getQuantidade(), getUnidade(),
            getEspacoLivre(), getUnidade(),
            getNivelPercentual()
        );
    }
}