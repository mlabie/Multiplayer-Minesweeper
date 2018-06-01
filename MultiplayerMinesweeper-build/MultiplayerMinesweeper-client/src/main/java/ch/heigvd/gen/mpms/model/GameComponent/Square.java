package ch.heigvd.gen.mpms.model.GameComponent;

public class Square {
    private String playerName;
    private int value, x, y;
    private boolean swept;
    private boolean bonus;
    private boolean malus;

    public Square(int x, int y){
        this.x = x;
        this.y = y;
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
