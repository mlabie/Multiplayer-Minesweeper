package ch.heigvd.gen.mpms.game;

public class Square {
	private String playerName;
	private int value;
	private boolean swept;
	private boolean bonus;
	private boolean malus;

	public Square(){}

	public String getPlayerName() {
		return playerName;
	}

	public void setPlayerName(String playerName) {
		this.playerName = playerName;
	}


	public void setValue(int value) {
		this.value = value;
	}

	public int getValue() {
		return value;
	}


	public void setSwept(boolean swept) {
		this.swept = swept;
	}

	public boolean isSwept() {
		return swept;
	}


	public void setBonus(boolean bonus) {
		this.bonus = bonus;
	}

	public boolean isBonus() {
		return bonus;
	}


	public void setMalus(boolean malus) {
		this.malus = malus;
	}

	public boolean isMalus() {
		return malus;
	}
}
