package ch.heigvd.gen.mpms.game;

import ch.heigvd.gen.mpms.GameComponent.Configuration;
import ch.heigvd.gen.mpms.GameComponent.Player;

import java.util.Random;
import java.util.Vector;

public class BoardGame {
	private Square[][] board;
	private int size;
	private boolean bonusMalusEnable;
	private Configuration config;
	private final int idMine = 10;
	private int valueOfIncrScore = 10;
	private final double ratioBonus = 0.005;
	private final double ratioMalus = 0.007;

	public BoardGame(Configuration configuration){
		this.config = configuration;
		bonusMalusEnable = config.isBonus();
		Random random = new Random();

		int width = config.getWidth();
		int height = config.getHeight();
		board = new Square[height][width];
		size = width * height;

		for (int i = 0; i < height; i++){
			for (int j = 0; j < width; j++){
				board[i][j] = new Square(i, j);
			}
		}

		//repartition of mines into the board
		for (int i = 0; i < config.getMineProportion() / 100. * size; i++){

			int x;
			int y;
			do {
				x = random.nextInt(height);
				y = random.nextInt(width);
			} while(board[x][y].getValue() == idMine);
			board[x][y].setValue(idMine);

			//mise a jour des cases adjacentes aux mines
			//position courante
			int I = x, J = y;
			for(int dirX = -1; dirX <= 1; ++dirX) {
				for (int dirY = -1; dirY <= 1; ++dirY) {
					//exclut le cas (0,0)
					if (dirX != 0 || dirY != 0) {
						if (I + dirX >= 0 && I + dirX < height &&
								J + dirY >= 0 && J + dirY < width) {

							int value = board[I + dirX][J + dirY].getValue();
							if (value != idMine)
								board[I + dirX][J + dirY].setValue(value + 1);
						}
					}
				}
			}
		}


		if(bonusMalusEnable) {
			//liste de coordonnees pour les bonus / malus
			for(int i = 0; i < ratioBonus * size; ++i) {
				int x;
				int y;
				do {
					x = random.nextInt(height);
					y = random.nextInt(width);
				} while (board[x][y].isBonus() || board[x][y].getValue() == idMine || board[x][y].getValue() == 0);
				board[x][y].setBonus(true);
			}

			for(int i = 0; i < ratioMalus * size; ++i) {
				int x;
				int y;
				do {
					x = random.nextInt(height);
					y = random.nextInt(width);
				} while (board[x][y].isBonus() || board[x][y].isMalus() || board[x][y].getValue() == idMine
						 || board[x][y].getValue() == 0);
				board[x][y].setMalus(true);
			}

		}
	}


	public Configuration getConfig() {
		return config;
	}

	//false : mine, true : ok, if Square already swept, the ArrayList is empty
	public synchronized boolean sweep(int x, int y, Player player, Vector<Square> tabOfSquare) {
		if(!board[x][y].isSwept()) {
			int I = x, J = y;

			//if a mine is discovered, the square won't change for the other players so they could fall in it
			if (board[I][J].getValue() == idMine) {
				return false;
			}

			board[I][J].setSwept(true);
			board[I][J].setPlayerName(player.getPlayerName());
			if(config.getScore().equals(Configuration.ScoreMode.EXPLORER)){
				player.addScore(valueOfIncrScore);
			}else if (config.getScore().equals(Configuration.ScoreMode.STANDARD)){
				player.addScore(valueOfIncrScore + board[I][J].getValue() * 100);
			}else{
				player.addScore(board[I][J].getValue() * 100);
			}

			tabOfSquare.add(board[I][J]);


			//il faut que la case courante ne soit pas adjacente
			//a des mines pour poursuivre la recherche autour
			if (board[I][J].getValue() == 0) {
				for (int dirX = -1; dirX <= 1; ++dirX) {
					for (int dirY = -1; dirY <= 1; ++dirY) {
						//exclut le cas (0,0)
						if (dirX != 0 || dirY != 0) {
							if (I + dirX >= 0 && I + dirX < config.getHeight() && J + dirY >= 0 && J + dirY <
									config.getWidth() && !board[I + dirX][J + dirY].isSwept() &&
									!(board[I + dirX][J + dirY].isMalus() || board[I + dirX][J + dirY].isBonus())) {
								sweep(I + dirX, J + dirY, player, tabOfSquare);
							}
						}
					}
				}
			}
		}
		return true;
	}

	public String toString() {
		String game = "";
		for(int i = 0; i < config.getHeight(); ++i) {
			String line = "";
			for(int j = 0; j < config.getWidth(); ++j) {
				Square s = board[i][j];
				if(s.isSwept()){
					line += "  ";
				}else if (s.isBonus()){
					line += "B ";
				}else if (s.isMalus()){
					line += "M ";
				}else {
					line += s.getValue() == idMine ? "* " : s.getValue() + " ";
				}
			}
			game += line + "\n";
		}
		return game;
	}
}
