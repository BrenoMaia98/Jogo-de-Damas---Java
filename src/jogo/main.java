/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jogo;

import Conexao.Cliente;
import Conexao.Servidor;
import Interface.TelaInicial;
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
        new TelaInicial().setVisible(true);
    }
    
}
