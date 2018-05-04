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

    String getPlayerName(){
        return playerName;
    }

    void setPlayerName(String playerName){
        this.playerName = playerName;
    }

    int getScore(){
        return score;
    }

    void setScore(int score){
        this.score = score;
    }

    ServantWorker getClient(){
        return client;
    }


}
