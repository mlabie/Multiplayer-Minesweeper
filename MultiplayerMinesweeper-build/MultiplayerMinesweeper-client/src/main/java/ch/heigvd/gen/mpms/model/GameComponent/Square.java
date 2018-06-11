package ch.heigvd.gen.mpms.model.GameComponent;
import com.google.gson.annotations.SerializedName;

public class Square {

	public enum BonusType {
		@SerializedName("${BONUS1}")
		BONUS1,
		@SerializedName("${BONUS2}")
		BONUS2,
		@SerializedName("${BONUS3}")
		BONUS3,
		@SerializedName("${NONE}")
		NONE
	}

	public enum MalusType {
		@SerializedName("${MALUS1}")
		MALUS1,
		@SerializedName("${MALUS2}")
		MALUS2,
		@SerializedName("${MALUS3}")
		MALUS3,
		@SerializedName("${NONE}")
		NONE
	}


	private String playerName;
	private int value, x, y;
	private boolean swept;

	@SerializedName("${BonusType}")
	private BonusType bonus;

	@SerializedName("${MalusType}")
	private MalusType malus;

	public Square(){}

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
