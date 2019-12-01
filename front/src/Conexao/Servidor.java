/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Conexao;

import Interface.CaixaDialogo;
import Interface.Tabuleiro;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

/**
 *
 * @author anach
 */
public final class Servidor extends Thread {

    private ServerSocket servidor;

    private Socket cliente;
    private BufferedReader in;
    private PrintWriter out;
    private Tabuleiro t;
     public static final int EMPTY = 0, RED = 1,WHITE = 3;
    private String nome;
    public Servidor(String nome) throws IOException {
        this.nome = nome;
    }

    @Override
    public void run() {
        try {
            servidor = new ServerSocket(12000);
            conectar();
        } catch (IOException ex) {
            Logger.getLogger(Servidor.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InterruptedException ex) {
            Logger.getLogger(Servidor.class.getName()).log(Level.SEVERE, null, ex);
        }
        System.out.println("Aguardando outro jogador...");

    }

    public void conectar() throws IOException, InterruptedException {
        cliente = servidor.accept();
        out = new PrintWriter(cliente.getOutputStream(), true);
        in = new BufferedReader(new InputStreamReader(cliente.getInputStream()));
        t = new Tabuleiro(this);
        Thread thread = new Thread(t);
        thread.start();
        this.executar();
    }


    public void setarDadosAdversario(String nome) {
        System.out.println("Partida iniciada!");
    }

    public void executar() {
        int ganhou=0, resposta;
        boolean saiu = false;
        String oponente="";
        CaixaDialogo caixadialogo = new CaixaDialogo();
        JFrame frame = new JFrame("Mensagem");
        String mensagem;
        try {
            mensagem = in.readLine();
            while (!saiu) {
                String array[] = mensagem.split(":");
                switch (array[0]) {
                    case "Nome":
                        oponente = array[1];
                        mandarMsg("Nome:"+nome);
                        break;
                        
                    case "Movimento":
                        int linhaAnterior, colAnterior , linhaAtual, colAtual;
                        colAtual = Integer.parseInt(array[1]);
                        linhaAtual = Integer.parseInt(array[2]);
                        colAnterior = Integer.parseInt(array[3]);
                        linhaAnterior = Integer.parseInt(array[4]);
                        t.atualizarPosicao(colAtual, linhaAtual,colAnterior,linhaAnterior);
                        if(array.length != 6){
                            t.swapPlayer(); // verique se o outro tem pulo encadeado
                        }
                        break;
                    case "Sair":
                        ganhou = Integer.parseInt(array[1]);
                        if(ganhou == RED){
                            resposta = JOptionPane.showConfirmDialog(frame, "Parabéns VOCÊ ganhou!\n Deseja desafiar "+oponente+" novamente?");
                            if(resposta == 1 || resposta ==2){
                                saiu = true;   
                                out.println("EncerrarPartida:"+ganhou);
                            }
                            else
                                out.println("JogarNovamente:"+ganhou);
                        }
                        break;
                    case "JogarNovamente":
                        caixadialogo.start();
                        resposta = caixadialogo.ConfirmDialog(oponente+" deseja te desafiar novamente, você aceita?");
                            if(resposta == 1 || resposta ==2){
                                saiu = true;   
                                out.println("EncerrarPartida:"+ganhou);
                            }
                            else{
                                t.initializeBoard();
                                t.repaint();
                                out.println("RestartGame");
                            }
                        break;
                    case "EncerrarPartida":
                        if(ganhou == RED)
                            caixadialogo.ShowMessage("Você ganhou e "+oponente+" não deseja jogar novamente");
                        if(ganhou == WHITE)
                            caixadialogo.ShowMessage(oponente+" ganhou e não deseja jogar novamente");
                        break;
                    case "RestartGame":
                          t.initializeBoard();
                          t.repaint();
                          break;
                    case "NaoTenhoJogadas":
                        if(t.currentPlayer != t.getCorPlayer1())
                            t.swapPlayer();
                        break;
                    default:
                        break;
                }
                if (!saiu) {
                    mensagem = in.readLine();
                }
            }
            fecharConexao();
        } catch (IOException ex) {
            Logger.getLogger(Servidor.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public void mandarMovimento(int colAtual, int linhaAtual, int colAnterior, int linhaAnterior){
        int a,b,c,d;
        a = 7 - colAtual;
        b = 7 - linhaAtual;
        c = 7 - colAnterior;
        d = 7 - linhaAnterior;
        String movimento = "Movimento:" + a + ":" + b + ":" + c + ":" + d;
        if(t.puloObrigatorio) 
            movimento +=":++"; // caso tenha um próximo pulo deve avisar o adversário
        out.println(movimento);
    }
    
    public void mandarMsg(String text){
        out.println(text);
    }
    
    public void fecharConexao() throws IOException {
        in.close();
        out.close();
        cliente.close();
        servidor.close();
    }
}
