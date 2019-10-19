/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Interface;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

/**
 *
 * @author Breno
 */
public class CaixaDialogo extends Thread{
    private JFrame frame;
    private String msg;
    private int resposta;
    
     public CaixaDialogo() {
    }
     
    @Override
    public void run(){
        frame = new JFrame("Mensagem");
    }
    
    public int ConfirmDialog(String msg){
        return JOptionPane.showConfirmDialog(frame, msg);
    }
    
     public void ShowMessage(String msg){
         JOptionPane.showMessageDialog(frame,msg);
    }

}
