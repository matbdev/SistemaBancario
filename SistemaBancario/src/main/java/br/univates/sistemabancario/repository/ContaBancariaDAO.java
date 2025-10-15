package br.univates.sistemabancario.repository;
        
import br.univates.alexandria.models.CPF;
import br.univates.alexandria.models.Pessoa;
import br.univates.alexandria.repository.BaseDAO;
import br.univates.alexandria.util.Arquivo;
import br.univates.alexandria.util.Messages;
import br.univates.sistemabancario.exceptions.ContaJaExisteException;
import br.univates.sistemabancario.exceptions.NumeroContaInvalidoException;
import br.univates.sistemabancario.exceptions.SaldoInvalidoException;
import br.univates.sistemabancario.service.ContaBancaria;
import br.univates.sistemabancario.service.ContaBancariaEspecial;
import br.univates.sistemabancario.service.Numero;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

/**
 * Classe com a lógica de gravação (inexistente por enquanto)
 * @author mateus.brambilla
 */
public class ContaBancariaDAO implements BaseDAO<ContaBancaria, Numero>{
    private static final Arquivo a = new Arquivo("conta_bancaria.dat");
    private final CorrentistaDAO cdao;
    private static final Random random = new Random();

    /**
     * Construtor que recebe um cdao via injeção de dependência
     * @param cdao - objeto de CorrentistaDAO já instanciado
     */
    public ContaBancariaDAO(CorrentistaDAO cdao) {
        this.cdao = cdao;
    }

    /**
     * Gera um número de conta único que ainda não existe no DAO.
     * @return um número inteiro único.
     * @throws NumeroContaInvalidoException - caso o número da conta for inválido
     */
    public int gerarNumeroUnico() throws NumeroContaInvalidoException {
        while (true) {
            int numero = random.nextInt(999999) + 1; // Gera de 1 a 999999
            if (this.read(numero) == null) {
                return numero;
            }
        }
    }

    /**
     * Verifica se o número já existe na base de dados
     * @param n - inteiro representando o número
     * @throws NumeroContaInvalidoException - caso o número já exista
     */
    private void verificaNumeroExiste(int n) throws NumeroContaInvalidoException {
        if (this.read(n) != null) {
            throw new NumeroContaInvalidoException("O número de conta " + n + " já existe na base de dados");
        }
    }
    
    /**
     * Método que retorna todos os registros
     * @return lista com objetos ContaBancaria
     */
    @Override
    public ArrayList<ContaBancaria> readAll() {
        ArrayList<Pessoa> todosCorrentistas = cdao.readAll();
        ArrayList<ContaBancaria> cbList = new ArrayList<>();
        
        if (a.abrirLeitura()) {
            try { // garante que o finally será executado
                String linha;
                while ((linha = a.lerLinha()) != null) {
                    try {
                        String[] cbLine = linha.split(";");
                        
                        if (cbLine.length < 4) { // proteção contra linha mal formatada
                            Messages.errorMessage("Registro de conta mal formatado no arquivo. Linha ignorada.");
                            continue;
                        }

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
                            continue;
                        }
                        
                        int numero = Integer.parseInt(cbLine[1]);
                        double saldo = Double.parseDouble(cbLine[2]);
                        double limite = Double.parseDouble(cbLine[3]);
                        String tipoConta = cbLine[4];
                        
                        ContaBancaria cb;
                        if (tipoConta.equals("ContaBancariaEspecial")) {
                            cb = new ContaBancariaEspecial(pEncontrada, limite, saldo, numero);
                        } else {
                            cb = new ContaBancaria(pEncontrada, saldo, numero);
                        }
                        
                        cbList.add(cb);
                        
                    } catch (NumberFormatException e) {
                        Messages.errorMessage("Erro de formatação de número em uma linha do arquivo de contas. Linha ignorada.");
                    } catch (SaldoInvalidoException | NumeroContaInvalidoException e) {
                        Messages.errorMessage(e.getMessage());
                    }
                }
            } finally {
                a.fecharArquivo();
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
    public void create(ContaBancaria cb) throws ContaJaExisteException, NumeroContaInvalidoException {
        // Se a conta foi criada com um número específico, verifica se ele já existe
        if (cb.getNumeroConta().isDefinidoPeloUsuario()) {
            verificaNumeroExiste(cb.getNumeroContaInt());
        } else {
            int numeroUnico = gerarNumeroUnico();
            cb.getNumeroConta().setNumero(numeroUnico); // Atribui o número gerado ao objeto da conta
        }

        ArrayList<ContaBancaria> cbList = readAll();
        if (cbList.contains(cb)) {
            throw new ContaJaExisteException("A conta de número " + cb.getNumeroContaFormatado() + " já existe.");
        }
        
        cbList.add(cb);
        saveAll(cbList);
    }
    
    /**
     * Método privado que salva a lista inteira de contas no arquivo,
     * sobrescrevendo o conteúdo anterior.
     * @param cbList A lista completa a ser salva.
     */
    private void saveAll(ArrayList<ContaBancaria> cbList) {
        if (a.abrirEscrita()) {
            try {
                for (ContaBancaria conta : cbList) {
                    a.escreverLinha(conta.getLineForSave());
                }
            } finally {
                a.fecharArquivo();
            }
        }
    }
    
    /**
     * Método que retorna uma ContaBancaria com base no número da conta
     * @param numero - objeto Numero instanciado, da conta
     * @return a conta com o número igual
     */
    @Override
    public ContaBancaria read(Numero numero) {
        if (a.abrirLeitura()) {
            try {
                String linha;
                while ((linha = a.lerLinha()) != null) {
                    try {
                        String[] cbLine = linha.split(";");
                        if (cbLine.length >= 2 && Integer.parseInt(cbLine[1]) == numero.getNumeroInt()) {
                            Pessoa pEncontrada = cdao.read(new CPF(cbLine[0]));
                            if (pEncontrada != null) {
                                double saldo = Double.parseDouble(cbLine[2]);
                                double limite = Double.parseDouble(cbLine[3]);
                                ContaBancaria cb = new ContaBancaria(pEncontrada, saldo, numero.getNumeroInt());
                                cb.setLimite(limite);
                                return cb; 
                            }
                        }
                    } catch (Exception e) { 
                        /* Ignora linha mal formatada ou inválida */
                    }
                }
            } finally {
                a.fecharArquivo();
            }
        }
        return null; // Não encontrou
    }
    
    /**
     * Método que retorna uma ContaBancaria com base no número da conta
     * @param numero - int que representa número da conta
     * @return a conta com o número igual
     * @throws NumeroContaInvalidoException - caso o número for inválido
     */
    public ContaBancaria read(int numero) throws NumeroContaInvalidoException {
        Numero numeroObj = new Numero(numero);
        return read(numeroObj);
    }
    
    /**
     * Método que retorna uma ContaBancaria com base no número da conta (de outra contabancária)
     * @param cb2 - outra conta bancária
     * @return a conta com o número igual
     */
    public ContaBancaria read(ContaBancaria cb2){
        return read(cb2.getNumeroConta());
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
            saveAll(cbList);
        } else {
            Messages.errorMessage("Erro: A conta " + cb.getNumeroContaFormatado() + " não foi encontrada para atualização.");
        }
    }
}
