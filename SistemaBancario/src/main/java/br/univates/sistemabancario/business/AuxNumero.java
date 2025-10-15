package br.univates.sistemabancario.business;

import br.univates.alexandria.exceptions.NumeroContaInvalidoException;
import br.univates.sistemabancario.persist.ContaBancariaDAO;
import java.util.Random;

/**
 * Classe responsável por gerar um número de conta único.
 * @author mateus.brambilla
 */
public class AuxNumero {
    private static final ContaBancariaDAO dao = new ContaBancariaDAO();
    private static final Random random = new Random();

    /**
     * Gera um número de conta único que ainda não existe no DAO.
     * @return um número inteiro único.
     * @throws NumeroContaInvalidoException - caso o número da conta for inválido
     */
    public static int gerarUnico() throws NumeroContaInvalidoException {
        while (true) {
            int numero = random.nextInt(1, 1000000);
            if (dao.read(numero) == null)
                return numero;
        }
    }
    
    /**
     * Verifica se o número já existe na base de dados
     * @param n - inteiro representando o número
     * @throws NumeroContaInvalidoException - caso o número já exista
     */
    public static void verificaExiste(int n) throws NumeroContaInvalidoException{
        if (dao.read(n) != null)
            throw new NumeroContaInvalidoException("O número já existe na base de dados");
    }
}