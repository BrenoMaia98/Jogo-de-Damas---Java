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
public final class Servidor extends Thread {

    private ServerSocket servidor;
    private Jogador jogador1;
    private Jogador jogador2;
    private Socket cliente;
    private BufferedReader in;
    private PrintWriter out;
    private Tabuleiro t;
    private boolean bloqueado;
    public Servidor(String nome) throws IOException {
        jogador1 = new Jogador(nome, 'b');
        bloqueado = false;
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

    public boolean isBloqueado() {
        return bloqueado;
    }

    public void setBloqueado(boolean bloqueado) {
        this.bloqueado = bloqueado;
    }

    public void setarDadosAdversario(String nome) {
        jogador2 = new Jogador(nome, 'p');
        System.out.println("Partida iniciada!");
    }

    public void executar() {
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
                        System.out.println("Jogador " + jogador2.getNome() + " saiu da partida!");
                        out.println("Obrigado por jogar!");
                        saiu = true;
                        fecharConexao();
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

    public void mandarMovimento(int colAtual, int linhaAtual, int colAnterior, int linhaAnterior){
        int a,b,c,d;
        a = 7 - colAtual;
        b = 7 - linhaAtual;
        c = 7 - colAnterior;
        d = 7 - linhaAnterior;
        String movimento = "Movimento:" + a + ":" + b + ":" + c + ":" + d;
        out.println(movimento);
    }
    
    public void fecharConexao() throws IOException {
        in.close();
        out.close();
        cliente.close();
        servidor.close();
    }
}
