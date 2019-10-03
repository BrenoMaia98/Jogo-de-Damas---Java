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
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author anach
 */
public class Cliente extends Thread{

    private Socket socket;
    private BufferedReader in;
    private PrintWriter out;
    private Tabuleiro t;
    private boolean bloqueado;
    public Cliente() {
        bloqueado = true;
    }

    public boolean isBloqueado() {
        return bloqueado;
    }

    public void setBloqueado(boolean bloqueado) {
        this.bloqueado = bloqueado;
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
        boolean saiu = false;
        String mensagem;
        try {
            mensagem = in.readLine();
            while (!saiu) {
                String array[] = mensagem.split(":");
                switch (array[0]) {
                    case "Movimento":
                        int linhaAnterior, colAnterior , linhaAtual, colAtual;
                        colAtual = Integer.parseInt(array[1]);
                        linhaAtual = Integer.parseInt(array[2]);
                        colAnterior = Integer.parseInt(array[3]);
                        linhaAnterior = Integer.parseInt(array[4]);
                        t.atualizarPosicao(colAtual, linhaAtual,colAnterior,linhaAnterior);
                        t.swapPlayer();
                        bloqueado = false;
                        break;
                    case "Sair":
                        out.println("Obrigado por jogar!");
                        saiu = true;
                        break;
                    default:
                        break;
                }
                if (!saiu) {
                    mensagem = in.readLine();
                }
            }
        } catch (IOException ex) {
            Logger.getLogger(Servidor.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }

    public void mandarMovimento(int colAtual, int linhaAtual, int colAnterior, int linhaAnterior) {
        String movimento = "Movimento:" + (7-colAtual) + ":" + (7-linhaAtual) + ":" + (7-colAnterior) + ":" + (7-linhaAnterior);
        out.println(movimento);
    }

    public void encerrarConexao() throws IOException {
        in.close();
        out.close();
        socket.close();
    }

}
