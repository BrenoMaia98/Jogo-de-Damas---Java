/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Conexao;


/**
 *
 * @author anach
 */
public class Jogador {
    private String nome;
    private char tipo; //b: brancas, p:pretas
    
    public Jogador() {
    }

    public Jogador(String nome, char tipo) {
        this.nome = nome;
        this.tipo = tipo;
    }
    
    public void setNome(String nome) {
        this.nome = nome;
    }

    public void setTipo(char tipo) {
        this.tipo = tipo;
    }

    public String getNome() {
        return nome;
    }

    
}
