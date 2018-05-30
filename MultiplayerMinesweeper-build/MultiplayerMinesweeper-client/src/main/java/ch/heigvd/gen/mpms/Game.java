package model;

import java.util.Random;

public class Game {

    private static final int boardSize = 10;
    private static final int nbMines = 10;
    private static final int idMine = 9;
    private static final int idBonus = 10;
    private static final int idMalus = 11;
    private boolean bonusMalus = false;
    private static final int ratioBonus = 5;

    //pour l'instant, a la sale en mode public
    public int[][] board;
    public boolean[][] discovered;
    private Random random = new Random();

    public Game() {
        // default : no mine, have to init board
        board = new int[boardSize][boardSize];
        discovered = new boolean[boardSize][boardSize];
        init();
    }

    public void init() {
        for(int i = 0; i < nbMines; ++i) {
            //repartition aleatoire des mines
            int x = 0;
            int y = 0;
            do {
                x = random.nextInt(boardSize);
                y = random.nextInt(boardSize);
            } while(board[x][y] == idMine);
            board[x][y] = idMine;

            //mise a jour des cases adjacentes aux mines
            //position courante
            int I = x, J = y;
            for(int dirX = -1; dirX <= 1; ++dirX) {
                for (int dirY = -1; dirY <= 1; ++dirY) {
                    //exclut le cas (0,0)
                    if (dirX != 0 || dirY != 0) {
                        if (I + dirX >= 0 && I + dirX < boardSize &&
                                J + dirY >= 0 && J + dirY < boardSize) {

                            if (board[I + dirX][J + dirY] != idMine)
                                board[I + dirX][J + dirY]++;
                        }
                    }
                }
            }
        }

        if(bonusMalus) {
            /*
            for(int i = 0; i < ratioBonus / 100. * (boardSize * boardSize); ++i) {
                int x = 0;
                int y = 0;
                do {
                    x = random.nextInt(boardSize);
                    y = random.nextInt(boardSize);
                } while (board[x][y] == idBonus );
                board[x][y] = idBonus;
            }
            */

            //liste de coordonnees pour les bonus / malus
        }
    }

    public String toString() {
        String game = "";
        for(int i = 0; i < boardSize; ++i) {
            String line = "";
            for(int j = 0; j < boardSize; ++j) {
                line += board[i][j] == idMine ? "* " : board[i][j] + " ";
            }
            game += line + "\n";
        }
        return game;
    }

    public int getSquare(int x, int y) {
        return board[x][y];
    }

    public void sweep(int x, int y) {
        discovered[x][y] = true;
        int I = x, J = y;

        //il faut que la case courante ne soit pas adjacente
        //a des mines pour poursuivre la recherche autour
        if(board[I][J] == 0) {
            for (int dirX = -1; dirX <= 1; ++dirX) {
                for (int dirY = -1; dirY <= 1; ++dirY) {
                    //exclut le cas (0,0)
                    if (dirX != 0 || dirY != 0) {
                        if (I + dirX >= 0 && I + dirX < boardSize &&
                                J + dirY >= 0 && J + dirY < boardSize
                                && discovered[I + dirX][J + dirY] == false) {
                            sweep(I + dirX, J + dirY);
                        }
                    }
                }
            }
        }
    }
 }
