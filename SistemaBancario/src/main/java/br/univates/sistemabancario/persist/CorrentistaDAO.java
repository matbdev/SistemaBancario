package br.univates.sistemabancario.persist;

import br.univates.alexandria.dao.BaseDAO;
import br.univates.alexandria.exceptions.CpfInvalidoException;
import br.univates.alexandria.exceptions.PessoaJaExisteException;
import br.univates.alexandria.models.CPF;
import br.univates.alexandria.models.Pessoa;
import br.univates.alexandria.tools.Arquivo;
import br.univates.alexandria.tools.Messages;
import java.util.ArrayList;
import java.util.Collections;

/**
 * Classe que lida com a persistência para cadastro de correntista
 * @author mateus.brambilla
 */
public class CorrentistaDAO implements BaseDAO<Pessoa, CPF> {
    private static final Arquivo a = new Arquivo("correntista.dat");
    
    /**
     * Método que retorna todos os valores
     * @return arraylist dos valores
     */
    @Override
    public ArrayList<Pessoa> readAll() {
        ArrayList<Pessoa> pList = new ArrayList<>();
        if (a.abrirLeitura()) {
            try {
                String linha;
                while ((linha = a.lerLinha()) != null) {
                    try {
                        String[] p = linha.split(";");
                        if (p.length < 3) {
                            Messages.errorMessage("Registro de correntista mal formatado no arquivo. Linha ignorada.");
                            continue;
                        }

                        pList.add(new Pessoa(p[0], p[1], p[2]));
                    } catch (CpfInvalidoException ex) {
                        Messages.errorMessage(ex);
                    }
                }
            } finally {
                a.fecharArquivo(); // Garante o fechamento do arquivo
            }
        }
        Collections.sort(pList);
        return pList;
    }
   
    /**
     * Método que retorna um objeto específico com base no cpf
     * @param cpf - um objeto da classe CPF
     * @return a pessoa encontrada ou nulo
     */  
    @Override
    public Pessoa read(CPF cpf) {
        if (a.abrirLeitura()) {
            try {
                String linha;
                while ((linha = a.lerLinha()) != null) {
                    try {
                        String[] p = linha.split(";");
                        if (p.length >= 1 && p[0].equals(cpf.getCpf())) {
                            return new Pessoa(p[0], p[1], p[2]); // Encontrou, cria e retorna.
                        }
                    } catch (CpfInvalidoException ex) { /* Ignora linha inválida */ }
                }
            } finally {
                a.fecharArquivo();
            }
        }
        return null; // Não encontrou
    }

    /*
     * Método que retorna uma pessoa se já houver uma no banco de dados
     * com o mesmo CPF
     * @param pessoa - um objeto da classe Pessoa
     * @return a pessoa encontrada ou nulo
     */  
    public Pessoa read(Pessoa pessoa) {
        return read(pessoa.getCPF()); // Reutiliza o método acima
    }
  
    /**
     * Método responsável por adicionar um objeto no arraylist
     * @param p - pessoa a ser adicionada
     * @throws PessoaJaExisteException - caso a pessoa já existir
     */
    @Override
    public void create(Pessoa p) throws PessoaJaExisteException {
        ArrayList<Pessoa> pList = readAll();
        if (pList.contains(p)) {
            throw new PessoaJaExisteException("A pessoa com CPF " + p.getCpfFormatado() + " já existe.");
        }
        pList.add(p);
        saveAll(pList);
    }
    
    /**
     * Método que recebe uma pessoa, nome e endereço e atualiza o cadastro do usuário
     * @param p - objeto de pessoa, que vem deste próprio dao
     * @param nome - nome (novo ou corrente) da pessoa
     * @param endereco - endereço (novo ou corrente) da pessoa
     */
    public void update(Pessoa p, String nome, String endereco) {
        ArrayList<Pessoa> pList = readAll(); // Lê a lista UMA VEZ
        int pIndex = pList.indexOf(p);
        if (pIndex != -1) {
            Pessoa pessoaLista = pList.get(pIndex);
            pessoaLista.setEndereco(endereco);
            pessoaLista.setNome(nome);
            saveAll(pList); // Salva a lista inteira
        }
    }
    
    /**
     * Método que recebe um objeto e o delta
     * @param p - objeto de pessoa
     */
    public void delete(Pessoa p){
        ArrayList<Pessoa> pList = readAll();
        pList.remove(p);
        saveAll(pList);
    }

    /**
     * Método privado que salva a lista inteira no arquivo, sobrescrevendo o conteúdo.
     */
    private void saveAll(ArrayList<Pessoa> pList) {
        if (a.abrirEscrita()) {
            try {
                for (Pessoa pes : pList) {
                    a.escreverLinha(pes.getLineForSave());
                }
            } finally {
                a.fecharArquivo(); // Garante o fechamento do arquivo
            }
        }
    }
}
