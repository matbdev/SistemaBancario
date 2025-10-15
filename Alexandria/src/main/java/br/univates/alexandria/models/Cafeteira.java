package br.univates.alexandria.models;

// - Cafeteira, ou seja, uma máquina que faz café: Imagine uma cafeteira que tem um espaço para colocar
// café em grãos e outro espaço para colocar água. Na frente da cafeteira tem dois botões, uma que faz 
// café curto e outro café longo. Toda vez que um café é feito cerca de 25g de café são consumidos. 
// No café curto cerca de 60ml de água são consumidos e no café longo cerca de 130ml de água são consumidos. 
// Obviamente se não há insumos (café ou água) não é possível fazer café. Agora crie um modelo computacional 
// em software para essa cafeteira.

public class Cafeteira {
    // Constantes para o consumo
    private static final double CONSUMO_CAFE_G = 25.0;
    private static final double CONSUMO_AGUA_CURTO_ML = 60.0;
    private static final double CONSUMO_AGUA_LONGO_ML = 130.0;
    private static final String ML_STR = "ml";
    private static final String G_STR = "g";

    // Atributos
    private final Reservatorio reservatorioCafeG;
    private final Reservatorio reservatorioAguaMl;
    
    /**
     * Constructor overload
     * @param quantidadeAguaMl - quantidade atual de água (ml)
     * @param quantidadeCafeG - quantidade atual de café (g)
     * @param capacidadeAguaMl - capacidade total de água (ml)
     * @param capacidadeCafeG - capacidade total de café (g)
     */
    public Cafeteira(double quantidadeAguaMl, double quantidadeCafeG, double capacidadeAguaMl, double capacidadeCafeG){        
        this.reservatorioAguaMl = new Reservatorio(capacidadeAguaMl, quantidadeAguaMl, ML_STR);
        this.reservatorioCafeG = new Reservatorio(capacidadeCafeG, quantidadeCafeG, G_STR);
    }

     public Cafeteira(double capacidadeAguaMl, double capacidadeCafeG) {
        this.reservatorioAguaMl = new Reservatorio(capacidadeAguaMl, ML_STR);
        this.reservatorioCafeG = new Reservatorio(capacidadeCafeG, G_STR);
    }

    /**
     * Getters e setters
     */
    public double getQuantidadeAguaMl(){
        return this.reservatorioAguaMl.getQuantidade();
    }

    public double getQuantidadeCafeG(){
        return this.reservatorioCafeG.getQuantidade();
    }

    public double getCapacidadeAguaMl(){
        return this.reservatorioAguaMl.getCapacidade();
    }

    public double getCapacidadeCafeG(){
        return this.reservatorioCafeG.getCapacidade();
    }

    public double getAguaPercentual(){
        return (this.reservatorioAguaMl.getQuantidade() / this.reservatorioAguaMl.getCapacidade()) * 100.0;
    }

    public double getCafePercentual(){
        return (this.reservatorioCafeG.getQuantidade() / this.reservatorioCafeG.getCapacidade()) * 100.0;
    }

    public double getEspacoLivreAgua(){
        return this.reservatorioAguaMl.getCapacidade() - this.reservatorioAguaMl.getQuantidade();
    }

    public double getEspacoLivreCafe(){
        return this.reservatorioCafeG.getCapacidade() - this.reservatorioCafeG.getQuantidade();
    }

    /**
     * Métodos que adicionam água/café
     * @param q - quantidade a ser adicionada
     */
    public void adicionarAgua(double q) {
        this.reservatorioAguaMl.addQtd(q);
    }

    public void adicionarCafe(double q) {
        this.reservatorioCafeG.addQtd(q);
    }

    /**
     * Métodos que enchem totalmente os reservatórios
     */
    public void encherReservatorioCafe(){
        this.reservatorioCafeG.encher();
    }

    public void encherReservatorioAgua(){
        this.reservatorioAguaMl.encher();
    }

    /**
     * Métodos que esvaziam totalmente os reservatórios
     */
    public void esvaziarReservatorioCafe(){
        this.reservatorioCafeG.esvaziar();
    }

    public void esvaziarReservatorioAgua(){
        this.reservatorioAguaMl.esvaziar();
    }

    /**
     * Métodos responsáveis por fazer os cafés
     */
    public String fazerCafeCurto() {
        verificarInsumos(CONSUMO_AGUA_CURTO_ML);
        consumirInsumos(CONSUMO_AGUA_CURTO_ML);
        return "Aproveite seu café curto!";
    }

    public String fazerCafeLongo() {
        verificarInsumos(CONSUMO_AGUA_LONGO_ML);
        consumirInsumos(CONSUMO_AGUA_LONGO_ML);
        return "Aproveite seu café longo!";
    }

    /**
     * Verifica se existem recursos para fazer o café
     * @param consumoAgua - agua a ser retirada (cafe é fixo)
     */
    private void verificarInsumos(double consumoAgua) {
        if (this.reservatorioCafeG.getQuantidade() < CONSUMO_CAFE_G) 
            throw new IllegalStateException("Não há café suficiente. Por favor, reabasteça.");
        
        if (this.reservatorioAguaMl.getQuantidade() < consumoAgua) 
            throw new IllegalStateException("Não há água suficiente para este tipo de café. Por favor, reabasteça.");
    }

    /**
     * Consome os insumos de acordo com o cafe
     * @param consumoAgua - agua a ser retirada (cafe é fixo)
     */
    private void consumirInsumos(double consumoAgua) {
        this.reservatorioCafeG.removeQtd(CONSUMO_CAFE_G);
        this.reservatorioAguaMl.removeQtd(consumoAgua);
    }

    /**
     * Método que exibe um relatório sobre a situação atual da cafeteira
     */
    public String gerarRelatorio(){
        return String.format(
            """
            RELATÓRIO DA CAFETEIRA:
            ÁGUA: 
            %s

            CAFÉ:
            %s
            """,
            reservatorioAguaMl.gerarRelatorio(),
            reservatorioCafeG.gerarRelatorio()
        );
    }
}
