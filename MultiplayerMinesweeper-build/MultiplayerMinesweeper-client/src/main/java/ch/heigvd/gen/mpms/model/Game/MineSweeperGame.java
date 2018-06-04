package ch.heigvd.gen.mpms.model.Game;

import ch.heigvd.gen.mpms.model.GameComponent.Player;

import java.util.ArrayList;

public class MineSweeperGame {


    private ArrayList<Player> players;

    public MineSweeperGame(ArrayList<Player> players){
        this.players = players;
    }


    public ArrayList<Player> getPlayers() {
        return players;
    }

    public void removePlayer(Player player){
        players.remove(player);
    }

    public Player getPlayer(String playerName){
        for (Player player : players)
            if(player.getPlayerName().equals(playerName))
                return player;

        return null;
    }
}
