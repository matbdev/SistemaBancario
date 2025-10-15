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
    public ArrayList<Pessoa> readAll(){
        ArrayList<Pessoa> pList = new ArrayList<>();
        String linha;
        
        if(a.abrirLeitura()){
            while ((linha = a.lerLinha()) != null) {
                String[] p = linha.split(";");
                try {
                    pList.add(new Pessoa(p[0], p[1], p[2]));
                } catch (CpfInvalidoException ex) {
                    Messages.errorMessage("Correntista inválido de CPF " + p[0] + " encontrada.");
                }
            }
        }
        a.fecharArquivo();
        
        Collections.sort(pList);
        return pList;
    }
   
    /**
     * Método que retorna um objeto específico com base no cpf
     * @param cpf - um objeto da classe CPF
     * @return a pessoa encontrada ou nulo
     */  
    @Override
    public Pessoa read(CPF cpf){
        ArrayList<Pessoa> pList = readAll();
        
        Pessoa pEncontrada = null;
        for(Pessoa p : pList){
            if(p.getCPF().equals(cpf)){
                pEncontrada = p;
            }
        }
        return pEncontrada;
    }
    
    /**
     * Método que retorna uma pessoa se já houver uma no banco de dados
     * com o mesmo CPF
     * @param pessoa - um objeto da classe Pessoa
     * @return a pessoa encontrada ou nulo
     */  
    public Pessoa read(Pessoa pessoa){
        ArrayList<Pessoa> pList = readAll();
        
        Pessoa pEncontrada = null;
        for(Pessoa p : pList){
            if(p.equals(pessoa)){
                pEncontrada = p;
            }
        }
        return pEncontrada;
    }
  
    /**
     * Método responsável por adicionar um objeto no arraylist
     * @param p - pessoa a ser adicionada
     * @throws PessoaJaExisteException - caso a pessoa já existir
     */
    @Override
    public void create(Pessoa p) throws PessoaJaExisteException{
        if(read(p) == null) addOnArchive(p);
        else throw new PessoaJaExisteException("A pessoa com CPF " + p.getCpfFormatado() + " já existe.");
    }
    
    /**
     * Método auxiliar que realiza o salvamento no arquivo
     * Não existe append, só overwrite
     * @param p - pessoa a ser adicionada
     */
    private void addOnArchive(Pessoa p){
        ArrayList<Pessoa> pList = readAll();
        pList.add(p);
        
        if(a.abrirEscrita()){
            for (Pessoa pes : pList) {
                a.escreverLinha(pes.getLineForSave());
            }
        }
        a.fecharArquivo();
    }
    
    /**
     * Método auxiliar que realiza o salvamento no arquivo
     * Não existe append, só overwrite
     * Recebe um arraylist atualizado (em métodos update e delete)
     * @param pList - arraylist a ser escrito
     */
    private void addOnArchive(ArrayList<Pessoa> pList){
        if(a.abrirEscrita()){
            for (Pessoa pes : pList) {
                a.escreverLinha(pes.getLineForSave());
            }
        }
        a.fecharArquivo();
    }
    
    /**
     * Método que recebe uma pessoa, nome e endereço e atualiza o cadastro do usuário
     * @param p - objeto de pessoa, que vem deste próprio dao
     * @param nome - nome (novo ou corrente) da pessoa
     * @param endereco - endereço (novo ou corrente) da pessoa
     */
    public void update(Pessoa p, String nome, String endereco){
        ArrayList<Pessoa> pList = readAll();
        
        int pIndex = pList.indexOf(p);
        Pessoa pessoaLista = pList.get(pIndex);
        
        pessoaLista.setEndereco(endereco);
        pessoaLista.setNome(nome);
        
        addOnArchive(pList);
    }
    
    /**
     * Método que recebe um objeto e o delta
     * @param p - objeto de pessoa
     */
    public void delete(Pessoa p){
        ArrayList<Pessoa> pList = readAll();
        pList.remove(p);
        addOnArchive(pList);
    }
}
