/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Interface;

/**
 *
 * @author Breno
 */
import Conexao.Cliente;
import Conexao.Servidor;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
import java.io.*;
import static java.lang.Math.abs;
import java.sql.Date;
import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

public class Tabuleiro extends JPanel implements ActionListener, MouseListener, Runnable {
    
    private static final long serialVersionUID = 1L; //Why? TODO GOOGLE

    /*variaveis para estabelecimento do tabuleiro*/
    public static JFrame frame;
    public static int width = 720, height = width; //square parameters. Optimized for any square resolution TODO any resolution to be squared
    public static final int tileSize = width / 8; //8 Tiles for checkers board
    public static final int numTilesPerRow = width / tileSize;
    public static int[][] gameData = new int[numTilesPerRow][numTilesPerRow]; //Stores piece data in an 8x8
    public static int[][] baseGameData = new int[numTilesPerRow][numTilesPerRow]; //Stores 8x8 board layout
    static BufferedImage crownImage = null;

    /*variaveis para o funcionamento do jogo*/
    public static final int EMPTY = 0, RED = 1, RED_KING = 2, WHITE = 3, WHITE_KING = 4, JUMP = 5, WALK = 6; //Values for gameData
    public ArrayList posicoesPulo;
    public boolean puloObrigatorio = false;
    public static int[][] availablePlays = new int[numTilesPerRow][numTilesPerRow]; //Stores available plays in an 8x8
    public static int[][] tipoMovimento = new int[numTilesPerRow][numTilesPerRow]; //Stores available plays in an 8x8
    public int storedRow, storedCol;
    public boolean isJump = false;
    public boolean noPieces = false;

    /*variaveis para estatísticas do jogo*/
    private int pontuacaoP1 = 0, pontuacaoP2 = 0;
    private int qtdDamasP1 = 0, qtdDamasP2 = 0;
    private LocalTime horarioPartida;
    private LocalDate dataPartida;
    private String nomeP1 , nomeP2;
    private boolean empate = false;


    /*variaveis de controle de turno*/
    public boolean inPlay = false; //Is there a move function processing?
    public int currentPlayer = RED;
    private int corPlayer1, corPlayer2;
    public boolean gameInProgress = true;
    private int colAt, linAt;

    /*Conexão com o outro jogador*/
    private Servidor servidor;
    private Cliente cliente;
    
    
    @Override
    public void run() {
        try {
            crownImage = ImageIO.read(new File("crown.png"));
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        window(width, height, this);
        initializeBoard();
        repaint(); // This is included in the JVM. Runs paint.
    }

    public Tabuleiro(Servidor servidor) {
        currentPlayer = RED;
        this.corPlayer1 = RED;
        this.corPlayer2 = WHITE;
        this.servidor = servidor;
        this.cliente = null;
        this.posicoesPulo = new ArrayList();
        this.dataPartida = LocalDate.now();
    }

    public Tabuleiro(Cliente cliente) {
        currentPlayer = EMPTY;
        this.corPlayer1 = WHITE;
        this.corPlayer2 = RED;
        this.servidor = null;
        this.cliente = cliente;
        this.posicoesPulo = new ArrayList();
        this.dataPartida = LocalDate.now();
    }
 public void setNomeP1(String nomeP1) {
        this.nomeP1 = nomeP1;
    }

    public void setNomeP2(String nomeP2) {
        this.nomeP2 = nomeP2;
    }

    //funções controle de variaveis
    public void resetPlay() {
        storedCol = 0;
        storedRow = 0;
        for (int row = 0; row < numTilesPerRow; row++) {
            for (int col = 0; col < numTilesPerRow; col++) {
                availablePlays[col][row] = 0;
            }
        }
        repaint();
    }

    public void zerarTipoMovimento() {
        for (int row = 0; row < numTilesPerRow; row++) {
            for (int col = 0; col < numTilesPerRow; col++) {
                tipoMovimento[col][row] = EMPTY;
            }
        }
    }

    public void zerarTipoAvailable() {
        for (int row = 0; row < numTilesPerRow; row++) {
            for (int col = 0; col < numTilesPerRow; col++) {
                availablePlays[col][row] = 0;
            }
        }
    }

    public void mousePressed(java.awt.event.MouseEvent evt) {
        int col = (evt.getX() - 8) / tileSize; // 8 is left frame length
        int row = (evt.getY() - 30) / tileSize; // 30 is top frame length
        colAt = col;
        linAt = row;
        if (puloObrigatorio && tipoMovimento[col][row] == JUMP || !puloObrigatorio) {
            if (inPlay == false && gameData[col][row] != 0 || inPlay == true && checkTeamPiece(col, row) == true) {
                resetPlay();
                inPlay = false;
                isJump = false;
                storedCol = col;
                storedRow = row; // Sets the current click to instance variables to be used elsewhere

                getAvailablePlays(col, row);
            } else if (inPlay == true && availablePlays[col][row] == 1) {
                makeMove(col, row, storedCol, storedRow);
            } else if (inPlay == true && availablePlays[col][row] == 0) {
                resetPlay();
                isJump = false;
            }
        } else {
            if (inPlay == true && availablePlays[col][row] == 1) {
                makeMove(col, row, storedCol, storedRow);
            } else if (inPlay == true && availablePlays[col][row] == 0) {
                resetPlay();
                isJump = false;
            }
        }
    }

    //operações de movimento
    public void atualizarPosicao(int col, int row, int storedCol, int storedRow) {
        int x = gameData[storedCol][storedRow]; //change the piece to new tile
        gameData[col][row] = x;
        gameData[storedCol][storedRow] = EMPTY; //change old piece location to EMPTY
        checkKing(col, row);

        if (abs((storedRow - row)) > 1) {
            removePiece(col, row, storedCol, storedRow);
        }
        resetPlay();
    }

    public void makeMove(int col, int row, int storedCol, int storedRow) {
        int x = gameData[storedCol][storedRow]; //change the piece to new tile
        gameData[col][row] = x;
        gameData[storedCol][storedRow] = EMPTY; //change old piece location to EMPTY
        checkKing(col, row);
        if (abs((storedRow - row)) > 1) {
            removePiece(col, row, storedCol, storedRow);
        }

        if (cliente == null) {
            servidor.mandarMovimento(col, row, storedCol, storedRow);
        } else {
            cliente.mandarMovimento(col, row, storedCol, storedRow);
        }
        resetPlay();
        if (currentPlayer == corPlayer1) {
            if (puloObrigatorio) {
                verificaMovimentoPecas();

                if (!puloObrigatorio) {
                    if (cliente == null) {
                        servidor.mandarMsg("NaoTenhoJogadas");
                    } else {
                        cliente.mandarMsg("NaoTenhoJogadas");
                    }
                    this.swapPlayer();
                }
            } else {
                this.swapPlayer();
            }
        }

    }

    public void getAvailablePlays(int col, int row) {
        inPlay = true;
        if ((checkTeamPiece(col, row) == true)) { //checks if the piece is assigned to the current player
            if (corPlayer1 == RED) {
                if (gameData[col][row] == RED) {  // only goes north, checks the row above it's own
                    getUp(col, row);
                }
                if (gameData[col][row] == WHITE) { // only goes south, checks the row below it's own
                    getDown(col, row);
                }
            } else {
                if (gameData[col][row] == WHITE) {  // only goes north, checks the row above it's own
                    getUp(col, row);
                }
                if (gameData[col][row] == RED) { // only goes south, checks the row below it's own
                    getDown(col, row);
                }
            }

            if (gameData[col][row] == RED_KING || gameData[col][row] == WHITE_KING) { // Goes up OR down 1 row below it's own
                getUp(col, row);
                //getUp(col, row);
                getDown(col, row); // GET UP GET UP AND GET DOWN
            }
            repaint();
        }
    }

    public void criaVetorPulo() {
        posicoesPulo.clear();
        for (int row2 = 0; row2 < 8; row2++) {
            for (int col2 = 0; col2 < 8; col2++) {
                if (tipoMovimento[col2][row2] == JUMP) {
                    int[] aux = {col2, row2};
                    posicoesPulo.add(aux);
                }
            }
        }
    }

    public void verificaMovimentoPecas() {
        zerarTipoMovimento();
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                if ((checkTeamPiece(col, row) == true)) { //checks if the piece is assigned to the current player
                    if (corPlayer1 == RED) {
                        if (gameData[col][row] == RED) {  // only goes north, checks the row above it's own
                            getUp(col, row);
                        }
                        if (gameData[col][row] == WHITE) { // only goes south, checks the row below it's own
                            getDown(col, row);
                        }
                    } else {
                        if (gameData[col][row] == WHITE) {  // only goes north, checks the row above it's own
                            getUp(col, row);
                        }
                        if (gameData[col][row] == RED) { // only goes south, checks the row below it's own
                            getDown(col, row);
                        }
                    }

                    if (gameData[col][row] == RED_KING || gameData[col][row] == WHITE_KING) { // Goes up OR down 1 row below it's own
                        getUp(col, row);
                        //getUp(col, row);
                        getDown(col, row); // GET UP GET UP AND GET DOWN
                    }
                }
                for (int row2 = 0; row2 < 8; row2++) {
                    for (int col2 = 0; col2 < 8; col2++) {
                        if (availablePlays[col2][row2] == 1) {// if possivel movimento
                            //peça normal ou rainha
                            if (row2 == row - 1 && (col2 == col + 1 || col2 == col - 1)) {// walk cima
                                if (tipoMovimento[col][row] != JUMP) {
                                    tipoMovimento[col][row] = WALK;
                                }
                            } else {
                                if (row2 == row - 2 && (col2 == col + 2 || col2 == col - 2)) { //jump cima
                                    tipoMovimento[col][row] = JUMP;
                                }
                            }
                            if (gameData[col][row] == RED_KING || gameData[col][row] == WHITE_KING) { // peça KING
                                if (row2 == row + 1 && (col2 == col + 1 || col2 == col - 1)) { //walk baixo
                                    if (tipoMovimento[col][row] != JUMP) {
                                        tipoMovimento[col][row] = WALK;
                                    }
                                } else {
                                    if (row2 == row + 2 && (col2 == col + 2 || col2 == col - 2)) { //jump baixo
                                        tipoMovimento[col][row] = JUMP;
                                    }
                                }
                            }
                        }
                    }
                }
                zerarTipoAvailable();
            }
        }
        criaVetorPulo();
        if (posicoesPulo.size() > 0) {
            puloObrigatorio = true;
        } else {
            puloObrigatorio = false;
        }
    }

    public void removePiece(int col, int row, int storedCol, int storedRow) { //might be a better way to do this, but detects position of opponent piece based on destination and original position
        int pieceRow = -1;
        int pieceCol = -1;
        if (col > storedCol && row > storedRow) {
            pieceRow = row - 1;
            pieceCol = col - 1;
        }
        if (col > storedCol && row < storedRow) {
            pieceRow = row + 1;
            pieceCol = col - 1;
        }
        if (col < storedCol && row > storedRow) {
            pieceRow = row - 1;
            pieceCol = col + 1;
        }
        if (col < storedCol && row < storedRow) {
            pieceRow = row + 1;
            pieceCol = col + 1;
        }
        gameData[pieceCol][pieceRow] = EMPTY;
    }//TODO REWRITE

    //Regras do jogo
    public void swapPlayer() {
        if (currentPlayer == corPlayer1) {
            currentPlayer = EMPTY;
        } else {
            currentPlayer = corPlayer1;
            verificaMovimentoPecas();
        }
    }

    public void getUp(int col, int row) { // Get Up availability
        int rowUp = row - 1;
        if (col == 0 && row != 0) { //X=0, Y is not 0
            for (int i = col; i < col + 2; i++) { //check to right
                if (gameData[col][row] != 0 && gameData[i][rowUp] != 0) {
                    if (canJump(col, row, i, rowUp) == true) {
                        int jumpCol = getJumpPos(col, row, i, rowUp)[0];
                        int jumpRow = getJumpPos(col, row, i, rowUp)[1];
                        availablePlays[jumpCol][jumpRow] = 1;
                    }
                } else if (baseGameData[i][rowUp] == 1 && gameData[i][rowUp] == 0) {
                    availablePlays[i][rowUp] = 1;
                }
            }
        } else if (col == (numTilesPerRow - 1) && row != 0) { //X=max, Y is not 0
            if (gameData[col][row] != 0 && gameData[col - 1][rowUp] != 0) {
                if (canJump(col, row, col - 1, rowUp) == true) {
                    int jumpCol = getJumpPos(col, row, col - 1, rowUp)[0];
                    int jumpRow = getJumpPos(col, row, col - 1, rowUp)[1];
                    availablePlays[jumpCol][jumpRow] = 1;
                }
            } else if (baseGameData[col - 1][rowUp] == 1 && gameData[col - 1][rowUp] == 0) {
                availablePlays[col - 1][rowUp] = 1;
            }
        } else if (col != numTilesPerRow - 1 && col != 0 && row != 0) {
            for (int i = col - 1; i <= col + 1; i++) {
                if (gameData[col][row] != 0 && gameData[i][rowUp] != 0) {
                    if (canJump(col, row, i, rowUp) == true) {
                        int jumpCol = getJumpPos(col, row, i, rowUp)[0];
                        int jumpRow = getJumpPos(col, row, i, rowUp)[1];
                        availablePlays[jumpCol][jumpRow] = 1;
                    }
                } else if (baseGameData[i][rowUp] == 1 && gameData[i][rowUp] == 0) {
                    availablePlays[i][rowUp] = 1;
                }
            }
        }
    }

    public void getDown(int col, int row) {
        int rowDown = row + 1;
        if (col == 0 && row != numTilesPerRow - 1) {
            if (gameData[col][row] != 0 && gameData[col + 1][rowDown] != 0) {
                if (canJump(col, row, col + 1, rowDown) == true) {
                    int jumpCol = getJumpPos(col, row, col + 1, rowDown)[0];
                    int jumpRow = getJumpPos(col, row, col + 1, rowDown)[1];
                    availablePlays[jumpCol][jumpRow] = 1;
                }
            } else if (baseGameData[col + 1][rowDown] == 1 && gameData[col + 1][rowDown] == 0) {
                availablePlays[col + 1][rowDown] = 1;
            }
        } else if (col == numTilesPerRow - 1 && row != numTilesPerRow - 1) {
            if (gameData[col][row] != 0 && gameData[col - 1][rowDown] != 0) {
                if (canJump(col, row, col - 1, rowDown) == true) {
                    int jumpCol = getJumpPos(col, row, col - 1, rowDown)[0];
                    int jumpRow = getJumpPos(col, row, col - 1, rowDown)[1];
                    availablePlays[jumpCol][jumpRow] = 1;
                }
            } else if (baseGameData[col - 1][rowDown] == 1 && gameData[col - 1][rowDown] == 0) {
                availablePlays[col - 1][rowDown] = 1;
            }
        } else if (col != numTilesPerRow - 1 && col != 0 && row != numTilesPerRow - 1) {
            for (int i = col - 1; i <= col + 1; i++) {
                if (gameData[col][row] != 0 && gameData[i][rowDown] != 0) {
                    if (canJump(col, row, i, rowDown) == true) {
                        int jumpCol = getJumpPos(col, row, i, rowDown)[0];
                        int jumpRow = getJumpPos(col, row, i, rowDown)[1];
                        availablePlays[jumpCol][jumpRow] = 1;
                    }
                } else if (baseGameData[i][rowDown] == 1 && gameData[i][rowDown] == 0) {
                    availablePlays[i][rowDown] = 1;
                }
            }
        }
    }

    
    
    public int gameOver() { //Wrapper for gameOverInternal
        return gameOverInternal(0, 0, 0, 0);
    }

    public int gameOverInternal(int col, int row, int red, int white) { //recursive practice
        if (gameData[col][row] == RED || gameData[col][row] == RED_KING) {
            red += 1;
        }
        if (gameData[col][row] == WHITE || gameData[col][row] == WHITE_KING) {
            white += 1;
        }
        if (col == numTilesPerRow - 1 && row == numTilesPerRow - 1) {
            if (red == 0)
                return WHITE;
            else
                if(white == 0)
                return RED;
                else return EMPTY;
            
        }
        if (col == numTilesPerRow - 1) {
            row += 1;
            col = -1;
        }
        return gameOverInternal(col + 1, row, red, white);
    }

    //funções de checagem
    public boolean podePular(int col, int row) {
        for (int i = 0; i < posicoesPulo.size(); i++) {
            int[] res = (int[]) posicoesPulo.get(i);
            if (res[0] == col && res[1] == row) {
                return true;
            }
        }
        return false;
    }

    public boolean isKing(int col, int row) {
        if (gameData[col][row] == RED_KING || gameData[col][row] == WHITE_KING) {
            return true;
        } else {
            return false;
        }
    }

    public int checkOpponent(int col, int row) {
        if (gameData[col][row] == RED || gameData[col][row] == RED_KING) {
            return WHITE;
        } else {
            return RED;
        }
    }

    public void checkExtraJumps(int col, int row) {
        int opponent = checkOpponent(col, row);
        int opponentKing = checkOpponent(col, row) + 1;
        if (gameData[col - 1][row - 1] == opponent || gameData[col - 1][row - 1] == opponentKing) {
            availablePlays[col - 1][row - 1] = 1;
        } else if (gameData[col + 1][row - 1] == opponent || gameData[col + 1][row - 1] == opponentKing) {
            availablePlays[col + 1][row - 1] = 1;
        } else if (gameData[col - 1][row + 1] == opponent || gameData[col - 1][row + 1] == opponentKing) {
            availablePlays[col - 1][row + 1] = 1;
        } else if (gameData[col + 1][row + 1] == opponent || gameData[col + 1][row + 1] == opponentKing) {
            availablePlays[col + 1][row + 1] = 1;
        }
        repaint();
    }

    public void checkKing(int col, int row) {
        if (gameData[col][row] == RED && row == 0 && servidor != null) {
            if (corPlayer1 == RED) {
                this.qtdDamasP1++;
            } else {
                this.qtdDamasP2++;
            }
            gameData[col][row] = RED_KING;
        } else if (gameData[col][row] == WHITE && row == numTilesPerRow - 1 && servidor != null) {
            if (corPlayer1 == WHITE) {
                this.qtdDamasP1++;
            } else {
                this.qtdDamasP2++;
            }
            gameData[col][row] = WHITE_KING;
        } else {
            if (gameData[col][row] == RED && row == numTilesPerRow - 1 && cliente != null) {
                if (corPlayer1 == RED) {
                    this.qtdDamasP1++;
                } else {
                    this.qtdDamasP2++;
                }
                gameData[col][row] = RED_KING;
            } else if (gameData[col][row] == WHITE && row == 0 && cliente != null) {
                if (corPlayer1 == WHITE) {
                    this.qtdDamasP1++;
                } else {
                    this.qtdDamasP2++;
                }
                gameData[col][row] = WHITE_KING;
            } else {
                return;
            }
        }
    }

    public boolean canJump(int col, int row, int opponentCol, int opponentRow) {
        if ((corPlayer1 == RED && (gameData[col][row] == RED || gameData[col][row] == RED_KING)) || (corPlayer1 == WHITE && (gameData[col][row] == WHITE || gameData[col][row] == WHITE_KING))) {
            //peça NORMAL
            //cima direita
            if (opponentRow == row - 1 && opponentCol == col + 1 && isLegalPos(col + 2, row - 2)) {
                if (gameData[col + 2][row - 2] == EMPTY && checkTeamPiece(opponentCol, opponentRow) == false) {
                    isJump = true;
                    return true;
                }
            }

            //cima esquerda
            if (opponentRow == row - 1 && opponentCol == col - 1 && isLegalPos(col - 2, row - 2)) {
                if (gameData[col - 2][row - 2] == EMPTY && checkTeamPiece(opponentCol, opponentRow) == false) {
                    isJump = true;
                    return true;
                }
            }
            // peça KING
            if ((corPlayer1 == RED && gameData[col][row] == RED_KING) || (corPlayer1 == WHITE && gameData[col][row] == WHITE_KING)) {
                //baixo direita
                if (opponentRow == row + 1 && opponentCol == col + 1 && isLegalPos(col + 2, row + 2)) {
                    if (gameData[col + 2][row + 2] == EMPTY && checkTeamPiece(opponentCol, opponentRow) == false) {
                        isJump = true;
                        return true;
                    }
                }

                //baixo esquerda
                if (opponentRow == row + 1 && opponentCol == col - 1 && isLegalPos(col - 2, row + 2)) {
                    if (gameData[col - 2][row + 2] == EMPTY && checkTeamPiece(opponentCol, opponentRow) == false) {
                        isJump = true;
                        return true;
                    }
                }
            } else {
                isJump = false;
                return false;
            }
        }
        isJump = false;
        return false;
    }

    public boolean checkTeamPiece(int col, int row) {
        if (currentPlayer == RED && (gameData[col][row] == RED || gameData[col][row] == RED_KING)) //bottom
        {
            return true;
        }
        if (currentPlayer == WHITE && (gameData[col][row] == WHITE || gameData[col][row] == WHITE_KING)) //top
        {
            return true;
        } else {
            return false;
        }
    }

    public boolean isLegalPos(int col, int row) {
        if (row < 0 || row >= numTilesPerRow || col < 0 || col >= numTilesPerRow) {
            return false;
        } else {
            return true;
        }
    }

    public int[] getJumpPos(int col, int row, int opponentCol, int opponentRow) {

        if (col > opponentCol && row > opponentRow && gameData[col - 2][row - 2] == 0) {
            return new int[]{col - 2, row - 2};
        } else if (col > opponentCol && row < opponentRow && gameData[col - 2][row + 2] == 0) {
            return new int[]{col - 2, row + 2};
        } else if (col < opponentCol && row > opponentRow && gameData[col + 2][row - 2] == 0) {
            return new int[]{col + 2, row - 2};
        } else {
            //return new int[]{col + 2, row - 2};
            if (corPlayer1 == WHITE && gameData[col][row] != WHITE_KING) {
                return new int[]{col - 2, row - 2};
            } else {
                return new int[]{col + 2, row + 2};
            }
        }
    }

    //metodos de interface grafica
    public void window(int width, int height, Tabuleiro game) { //draw the frame and add exit functionality
        JFrame frame = new JFrame();
        frame.setSize(width, height);
        frame.setIconImage(crownImage);
        frame.setBackground(Color.cyan);
        frame.setLocationRelativeTo(null);
        frame.pack();
        Insets insets = frame.getInsets();
        int frameLeftBorder = insets.left;
        int frameRightBorder = insets.right;
        int frameTopBorder = insets.top;
        int frameBottomBorder = insets.bottom;
        frame.setPreferredSize(new Dimension(width + frameLeftBorder + frameRightBorder, height + frameBottomBorder + frameTopBorder));
        frame.setMaximumSize(new Dimension(width + frameLeftBorder + frameRightBorder, height + frameBottomBorder + frameTopBorder));
        frame.setMinimumSize(new Dimension(width + frameLeftBorder + frameRightBorder, height + frameBottomBorder + frameTopBorder));
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.addMouseListener(this);
        frame.requestFocus();
        frame.setVisible(true);
        frame.add(game);
    }

    public void initializeBoard() {
        zerarTipoAvailable();
        zerarTipoMovimento();
        isJump = false;
        puloObrigatorio = false;
        currentPlayer = RED;
        
        //UPDATE THE STARTING POSITIONS
        for (int col = 0; col < (numTilesPerRow); col += 2) {
            gameData[col][5] = corPlayer1;
            gameData[col][7] = corPlayer1;
            gameData[col][3] = EMPTY;
        }
        for (int col = 1; col < (numTilesPerRow); col += 2) {
            gameData[col][6] = corPlayer1;
            gameData[col][4] = EMPTY;
        }
        for (int col = 1; col < (numTilesPerRow); col += 2) {
            gameData[col][0] = corPlayer2;
            gameData[col][2] = corPlayer2;
        }
        for (int col = 0; col < (numTilesPerRow); col += 2) {
            gameData[col][1] = corPlayer2;
        }
    }

    public static void drawPiece(int col, int row, Graphics g, Color color) {
        ((Graphics2D) g).setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        ((Graphics2D) g).setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        g.setColor(color);
        // These 2 and 4 values are arbitrary values that compensate for a smaller piece size than tileSize
        g.fillOval((col * tileSize) + 2, (row * tileSize) + 2, tileSize - 4, tileSize - 4);
    }

    public void paint(Graphics g) { // This method paints the board
        //PRINT THE BOARD & PIECES
        super.paintComponent(g);
        int ganhou;
        String playerWin;
        for (int row = 0; row < numTilesPerRow; row++) {
            for (int col = 0; col < numTilesPerRow; col++) {
                if ((row % 2 == 0 && col % 2 == 0) || (row % 2 != 0 && col % 2 != 0)) { // This assigns the checkerboard pattern
                    baseGameData[col][row] = 0;
                    g.setColor(Color.gray);
                    g.fillRect(col * tileSize, row * tileSize, tileSize, tileSize);
                } else {
                    baseGameData[col][row] = 1;
                    g.setColor(Color.darkGray);
                    g.fillRect(col * tileSize, row * tileSize, tileSize, tileSize);
                }
                if (checkTeamPiece(col, row) == true) {
                    g.setColor(Color.darkGray.darker());
                    g.fillRect(col * tileSize, row * tileSize, tileSize, tileSize);
                }

                if (availablePlays[col][row] == 1) {
                    if (puloObrigatorio && tipoMovimento[colAt][linAt] == JUMP) {
                        if (col <= (colAt + 1) && col >= (colAt - 1)) { // esta em +1 ou -1 de coluna
                            if (row <= (linAt + 1) && row >= (linAt - 1)) { // esta em +1 ou -1 de coluna
                                availablePlays[col][row] = 0;
                            } else {
                                g.setColor(Color.CYAN.darker());
                                g.fillRect(col * tileSize, row * tileSize, tileSize, tileSize);
                            }
                        } else {
                            g.setColor(Color.CYAN.darker());
                            g.fillRect(col * tileSize, row * tileSize, tileSize, tileSize);
                        }
                    } else {
                        g.setColor(Color.CYAN.darker());
                        g.fillRect(col * tileSize, row * tileSize, tileSize, tileSize);

                    }
                }
                if (gameData[col][row] == WHITE) {
                    drawPiece(col, row, g, Color.white);
                } else if (gameData[col][row] == WHITE_KING) {
                    drawPiece(col, row, g, Color.white);
                    g.drawImage(crownImage, (col * tileSize) + 6, (row * tileSize) + 6, tileSize - 12, tileSize - 12, null);
                } else if (gameData[col][row] == RED) {
                    drawPiece(col, row, g, Color.red);
                } else if (gameData[col][row] == RED_KING) {
                    drawPiece(col, row, g, Color.red);
                    g.drawImage(crownImage, (col * tileSize) + 6, (row * tileSize) + 6, tileSize - 12, tileSize - 12, null);
                }
            }
        }
        ganhou  = gameOver();
        if (ganhou != EMPTY) {
            
            if (cliente == null) {  
                /*Date data, Time horario, String jogador_1, String jogador_2, String vencedor, boolean empate*/
                /*
                inserirRegistroPartida(
                Date.valueOf(this.dataPartida) ,
                Time.valueOf(this.horarioPartida) ,
                this.nomeP1 ,
                this.nomeP2 ,
                playerWin ,
                this.emapate
                )
                */
                
                /*nome , */
                /*
                
                */
                if(ganhou == this.corPlayer1)
                        playerWin = this.nomeP1;
                        else
                            playerWin = this.nomeP2;
                        
            servidor.mandarMsg("Sair:"+ganhou);
        } else {
            cliente.mandarMensagem("Sair:"+ganhou);
        }
            gameOverDisplay(g);
        }
    }

    public void gameOverDisplay(Graphics g) { //Displays the game over message
        String msg = "Game Over";
        Font small = new Font("Helvetica", Font.BOLD, 30);
        FontMetrics metr = getFontMetrics(small);
        g.setColor(Color.white);
        g.setFont(small);
        g.drawString(msg, (width - metr.stringWidth(msg)) / 2, width / 2);
    }

    public int getCorPlayer1() {
        return corPlayer1;
    }

    // Methods that must be included for some reason? WHY
    public void mouseClicked(MouseEvent e) {
    }

    public void mouseReleased(MouseEvent e) {
    }

    public void mouseEntered(MouseEvent e) {
    }

    public void mouseExited(MouseEvent e) {
    }

    public void actionPerformed(ActionEvent e) {
    }

}
