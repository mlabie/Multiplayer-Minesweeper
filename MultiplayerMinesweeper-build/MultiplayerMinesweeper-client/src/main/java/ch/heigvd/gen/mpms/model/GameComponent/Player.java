package ch.heigvd.gen.mpms.model.GameComponent;

/**
 * @brief: This class represent a player, which is represented by a name and the score
 *         he reached during a game.
 *
 * @author Corentin Basler, Antonio Cusanelli, Marc Labie, Simon Jobin
 */
public class Player {

    private String playerName;
    private int score;


    public Player(String playerName) {
        this.playerName = playerName;
        this.score = 0;
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

}

