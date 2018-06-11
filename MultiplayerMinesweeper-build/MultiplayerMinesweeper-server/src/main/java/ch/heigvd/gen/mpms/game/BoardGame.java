package ch.heigvd.gen.mpms.game;

import ch.heigvd.gen.mpms.GameComponent.Configuration;
import ch.heigvd.gen.mpms.GameComponent.Player;

import java.util.Random;
import java.util.Vector;


public class BoardGame {
	private Square[][] board;
	private Vector<Square> tabOfMineOfTheBoard;
	private int size;
	private boolean bonusMalusEnable;
	private Configuration config;
	private final int mineValue = 10;
	private int valueOfIncrScore = 10;
	private final double ratioBonus = 0.005;
	private final double ratioMalus = 0.007;

	private int mineAmount;
	private int squareAmount;
	private int squareSweptAmount;


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
			} while(board[x][y].getValue() == mineValue);
			board[x][y].setValue(mineValue);

			//update of the adjacent mine's square with their value
			int I = x, J = y; //current position
			for(int dirX = -1; dirX <= 1; ++dirX) {
				for (int dirY = -1; dirY <= 1; ++dirY) {
					//exclude the case (0,0)
					if (dirX != 0 || dirY != 0) {
						if (I + dirX >= 0 && I + dirX < height &&
								J + dirY >= 0 && J + dirY < width) {

							int value = board[I + dirX][J + dirY].getValue();
							if (value != mineValue)
								board[I + dirX][J + dirY].setValue(value + 1);
						}
					}
				}
			}
		}


		if(bonusMalusEnable) {
			//insert the bonus in the board
			for(int i = 0; i < ratioBonus * size; ++i) {
				int x;
				int y;
				do {
					x = random.nextInt(height);
					y = random.nextInt(width);
				} while (board[x][y].getBonus() != BonusType.NONE || board[x][y].getValue() == mineValue || board[x][y].getValue() == 0);
				int bonus = random.nextInt(2);
				board[x][y].setBonus(BonusType.values()[bonus]);
			}

			//insert the malus in the board
			for(int i = 0; i < ratioMalus * size; ++i) {
				int x;
				int y;
				do {
					x = random.nextInt(height);
					y = random.nextInt(width);
				} while (board[x][y].getBonus() != BonusType.NONE || board[x][y].getMalus() != MalusType.NONE || board[x][y].getValue() == mineValue
						 || board[x][y].getValue() == 0);
				int malus = random.nextInt(2);
				board[x][y].setMalus(MalusType.values()[malus]);
			}
		}

		//this tab will be sent to the players who died to show them the mines in the GUI
		tabOfMineOfTheBoard = minesOfTheBoard();

		mineAmount     	   = tabOfMineOfTheBoard.size();
		squareAmount 	   = width * height;
		squareSweptAmount  = 0;

	}

	public int getMineAmount() {
		return mineAmount;
	}

	public int getSquareAmount() {
		return squareAmount;
	}

	public int getSquareSweptAmount() {
		return squareSweptAmount;
	}

	private Vector<Square> minesOfTheBoard(){
		Vector<Square> tabOfMine = new Vector<>();
		for(int i = 0; i < config.getHeight(); ++i) {
			for (int j = 0; j < config.getWidth(); ++j) {
				if(board[i][j].getValue() == mineValue)
					tabOfMine.add(board[i][j]);
			}
		}
		return tabOfMine;
	}


	public Vector<Square> getTabOfMineOfTheBoard() {
		return tabOfMineOfTheBoard;
	}


	public Configuration getConfig() {
		return config;
	}


	//false : mine, true : ok, if Square already swept, the ArrayList is empty
	public synchronized boolean sweep(int x, int y, Player player, Vector<Square> tabOfSquare) {
		if(!board[x][y].isSwept()) {
			int I = x, J = y;

			//if a mine is discovered, the square won't change for the other players so they could fall in it
			if (board[I][J].getValue() == mineValue) {
				return false;
			}

			board[I][J].setSwept(true);
			board[I][J].setPlayerName(player.getPlayerName());

			//add bonus to score if the square contained a bonus
			if(board[I][J].getBonus() == BonusType.BONUS1){
				player.addScore(50);
			}else if (board[I][J].getBonus() == BonusType.BONUS2){
				player.addScore(100);
			}else if(board[I][J].getBonus() == BonusType.BONUS3){
				player.addScore(150);
			}

			//add malus to score if the square contained a malus
			if(board[I][J].getMalus() == MalusType.MALUS1){
				player.addScore(-40);
			}else if (board[I][J].getMalus() == MalusType.MALUS1){
				player.addScore(-90);
			}else if(board[I][J].getMalus() == MalusType.MALUS1){
				player.addScore(-130);
			}

			//add score to player according to the playing's mode and the value of the square
			if(config.getScore().equals(Configuration.ScoreMode.EXPLORER)){
				player.addScore(valueOfIncrScore);
			}else if (config.getScore().equals(Configuration.ScoreMode.STANDARD)){
				player.addScore(valueOfIncrScore + board[I][J].getValue() * 100);
			}else{
				player.addScore(board[I][J].getValue() * 100);
			}

			tabOfSquare.add(board[I][J]);
			squareSweptAmount++;


			//Current square can't be adjacent of a mine's square to follow the research in the board
			if (board[I][J].getValue() == 0) {
				for (int dirX = -1; dirX <= 1; ++dirX) {
					for (int dirY = -1; dirY <= 1; ++dirY) {
						//exclude the case (0,0) and bonus/malus
						if (dirX != 0 || dirY != 0) {
							if (I + dirX >= 0 && I + dirX < config.getHeight() && J + dirY >= 0 && J + dirY <
									config.getWidth() && !board[I + dirX][J + dirY].isSwept() &&
									!(board[I + dirX][J + dirY].getMalus() != MalusType.NONE
											|| board[I + dirX][J + dirY].getBonus() != BonusType.NONE)) {
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
		//show the boardGame to the console
		String game = "";
		for(int i = 0; i < config.getHeight(); ++i) {
			String line = "";
			for(int j = 0; j < config.getWidth(); ++j) {
				Square s = board[i][j];
				if(s.isSwept()){
					line += "  ";
				}else if (s.getBonus() != BonusType.NONE){
					line += "B ";
				}else if (s.getMalus() != MalusType.NONE){
					line += "M ";
				}else {
					line += s.getValue() == mineValue ? "* " : s.getValue() + " ";
				}
			}
			game += line + "\n";
		}
		return game;
	}
}
