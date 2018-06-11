package ch.heigvd.gen.mpms.game;

enum BonusType {BONUS1, BONUS2, BONUS3, NONE}
enum MalusType {MALUS1, MALUS2, MALUS3, NONE}
public class Square {
	private String playerName;
	private int value, x, y;
	private boolean swept;
	private BonusType bonus;
	private MalusType malus;

	public Square(int x, int y){
		this.x = x;
		this.y = y;
		bonus = BonusType.NONE;
		malus = MalusType.NONE;
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}


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

	public BonusType getBonus() {
		return bonus;
	}

	public void setBonus(BonusType bonus) {
		this.bonus = bonus;
	}

	public MalusType getMalus() {
		return malus;
	}

	public void setMalus(MalusType malus) {
		this.malus = malus;
	}
}
