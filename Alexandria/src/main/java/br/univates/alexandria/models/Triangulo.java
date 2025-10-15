package br.univates.alexandria.models;

/**
 * Classe que simula um triângulo
 * @author mateus.brambilla
 */
public class Triangulo {
    private double a;
    private double b;
    private double c;
    
    public Triangulo(double a, double b, double c){
        setTriangulo(a, b, c);
    }
    
    public double getArea(){
        double sp = (this.a + this.b + this.c) / 2;
        double area = Math.sqrt(sp * (sp - this.a) * (sp - this.b) * (sp - this.c));
        return area;
    }
    
    public double getPerimetro(){
        return this.a + this.b + this.c;
    }

    public double getLadoA(){
        return this.a;
    }

    public double getLadoB(){
        return this.b;
    }

    public double getLadoC(){
        return this.c;
    }

    /**
     * Método que seta o lado a após verificar-lo
     * @param a - lado a
     */
    public void setLadoA(double a){
        verificarTriangulo(a, this.b, this.c);
        this.a = a;
    }

    /**
     * Método que seta o lado b após verificar-lo
     * @param b - lado b
     */
    public void setLadoB(double b){
        verificarTriangulo(this.a, b, this.c);
        this.b = b;
    }

    /**
     * Método que seta o lado c após verificar-lo
     * @param c - lado c
     */
    public void setLadoC(double c){
        verificarTriangulo(this.a, this.b, c);
        this.c = c;
    }
    
    /**
     * Seta um triângulo e o verifica
     * @param a - lado a
     * @param b - lado b
     * @param c - lado c
     */
    public void setTriangulo(double a, double b, double c){
        verificarTriangulo(a, b, c);
        this.a = a;
        this.b = b;
        this.c = c;
    }
    
    /**
     * Método que aumenta proporcionalmente os lados de um triângulo
     * @param percentual - porcentagem a ser aumentada
     * @throws IllegalArgumentException - caso a porcentagem for <= 0
     */
    public void aumentarLados(double percentual) throws IllegalArgumentException{
        if (percentual <= 0)
            throw new IllegalArgumentException("Informe uma porcentaem válida.");
        
        double fator = 1 + (percentual/100);
        a *= fator;
        b *= fator;
        c *= fator;
    }
    
    /**
     * Método que verifica se os lados do triângulo são válidos
     * @param a - lado a
     * @param b - lado b
     * @param c - lado c
     * @throws IllegalArgumentException - em caso de triângulo inválido
     */
    public void verificarTriangulo(double a, double b, double c) throws IllegalArgumentException{
        double maior = Math.max(a, Math.max(b, c));
        double soma = a + b + c - maior;
        
        if(soma < maior)
            throw new IllegalArgumentException("Os lados informados não formam um triângulo.");
    }
}
