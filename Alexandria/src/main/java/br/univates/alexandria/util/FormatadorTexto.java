package br.univates.alexandria.util;

/**
 * Método que fornece conversores de texto
 * @author mateus.brambilla
 */
public class FormatadorTexto {
    
    /**
     * Método que recebe uma string e formata para que a primeira letra
     * de cada palavra seja maíuscula (title case)
     * @param text - string recebida
     * @return  - string com primeiro caractere em maiúsculo
     */
    public static String converteTitleCase(String text){
        // Primeira letra de cada palavra em maiúsculo
        StringBuilder sb = new StringBuilder();
        Verificador.verificaVazio(text, "Título inválido fornecido");

        String trimmedText = text.trim();
            
        if(trimmedText.length() == 1){
            sb.append(trimmedText.toUpperCase());
        }else{
            String[] palavras = trimmedText.split(" ");
                
            for(String palavra : palavras){
                sb.append(palavra.substring(0, 1).toUpperCase());
                sb.append(palavra.substring(1).toLowerCase());
                sb.append(" ");
            }
        }
        return sb.toString().trim();
    }
    
    /**
     * Método que recebe um text e retorna ele sem caracteres, só números
     * @param text - texto com caracteres completos
     * @return - dígitos numéricos
     */
    public static String limparNumeros(String text) {
        return text.replaceAll("[^0-9]", "");
    }
}
