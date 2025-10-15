package br.univates.alexandria.models;

import br.univates.alexandria.exceptions.CpfInvalidoException;
import br.univates.alexandria.util.FormatadorTexto;
import br.univates.alexandria.util.Verificador;

/**
 * Classe que abstrai o cpf de uma pessoa
 * @author mateus.brambilla
 */
public class CPF {
    private final String cpf;
    
    /**
     * Consturutor que recebe e trata cpf
     * @param cpf - cpf do usuário
     * @throws CpfInvalidoException - em caso de cpf inválido
     */
    public CPF(String cpf) throws CpfInvalidoException{
        Verificador.verificaVazio(cpf, "CPF vazio informado");
        String cpfLimpo = FormatadorTexto.limparNumeros(cpf);
        verificaCpf(cpfLimpo);

        this.cpf = cpfLimpo;
    }
        

    // Getters
    public String getCpf() {
        return this.cpf;
    }
    
    public String getCpfFormatado() {
        String primeiraCadeia = this.cpf.substring(0, 3);
        String segundaCadeia = this.cpf.substring(3, 6);
        String terceiraCadeia = this.cpf.substring(6, 9);
        String quartaCadeia = this.cpf.substring(9, 11);
        
        String cpfFormatado = (
                primeiraCadeia + "." +
                segundaCadeia + "." +
                terceiraCadeia + "-" +
                quartaCadeia
        );
        return cpfFormatado;
    }
    
    /**
     * Verifica se o cpf digitado é válido
     * @param cpf - cpf informado no construtor 
     * @throws CpfInvalidoException - em caso de cpf inválido
     */
    public void verificaCpf(String cpf) throws CpfInvalidoException{
        if(cpf.length() != 11)
            throw new CpfInvalidoException("CPF sem números o suficiente");

        // CPFs inválidos
        if(
            cpf.equals("11111111111")
            || cpf.equals("22222222222")
            || cpf.equals("33333333333")
            || cpf.equals("44444444444")
            || cpf.equals("55555555555")
            || cpf.equals("66666666666")
            || cpf.equals("77777777777")
            || cpf.equals("88888888888")
            || cpf.equals("99999999999")
        )
            throw new CpfInvalidoException("CPF inválido - todos os dígitos são iguais");

        String primeirosNove = cpf.substring(0, 9);
        String primeirosDez = cpf.substring(0, 10);
        int soma = 0;
        int multiplicador = 10;
        
        for (int i = 0; i < primeirosNove.length(); i++) {
            soma += Character.getNumericValue(primeirosNove.charAt(i)) * multiplicador;
            multiplicador--;
        }

        int resto = 11 - (soma % 11);
        int digitoDez = (resto >= 10) ? 0 : resto;

        if(!(digitoDez == Character.getNumericValue(cpf.charAt(9))))
            throw new CpfInvalidoException("CPF inválido - Décimo dígito inválido");

        soma = 0;
        multiplicador = 11;

        for (int i = 0; i < primeirosDez.length(); i++) {
            soma += Character.getNumericValue(primeirosDez.charAt(i)) * multiplicador;
            multiplicador--;
        }

        resto = 11 - (soma % 11);
        int digitoOnze = (resto >= 10) ? 0 : resto;
        
        if(!(digitoOnze == Character.getNumericValue(cpf.charAt(10))))
            throw new CpfInvalidoException("CPF inválido - Décimo primeiro dígito inválido");
    }
}
