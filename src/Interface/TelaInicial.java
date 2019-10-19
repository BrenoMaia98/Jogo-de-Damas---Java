/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Interface;

import Conexao.Cliente;
import Conexao.Servidor;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Breno
 */
public class TelaInicial extends javax.swing.JFrame {

    /**
     * Creates new form TelaInicial
     */
    public TelaInicial() {
        initComponents();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        apelido = new javax.swing.JTextField();
        IniciarPartida = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        BuscarPartida = new javax.swing.JButton();
        jLabel4 = new javax.swing.JLabel();
        status = new javax.swing.JLabel();

        apelido.setToolTipText("O id local pode ser :192.168...");
        apelido.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                apelidoActionPerformed(evt);
            }
        });

        IniciarPartida.setText("Iniciar Partida");
        IniciarPartida.setActionCommand("BuscarPartida");
        IniciarPartida.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        IniciarPartida.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                IniciarPartidaActionPerformed(evt);
            }
        });

        jLabel1.setText("Apelido doJogador");

        jLabel2.setFont(new java.awt.Font("Tahoma", 0, 24)); // NOI18N
        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel2.setText("Jogo de Damas ");

        jLabel3.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel3.setText("Para iniciar é necessário inserir um apelido no Jogo:");

        BuscarPartida.setText("Buscar Partida");
        BuscarPartida.setActionCommand("BuscarPartida");
        BuscarPartida.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        BuscarPartida.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BuscarPartidaActionPerformed(evt);
            }
        });

        jLabel4.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel4.setText("Caso ja exista uma partida utilize a busca, caso contrário inicie uma nova");

        status.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(62, Short.MAX_VALUE)
                .addComponent(jLabel1)
                .addGap(18, 18, 18)
                .addComponent(apelido, javax.swing.GroupLayout.PREFERRED_SIZE, 178, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(74, 74, 74))
            .addComponent(jLabel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jLabel2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(layout.createSequentialGroup()
                .addGap(97, 97, 97)
                .addComponent(IniciarPartida)
                .addGap(18, 18, 18)
                .addComponent(BuscarPartida)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(status, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addComponent(jLabel2)
                .addGap(36, 36, 36)
                .addComponent(jLabel3)
                .addGap(18, 18, 18)
                .addComponent(jLabel4)
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(apelido, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel1))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(IniciarPartida)
                    .addComponent(BuscarPartida))
                .addGap(18, 18, 18)
                .addComponent(status, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(22, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void apelidoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_apelidoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_apelidoActionPerformed

    private void BuscarPartidaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BuscarPartidaActionPerformed
        // TODO add your handling code here:
        String nome = apelido.getText();
        if(!" ".equals(nome) || !"".equals(nome)){
            Cliente jogador = new Cliente(nome);
            jogador.conectarServidor();
            jogador.start();
            
            //jogador.mandarMensagem("Sair");   
        }
    }//GEN-LAST:event_BuscarPartidaActionPerformed

    private void IniciarPartidaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_IniciarPartidaActionPerformed
        status.setText("Aguardando outro jogador...");
        String nome = apelido.getText();
        if(!"".equals(nome) && !" ".equals(nome)){
            
            Servidor servidor;
            try {
                servidor = new Servidor(nome);
                servidor.start();
                    
                
            } catch (IOException ex) {
                Logger.getLogger(TelaInicial.class.getName()).log(Level.SEVERE, null, ex);
            } 
            
        }
       
    }//GEN-LAST:event_IniciarPartidaActionPerformed
                                       

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(TelaInicial.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(TelaInicial.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(TelaInicial.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(TelaInicial.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new TelaInicial().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton BuscarPartida;
    private javax.swing.JButton IniciarPartida;
    private javax.swing.JTextField apelido;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel status;
    // End of variables declaration//GEN-END:variables
}
