package ch.heigvd.gen.mpms.GameComponent;

import ch.heigvd.gen.mpms.model.net.server.ServantWorker;

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
    private boolean       isAlive;

    public Player(String playerName){
    	this.playerName = playerName;
	}
    public Player(ServantWorker client){
        this(client, "");
    }

    public Player(ServantWorker client, String playerName){
        this.client     = client;
        this.playerName = playerName;
        this.score      = 0;
        this.isAlive    = true;
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

    public void addScore(int score){
        this.score += score;
    }

    public void setScore(int score){
        this.score = score;
    }

    public boolean getIsAlive(){
        return isAlive;
    }

    public ServantWorker getClient(){
        return client;
    }

    public void kill(){
        isAlive = false;
    }
}
