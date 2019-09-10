/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Conexao;

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
public final class Servidor extends Thread{
    private ServerSocket servidor;
    private Jogador jogador1;
    private Jogador jogador2;
    private Socket cliente;
    private BufferedReader in;
    private PrintWriter out;

    public Servidor() throws IOException {
        String nome1 = JOptionPane.showInputDialog("Qual será seu nome no jogo?");
        jogador1 = new Jogador(nome1,'b');
        iniciar();
    }
    
    public void iniciar() throws IOException{
        servidor = new ServerSocket(12000);
        System.out.println("Aguardando outro jogador...");
        conectar();
    }
    
    public void conectar() throws IOException{
       cliente = servidor.accept();
       out = new PrintWriter(cliente.getOutputStream(), true);
       in = new BufferedReader(new InputStreamReader(cliente.getInputStream()));
       this.start();
    }
    
    public void setarDadosAdversario(String nome){
        jogador2 = new Jogador(nome,'p');
        System.out.println("Partida iniciada!");
    }
    
    @Override
    public void run(){
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
