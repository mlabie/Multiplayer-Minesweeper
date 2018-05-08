package ch.heigvd.gen.mpms.GameComponent;

import ch.heigvd.gen.mpms.net.server.ServantWorker;

/**
 * @brief: This class represent a player, which is represented by a name, a ServantWorker, and the score
 *         he reached during a game.
 *
 * @author Corentin Basler, Antonio Cusanelli, Marc Labie, Simon Jobin
 */
public class Player {

    private ServantWorker client;
    private String        playerName;
    private int           score;

    public Player(ServantWorker client){
        this(client, "");
    }

    public Player(ServantWorker client, String playerName){
        this.client     = client;
        this.playerName = playerName;
        this.score      = 0;
    }

    public String getPlayerName(){
        return playerName;
    }

    public void setPlayerName(String playerName){
        this.playerName = playerName;
    }

    public int getScore(){
        return score;
    }

    public void setScore(int score){
        this.score = score;
    }

    public ServantWorker getClient(){
        return client;
    }


}
