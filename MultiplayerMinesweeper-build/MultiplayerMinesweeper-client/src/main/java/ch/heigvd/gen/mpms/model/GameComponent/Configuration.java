package ch.heigvd.gen.mpms.model.GameComponent;

public class Configuration {

    public enum ScoreMode{
        STANDARD,
        EXPLORER,
        HARDSWEEPER
    }

    public enum Difficulty{
        EASY(6),
        MEDIUM(15),
        HARD(24);

        private final int difficulty;
        Difficulty(int difficulty) { this.difficulty = difficulty; }
        public int getValue() { return difficulty; }
    }

    public static final int MAX_WIDTH   =  48;
    public static final int MAX_HEIGHT  =  28;

    public static final int MIN_WIDTH   =  16;
    public static final int MIN_HEIGHT  =  16;

    public static final int PROPORTION_MIN  =  5;
    public static final int PROPORTION_MAX  =  30;

    public static final int MIN_SLOT  =  2;
    public static final int MAX_SLOT  =  4;

    private String     name;

    private ScoreMode  score;
    private int        nbrSlot;
    private int        mineProportion;
    private boolean    bonus;
    private boolean    malus;
    private int        width;
    private int        height;


	public Configuration(){
		this.name           = "";
		this.score          = ScoreMode.STANDARD;
		this.nbrSlot        = MAX_SLOT;
		this.mineProportion = Difficulty.MEDIUM.getValue();
		this.bonus          = false;
		this.malus          = false;
		this.width          = 16;
		this.height         = 16;
	}

    public Configuration(String name){
        this.name           = name;
        this.score          = ScoreMode.STANDARD;
        this.nbrSlot        = MAX_SLOT;
        this.mineProportion = Difficulty.MEDIUM.getValue();
        this.bonus          = false;
        this.malus          = false;
        this.width          = 16;
        this.height         = 16;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ScoreMode getScore() {
        return score;
    }

    public void setScore(ScoreMode score) {
        this.score = score;
    }

    public int getNbrSlot() {
        return nbrSlot;
    }

    public void setNbrSlot(int nbrSlot) {
        if(nbrSlot > MAX_SLOT || nbrSlot < MIN_SLOT){
            throw new IllegalArgumentException("The number of slot is not allowed.");
        }
        this.nbrSlot = nbrSlot;
    }

    public int getMineProportion() {
        return mineProportion;
    }

    public void setMineProportion(int mineProportion) {
        if(mineProportion > PROPORTION_MAX || mineProportion < PROPORTION_MIN){
            throw new IllegalArgumentException("This difficulty is not allowed.");
        }
        this.mineProportion = mineProportion;
    }

    public boolean isBonus() {
        return bonus;
    }

    public void setBonus(boolean bonus) {
        this.bonus = bonus;
    }

    public boolean isMalus() {
        return malus;
    }

    public void setMalus(boolean malus) {
        this.malus = malus;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        if(width > MAX_WIDTH || width < MIN_WIDTH){
            throw  new IllegalArgumentException("The width is not allowed.");
        }
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        if(height > MAX_HEIGHT || height < MIN_HEIGHT){
            throw  new IllegalArgumentException("The width is not allowed.");
        }
        this.height = height;
    }
}
