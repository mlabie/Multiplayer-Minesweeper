package ch.heigvd.gen.mpms.model.GameComponent;

/**
 * @brief: This class represent a player, which is represented by a name and the score
 *         he reached during a game.
 *
 * @author Corentin Basler, Antonio Cusanelli, Marc Labie, Simon Jobin
 */
public class Player {

    public static int playerNumber = 0;

    private String playerName;
    private int score;
    private int number;


    public Player(String playerName) {
        this.playerName = playerName;
        this.score      = 0;
        this.number     = ++playerNumber;
    }

    public String getPlayerName() {
        return playerName;
    }

    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }
}

