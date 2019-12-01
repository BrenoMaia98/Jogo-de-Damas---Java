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
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

/**
 *
 * @author anach
 */
public class Cliente extends Thread{

    private Socket socket;
    private BufferedReader in;
    private PrintWriter out;
    private Tabuleiro t;
    private String nome;
     public static final int EMPTY = 0, RED = 1,WHITE = 3;
    public Cliente(String nome) {
       this.nome = nome;
    }


    public void conectarServidor() {
        try {
            socket = new Socket("localhost", 12000);
            out = new PrintWriter(socket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            t = new Tabuleiro(this);
            Thread thread = new Thread(t);
            thread.start();
        } catch (Exception e) {
            System.out.println("Erro: " + e.getMessage());
        }
    }

    public String mandarMensagem(String mensagem) {
        this.out.println(mensagem);
        try {
            String resposta = in.readLine();
            return resposta;
        } catch (IOException ex) {
            return "Erro";
        }

    }

    public void run() {
        int ganhou=0, resposta;
        boolean saiu = false;
        String oponente="";
        CaixaDialogo caixadialogo = new CaixaDialogo();
        JFrame frame = new JFrame("Mensagem");
        String mensagem="";
        try {
            out.println("Nome:"+nome);
            while(mensagem.equals(""))
             mensagem = in.readLine();
            while (!saiu) {
                String array[] = mensagem.split(":");
                switch (array[0]) {
                    case "Nome":
                        oponente = array[1];
                        t.setNomeP1(nome);
                        t.setNomeP2(oponente);
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

                        if(ganhou == WHITE){
                            resposta = JOptionPane.showConfirmDialog(frame, "Parabéns VOCÊ ganhou!\n Deseja desafiar "+oponente+" novamente?");
                            if(resposta == 1 || resposta ==2){
                                saiu = true;   
                                out.println("EncerrarPartida:"+ganhou);
                            }
                            else
                                out.println("JogarNovamente:"+ganhou);
                        }
                        break;
                    case "EncerrarPartida":
                        if(ganhou == WHITE)
                            caixadialogo.ShowMessage("Você ganhou e "+oponente+" não deseja jogar novamente");
                        if(ganhou == RED)
                            caixadialogo.ShowMessage(oponente+" ganhou e não deseja jogar novamente");
                        break;
                    case "NaoTenhoJogadas":
                        t.swapPlayer();
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
                    case "RestartGame":
                          t.initializeBoard();
                          t.repaint();
                          break;
                    default:
                        break;
                }
                if (!saiu) {
                    mensagem = in.readLine();
                }
            }
            encerrarConexao();
        } catch (IOException ex) {
            Logger.getLogger(Servidor.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }

    public void mandarMovimento(int colAtual, int linhaAtual, int colAnterior, int linhaAnterior) {
        int a,b,c,d;
        a = 7 - colAtual;
        b = 7 - linhaAtual;
        c = 7 - colAnterior;
        d = 7 - linhaAnterior;
        String movimento = "Movimento:" + a + ":" + b + ":" + c + ":" + d;
        // movimento = "Sair:" + RED;
        if(t.puloObrigatorio) movimento +=":++"; // caso tenha um próximo pulo deve avisar o adversário
        out.println(movimento);
    }

    public void mandarMsg(String text){
        out.println(text);
    }
    
    public void encerrarConexao() throws IOException {
        in.close();
        out.close();
        socket.close();
    }

}
