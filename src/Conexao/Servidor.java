/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Conexao;

import Interface.Tabuleiro;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

/**
 *
 * @author anach
 */
public final class Servidor{
    private ServerSocket servidor;
    private Jogador jogador1;
    private Jogador jogador2;
    private Socket cliente;
    private BufferedReader in;
    private PrintWriter out;

    public Servidor(String nome) throws IOException {
        jogador1 = new Jogador(nome,'b');
        iniciar();
    }
    
    public void iniciar() throws IOException{
        servidor = new ServerSocket(12000);
        System.out.println("Aguardando outro jogador...");
        
    }
    
    public void conectar() throws IOException{
       cliente = servidor.accept();
       out = new PrintWriter(cliente.getOutputStream(), true);
       in = new BufferedReader(new InputStreamReader(cliente.getInputStream()));
       new Tabuleiro();
       this.executar();
    }
    
    public void setarDadosAdversario(String nome){
        jogador2 = new Jogador(nome,'p');
        System.out.println("Partida iniciada!");
    }
    
    
    public void executar(){
        boolean saiu = false;
        String mensagem;
        try {
            mensagem = in.readLine();
            while (!saiu) {
             String array[] = mensagem.split(":");
             switch(array[0]){
                 case "Nome":
                     setarDadosAdversario(array[1]);
                     out.println("Partida iniciada!");
                     break;
                 case "Sair":
                     System.out.println("Jogador " + jogador2.getNome() + " saiu da partida!");
                     out.println("Obrigado por jogar!");
                     saiu = true;
                     fecharConexao();
                     break;
                 default:
                     break;
             }
             if(!saiu)mensagem = in.readLine();
            }
        } catch (IOException ex) {
            Logger.getLogger(Servidor.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    
    public void fecharConexao() throws IOException{
        in.close();
        out.close();
        cliente.close();
        servidor.close();
    }
}
