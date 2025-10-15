package br.univates.sistemabancario.persist;
        
import br.univates.alexandria.dao.BaseDAO;
import br.univates.alexandria.exceptions.ContaJaExisteException;
import br.univates.alexandria.exceptions.NumeroContaInvalidoException;
import br.univates.alexandria.exceptions.SaldoInvalidoException;
import br.univates.alexandria.models.Pessoa;
import br.univates.alexandria.tools.Arquivo;
import br.univates.alexandria.tools.Messages;
import br.univates.sistemabancario.business.ContaBancaria;
import br.univates.sistemabancario.business.Numero;
import java.util.ArrayList;
import java.util.Collections;
import java.util.InputMismatchException;

/**
 * Classe com a lógica de gravação (inexistente por enquanto)
 * @author mateus.brambilla
 */
public class ContaBancariaDAO implements BaseDAO<ContaBancaria, Numero>{
    private static final Arquivo a = new Arquivo("conta_bancaria.dat");
    private final CorrentistaDAO cdao = new CorrentistaDAO();
    
    /**
     * Método que retorna todos os registros
     * @return lista com objetos ContaBancaria
     */
    @Override
public ArrayList<ContaBancaria> readAll(){
    ArrayList<Pessoa> todosCorrentistas = cdao.readAll();
    
    ArrayList<ContaBancaria> cbList = new ArrayList<>();
    String linha;
    
    if(a.abrirLeitura()){
        while ((linha = a.lerLinha()) != null) {
            String[] cbLine = linha.split(";");
            try {
                String cpfArquivo = cbLine[0];
                
                Pessoa pEncontrada = null;
                for (Pessoa p : todosCorrentistas) {
                    if (p.getCpfNumbers().equals(cpfArquivo)) { 
                        pEncontrada = p;
                        break;
                    }
                }

                if (pEncontrada == null) {
                    Messages.errorMessage("Conta bancária órfã encontrada. CPF: " + cpfArquivo);
                    continue; // Pula para a próxima linha do arquivo
                }
                
                int numero = Integer.parseInt(cbLine[1]);
                double saldo = Double.parseDouble(cbLine[2]);
                double limite = Double.parseDouble(cbLine[3]);
                
                ContaBancaria cb = new ContaBancaria(pEncontrada, saldo, numero);
                cb.setLimite(limite);
                cbList.add(cb);
                
            } catch(InputMismatchException e){
                Messages.errorMessage("Erro de formatação de um dos campos do arquivo");
            } catch (SaldoInvalidoException | NumeroContaInvalidoException e) {
                Messages.errorMessage(e);
            } finally {
                a.fecharArquivo();
            }
        }
    }
    Collections.sort(cbList);
    return cbList;
}
    
    /**
     * Método responsável por adicionar um objeto na lista
     * @param cb - ContaBancaria a ser adicionada
     * @throws ContaJaExisteException - caso a conta já exista
     */
    @Override
    public void create(ContaBancaria cb) throws ContaJaExisteException{
        if(read(cb) == null) addOnArchive(cb);
        else throw new ContaJaExisteException("A conta de número " + cb.getNumeroContaFormatado()+ " já existe.");
    }
    
    /**
     * Método auxiliar que realiza o salvamento no arquivo
     * Não existe append, só overwrite
     * @param cb - conta bancária a ser adicionada
     */
    private void addOnArchive(ContaBancaria cb){
        ArrayList<ContaBancaria> cbList = readAll();
        cbList.add(cb);
        
        if(a.abrirEscrita()){
            for (ContaBancaria conta : cbList) {
                a.escreverLinha(conta.getLineForSave());
            }
        }
        a.fecharArquivo();
    }
    
    /**
     * Método auxiliar que realiza o salvamento no arquivo
     * Não existe append, só overwrite
     * Recebe um arraylist atualizado (em métodos update e delete)
     * @param cb - conta bancária a ser adicionada
     */
    private void addOnArchive(ArrayList<ContaBancaria> cbList){
        if(a.abrirEscrita()){
            for (ContaBancaria cb : cbList) {
                a.escreverLinha(cb.getLineForSave());
            }
        }
        a.fecharArquivo();
    }
    
    /**
     * Método que retorna uma ContaBancaria com base no número da conta
     * @param numero - objeto Numero instanciado, da conta
     * @return a conta com o número igual
     */
    @Override
    public ContaBancaria read(Numero numero){
        ArrayList<ContaBancaria> cbList = readAll();
        ContaBancaria contaEncontrada = null;
        
        for (ContaBancaria cb : cbList) {
            if (cb.getNumeroConta().equals(numero)) {
                contaEncontrada = cb;
            }
        }
        return contaEncontrada;
    }
    
    /**
     * Método que retorna uma ContaBancaria com base no número da conta
     * @param numero - int que representa número da conta
     * @return a conta com o número igual
     * @throws NumeroContaInvalidoException - caso o número for inválido
     */
    public ContaBancaria read(int numero) throws NumeroContaInvalidoException{
        ArrayList<ContaBancaria> cbList = readAll();
        ContaBancaria contaEncontrada = null;
        
        Numero numeroObj = new Numero(numero, false);
        for (ContaBancaria cb : cbList) {
            if (cb.getNumeroConta().equals(numeroObj)) {
                contaEncontrada = cb;
            }
        }
        return contaEncontrada;
    }
    
    /**
     * Método que retorna uma ContaBancaria com base no número da conta (de outra contabancária)
     * @param cb2 - outra conta bancária
     * @return a conta com o número igual
     */
    public ContaBancaria read(ContaBancaria cb2){
        ArrayList<ContaBancaria> cbList = readAll();
        ContaBancaria contaEncontrada = null;
        
        for (ContaBancaria cb : cbList) {
            if (cb.equals(cb2)) {
                contaEncontrada = cb2;
            }
        }
        return contaEncontrada;
    }
    
    /**
     * Método que recebe uma conta e saldo e atualiza o cadastro da conta
     * @param cb - objeto de ContaBancaria, que vem deste próprio dao
     */
    public void update(ContaBancaria cb){
        ArrayList<ContaBancaria> cbList = readAll();
        
        int pIndex = cbList.indexOf(cb);

        if (pIndex != -1) {
            cbList.set(pIndex, cb); 
            addOnArchive(cbList);
        } else {
            Messages.errorMessage("Erro: A conta " + cb.getNumeroContaFormatado() + " não foi encontrada para atualização.");
        }
    }
}
