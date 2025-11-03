package br.univates.sistemabancario.repository;

import br.univates.alexandria.interfaces.IDao;
import br.univates.alexandria.models.CPF;
import br.univates.alexandria.models.Pessoa;
import br.univates.sistemabancario.repository.interfaces.IDaoTransacao;
import br.univates.sistemabancario.service.ContaBancaria;
import br.univates.sistemabancario.service.Transacao;

/**
 * Fábrica para criar instâncias de DAOs.
 * Esta fábrica utiliza Injeção de Dependência, recebendo a conexão
 * que será compartilhada pelos DAOs.
 */
public class DAOFactory {

    /**
     * Cria e retorna uma instância de CorrentistaDAO.
     * 
     * @return - instância de CorrentistaDAO
     */
    public static IDao<Pessoa, CPF> getCorrentistaDAO() {
        return new CorrentistaDAO();
    }

    /**
     * Cria e retorna uma instância de ContaBancariaDAO.
     * 
     * @return - instância de ContaBancariaDAO
     */
    public static IDao<ContaBancaria, Integer> getContaBancariaDAO() {
        return new ContaBancariaDAO();
    }

    /**
     * Cria e retorna uma instância de TransacaoDAO.
     * 
     * @return - instância de TransacaoDAO
     */
    public static IDaoTransacao<Transacao, Integer> getTransacaoDAO() {
        return new TransacaoDAO();
    }
}
