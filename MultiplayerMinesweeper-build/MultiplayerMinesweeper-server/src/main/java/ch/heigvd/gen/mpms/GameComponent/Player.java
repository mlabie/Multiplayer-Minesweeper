package ch.heigvd.gen.mpms.GameComponent;

import ch.heigvd.gen.mpms.net.server.ServantWorker;

/**
 *
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
