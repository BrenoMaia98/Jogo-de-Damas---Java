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
import java.net.Socket;

/**
 *
 * @author anach
 */
public class Cliente {
    private Socket socket;
    private BufferedReader in;
    private PrintWriter out;
    
    public Cliente() {
    }
    
    public void conectarServidor() {
        try {
          socket = new Socket("localhost",12000);
          out = new PrintWriter(socket.getOutputStream(), true);
          in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        }
        catch(Exception e) {
          System.out.println("Erro: " + e.getMessage());
        }
    }
    
    public String mandarMensagem(String mensagem) {
        out.println(mensagem);
        try {
            String resposta = in.readLine();
            return resposta;
        } catch (IOException ex) {
            return "Erro";
        }
        
    }
    
    public void encerrarConexao() throws IOException{
        in.close();
        out.close();
        socket.close();
    }
    
}
