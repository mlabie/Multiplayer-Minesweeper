package ch.heigvd.gen.mpms.model.Lobby;


import ch.heigvd.gen.mpms.model.GameComponent.Configuration;
import ch.heigvd.gen.mpms.model.GameComponent.Player;
import java.util.ArrayList;

public class Lobby {

    private String name;

    private ArrayList<Player> players;

    private Configuration config;

    public Lobby(String name){
        this.name      = name;
        this.config    = new Configuration("Game configuration");
        this.players   = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public Configuration getConfig() {
        return config;
    }

    public ArrayList<Player> getPlayers() {
        return players;
    }

    public void addPlayer(Player player){
        players.add(player);
    }

    public void removePlayer(String player){
        players.remove(this.getPlayer(player));
        Player.playerNumber = 0;
        for(Player p : players)
            p.setNumber(++Player.playerNumber);

    }

    private Player getPlayer(String playerName){
        for (Player player : players)
            if(player.getPlayerName().equals(playerName))
                return player;

        return null;
    }


}
