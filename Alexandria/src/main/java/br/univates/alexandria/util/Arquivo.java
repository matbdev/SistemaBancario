package br.univates.alexandria.util;

import java.io.*;
/************
 * Arquivo - versão 25/05/2007
 * 
 * abrirEscrita() -- boolean
 * abrirLeitura() -- boolean
 * lerLinha() -- string
 * escreverLinha( string )
 * fecharArquivo()
 */
public class Arquivo
{
    private BufferedReader bufferedReader;
    private BufferedWriter bufferedWriter;
    private String nomeArquivo;
    private char status;
    
    public Arquivo( String nomeArquivo )
    {
        this.nomeArquivo = nomeArquivo;
        status = 'C'; // Closed, Read, Write
    }
    
    /**
     * Abre o arquivo para leitura. O arquivo precisa estar aberto para leitura
     * a fim de que o método lerLinha() possa ser utilizado.
     * @return Retorna verdadeiro quando abre o arquivo com sucesso
     */
    public boolean abrirLeitura()
    {
        boolean sucesso = true;
        FileReader fileReader = null;
        if (status != 'C' )
        {
            System.err.println( "-- Arquivo \"" + nomeArquivo + "\" já está aberto" );
            sucesso = false;
        }
        else
        {
            try
            {
                fileReader = new FileReader(nomeArquivo);
            }
            catch (FileNotFoundException e)
            {
                System.err.println( "-- Arquivo \"" + nomeArquivo + "\" não encontrado" );
                sucesso = false;
                //e.printStackTrace();
            }
            if (sucesso)
            {
                status = 'R';
                bufferedReader = new BufferedReader(fileReader);
            }
        }
        return (sucesso);
    }
    
    /**
     * Abre o arquivo para escrever linhas. Para escrever no arquivo 
     * usando o método escreverLinha() é necessário que o mesmo esteja 
     * aberto para escrita. 
     * @return Verdadeiro quando houve sucesso na abertura do arquivo.
     */
    public boolean abrirEscrita()
    {
        boolean sucesso = true;
        FileWriter fileWriter = null;
        if (status != 'C' )
        {
            System.err.println( "-- Arquivo \"" + nomeArquivo + "\" já está aberto" );
            sucesso = false;
        }
        else
        {
            try
            {
                fileWriter = new FileWriter(nomeArquivo);
            }
            catch (IOException e)
            {
                System.err.println( "-- Erro de escrita no arquivo \"" + nomeArquivo +"\"" );
                sucesso = false;
                //e.printStackTrace();
            }
            if (sucesso)
            {
                status = 'W';
                bufferedWriter = new BufferedWriter(fileWriter);
            }
        }
        return (sucesso);
    }
    
    /**
     * Lê uma linha do arquivo. Para usar este método o arquivo deve estar aberto
     * em modo leitura, usando o método abrirLeitura()
     * @return Retorna a linha lida.
     */
    public String lerLinha()
    {
        String linha = null;
        if (status == 'R')
        {
            try
            {
                linha = bufferedReader.readLine();
            }
            catch (IOException e)
            {
                System.err.println( "-- Erro de leitura no arquivo \"" + nomeArquivo + "\"" );
                //e.printStackTrace();
            }
        }
        else
        {
            System.err.println( "-- Arquivo \"" + nomeArquivo + "\" não está pronto para leitura" );
        }
        return (linha);
    }
    
    /**
     * Escreve uma linha no arquivo. O arquivo precisa estar aberto para escrita
     * usando o método abrirEscrita().
     * @param linha A string que vai ser gravada no arquivo.
     */
    public void escreverLinha( String linha )
    {
        if (status == 'W')
        {
            try
            {
                bufferedWriter.write(linha + "\n");
            }
            catch (IOException e)
            {
                System.err.println( "Erro de escrita no arquivo \"" + nomeArquivo + "\"" );
                //e.printStackTrace();
            }
        }
        else
        {
            System.err.println( "-- Arquivo \"" + nomeArquivo + "\" não está pronto para escrita" );
        }
    }
    
    public void fecharArquivo()
    {
        if (status == 'R') // estava aberto para leitura
        {
            try
            {
                bufferedReader.close();
                status = 'C';
            }
            catch(IOException e)
            {
                System.err.println( "Erro ao fechar o arquivo \"" + nomeArquivo + "\"" );
                //e.printStackTrace();
            }
        }
        if (status == 'W') // estava aberto para gravação
        {
            try
            {
                bufferedWriter.close();
                status = 'C';
            }
            catch(IOException e)
            {
                System.err.println( "Erro ao fechar o arquivo \"" + nomeArquivo + "\"" );
                //e.printStackTrace();
            }
        }
    }
}