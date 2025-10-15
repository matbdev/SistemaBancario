package br.univates.alexandria.util;

import br.univates.alexandria.exceptions.ElementoVazioException;
import java.util.ArrayList;

/**
 * Fornece métodos verificadores auxiliares
 * @author mateus.brambilla
 */
public class Verificador {
    /**
     * Método auxiliar que verifica se o input está vazio
     * @param text - texto informado
     * @param message - mensagem em caso de erro
     * @throws ElementoVazioException - em caso de elemento vazio
     */
    public static void verificaVazio(String text, String message) throws ElementoVazioException{
        if(text.isBlank()){
            throw new ElementoVazioException(message);
        }
    }
    
    /**
     * Método auxiliar que verifica se o input está vazio
     * @param text - texto informado
     * @param message - mensagem em caso de erro
     * @throws ElementoVazioException - em caso de elemento vazio
     */
    public static void verificaVazio(char text, String message) throws ElementoVazioException{
        if(Character.isWhitespace(text)){
            throw new ElementoVazioException(message);
        }
    }
    
    /**
     * Método auxiliar que verifica se o input está vazio
     * @param array - lista informada
     * @param message - mensagem em caso de erro
     * @throws ElementoVazioException - em caso de elemento vazio
     */
    public static void verificaVazio(ArrayList<?> array, String message) throws ElementoVazioException{
        if(array.isEmpty()){
            throw new ElementoVazioException(message);
        }
    }
}
