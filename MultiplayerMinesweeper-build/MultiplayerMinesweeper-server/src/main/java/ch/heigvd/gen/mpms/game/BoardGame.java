package ch.heigvd.gen.mpms.game;

import java.util.Random;

public class BoardGame {
	private Square[][] board;
	private int size;
	private boolean bonusMalusEnable;
	private static final int idMine = 10;
	private final double ratioBonus = 0.02;
	private final double ratioMalus = 0.02;


	public BoardGame(int size, int nbOfMine, boolean bonusMalus){
		bonusMalusEnable = bonusMalus;
		Random random = new Random();
		this.size = size;

		board = new Square[size][size];

		for (int i = 0; i < size; i++){
			for (int j = 0; j < size; j++){
				board[i][j] = new Square();
			}
		}

		for (int i = 0; i < nbOfMine; i++){

			int x;
			int y;
			do {
				x = random.nextInt(size);
				y = random.nextInt(size);
			} while(board[x][y].getValue() == idMine);
			board[x][y].setValue(idMine);

			//mise a jour des cases adjacentes aux mines
			//position courante
			int I = x, J = y;
			for(int dirX = -1; dirX <= 1; ++dirX) {
				for (int dirY = -1; dirY <= 1; ++dirY) {
					//exclut le cas (0,0)
					if (dirX != 0 || dirY != 0) {
						if (I + dirX >= 0 && I + dirX < size &&
								J + dirY >= 0 && J + dirY < size) {

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
			for(int i = 0; i < ratioBonus * (size * size); ++i) {
				int x;
				int y;
				do {
					x = random.nextInt(size);
					y = random.nextInt(size);
				} while (board[x][y].isBonus() || board[x][y].getValue() == idMine || board[x][y].getValue() == 0);
				board[x][y].setBonus(true);
			}

			for(int i = 0; i < ratioMalus * (size * size); ++i) {
				int x;
				int y;
				do {
					x = random.nextInt(size);
					y = random.nextInt(size);
				} while (board[x][y].isBonus() || board[x][y].isMalus() || board[x][y].getValue() == idMine
						 || board[x][y].getValue() == 0);
				board[x][y].setMalus(true);
			}

		}
	}

	public synchronized boolean sweep(int x, int y) {
		if(!board[x][y].isSwept()){
			board[x][y].setSwept(true);

			int I = x, J = y;

			//il faut que la case courante ne soit pas adjacente
			//a des mines pour poursuivre la recherche autour
			if(board[I][J].getValue() == 0) {
				for (int dirX = -1; dirX <= 1; ++dirX) {
					for (int dirY = -1; dirY <= 1; ++dirY) {
						//exclut le cas (0,0)
						if (dirX != 0 || dirY != 0) {
							if (I + dirX >= 0 && I + dirX < size && J + dirY >= 0 && J + dirY < size &&
									!board[I + dirX][J + dirY].isSwept()) {
								sweep(I + dirX, J + dirY);
							}
						}
					}
				}
			}
			return true;
		}else
			return false;
	}

	public String toString() {
		String game = "";
		for(int i = 0; i < size; ++i) {
			String line = "";
			for(int j = 0; j < size; ++j) {
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
