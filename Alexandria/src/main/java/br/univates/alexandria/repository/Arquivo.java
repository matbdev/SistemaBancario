package br.univates.alexandria.repository;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

/**
 * Código fornecido pela Univates, refatorado por
 * Simplifica criação de arquivo e sua alteração
 * @author mateus.brambilla
 */
public class Arquivo {
    private static final char CLOSED_STATUS = 'C';
    private static final char READ_STATUS = 'R';
    private static final char WRITE_STATUS = 'W';

    /**
     * Exception para quando o caminho informado é vazio
     * @author mateus.brambilla
     */
    public class ArquivoVazioException extends IllegalArgumentException{
        public ArquivoVazioException(String text){
            super(text);
        }
    }

    /**
     * Exception para quando a lista informada para escrita em lote está vazia
     * @author mateus.brambilla
     */
    public class ListaVaziaException extends IllegalArgumentException{
        public ListaVaziaException(String text){
            super(text);
        }
    }

    private BufferedReader bufferedReader;
    private BufferedWriter bufferedWriter;
    private String nomeArquivo;
    private char status;
    
    /**
     * Construtor que recebe um caminho para o arquivo de salvamento
     * Lida com a criação dos diretórios pais e arquivo
     * @param nomeArquivo
     */
    public Arquivo(String nomeArquivo) {
        if(nomeArquivo.isBlank()){
            throw new ArquivoVazioException("Passe um arquivo para realizar operações!");
        }

        this.nomeArquivo = nomeArquivo;
        status = CLOSED_STATUS;
        File meuArquivo = new File(nomeArquivo);

        try {
            // Pega caminho até arquivo
            File parentDir = meuArquivo.getParentFile();

            // Verifica se há um caminho para o arquivo e se o diretório já existe
            if (parentDir != null && !parentDir.exists()) {
                // Retorna false caso não conseguir criar
                boolean tryMkdirs = parentDir.mkdirs();

                if (!tryMkdirs) {
                    System.err.println("Falha ao criar diretório(s)");
                    return; 
                }
            }

            // Retorna false caso o arquivo já exista, e true se não (criando o arquivo, inclusive)
            boolean sucesso = meuArquivo.createNewFile();
            if(sucesso){
                System.out.println("Arquivo criado com sucesso!");
            }
        } catch (IOException e) {
            System.err.println("Erro ao criar o arquivo: " + nomeArquivo + " | " + e.getMessage());
        } catch (SecurityException se) {
            System.err.println("Erro de segurança. Verifique as permissões de escrita para o caminho: " + nomeArquivo + " | " + se.getMessage());
        }
    }
    
    /**
     * Abre o arquivo para leitura. O arquivo precisa estar aberto para leitura
     * a fim de que o método lerLinha() possa ser utilizado.
     * @return Retorna verdadeiro quando abre o arquivo com sucesso
     */
    public boolean abrirLeitura() {
        boolean sucesso = true;
        FileReader fileReader = null;

        switch (status) {
            case READ_STATUS -> {
                System.err.println( "-- Arquivo \"" + nomeArquivo + "\" já está aberto para leitura" );
                sucesso = false;
            }
            case WRITE_STATUS -> {
                System.err.println( "-- Arquivo \"" + nomeArquivo + "\" já está aberto para escrita" );
                sucesso = false;
            }
            default -> {
                try{
                    fileReader = new FileReader(nomeArquivo);
                }
                catch (FileNotFoundException e){
                    System.err.println( "-- Arquivo \"" + nomeArquivo + "\" não encontrado" );
                    sucesso = false;
                }

                if (sucesso){
                    status = READ_STATUS;
                    bufferedReader = new BufferedReader(fileReader);
                }
            }
        }
        return sucesso;
    }
    
    /**
     * Abre o arquivo para escrever linhas. Para escrever no arquivo 
     * usando o método escreverLinha() é necessário que o mesmo esteja 
     * aberto para escrita. 
     * @return Verdadeiro quando houve sucesso na abertura do arquivo.
     */
    public boolean abrirEscrita() {
        boolean sucesso = true;
        FileWriter fileWriter = null;

        switch (status) {
            case READ_STATUS -> {
                System.err.println( "-- Arquivo \"" + nomeArquivo + "\" já está aberto para leitura" );
                sucesso = false;
            }
            case WRITE_STATUS -> {
                System.err.println( "-- Arquivo \"" + nomeArquivo + "\" já está aberto para escrita" );
                sucesso = false;
            }
            default -> {
                try{
                    fileWriter = new FileWriter(nomeArquivo);
                }catch (IOException e){
                    System.err.println( "-- Erro de escrita no arquivo \"" + nomeArquivo +"\"" );
                    sucesso = false;
                }

                if (sucesso){
                    status = WRITE_STATUS;
                    bufferedWriter = new BufferedWriter(fileWriter);
                }
            }
        }
        return sucesso;
    }
    
    /**
     * Lê uma linha do arquivo. Para usar este método o arquivo deve estar aberto
     * em modo leitura, usando o método abrirLeitura()
     * @return Retorna a linha lida.
     */
    public String lerLinha() {
        String linha = null;
        if (status == READ_STATUS){
            try{
                linha = bufferedReader.readLine();
            }catch (IOException e){
                System.err.println( "-- Erro de leitura no arquivo \"" + nomeArquivo + "\"" );
            }
        }else{
            System.err.println( "-- Arquivo \"" + nomeArquivo + "\" não está pronto para leitura" );
        }

        return linha;
    }
    
    /**
     * Escreve uma linha no arquivo. O arquivo precisa estar aberto para escrita
     * usando o método abrirEscrita().
     * @param linha A string que vai ser gravada no arquivo.
     */
    public void escreverLinha(String linha){
        if (status == WRITE_STATUS){
            try{
                bufferedWriter.write(linha + "\n");
            }catch (IOException e){
                System.err.println( "Erro de escrita no arquivo \"" + nomeArquivo + "\"" );
            }
        }else{
            System.err.println( "-- Arquivo \"" + nomeArquivo + "\" não está pronto para escrita" );
        }
    }
    
    /**
     * Fecha o arquivo, seja de leitura ou escrita.
     */
    public void fecharArquivo(){
        if (status == READ_STATUS){
            try{
                bufferedReader.close();
                status = CLOSED_STATUS;
            }catch(IOException e){
                System.err.println( "Erro ao fechar o arquivo \"" + nomeArquivo + "\"" );
            }
        }else if (status == WRITE_STATUS){
            try{
                bufferedWriter.close();
                status = CLOSED_STATUS;
            }catch(IOException e){
                System.err.println( "Erro ao fechar o arquivo \"" + nomeArquivo + "\"" );
            }
        }
    }
}