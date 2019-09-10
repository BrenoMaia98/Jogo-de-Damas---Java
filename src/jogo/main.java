/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jogo;

import Conexao.Cliente;
import Conexao.Servidor;
import java.io.IOException;
import javax.swing.JOptionPane;

/**
 *
 * @author Breno
 */
public class main {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws IOException {
        // TODO code application logic here
        String opcao = JOptionPane.showInputDialog("Iniciar partida ou entrar em uma existente?"), nome, resposta;
        if(opcao.equals("iniciar")){
            Servidor servidor = new Servidor();
        }
        else{
            nome = JOptionPane.showInputDialog("Qual será seu nome no jogo?");
            Cliente jogador = new Cliente();
            jogador.conectarServidor();
            resposta = jogador.mandarMensagem("Nome:"+nome);
            System.out.println(resposta);
            jogador.mandarMensagem("Sair");
        }
        
    }
    
}
