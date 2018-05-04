package ch.heigvd.gen.mpms.lobby;

import ch.heigvd.gen.mpms.GameComponent.Player;
import ch.heigvd.gen.mpms.net.Protocol.MinesweeperProtocol;

import java.util.ArrayList;

public class Lobby {

    private Player            admin;
    private ArrayList<Player> players;

    private boolean isOpened;

    private int slot;
    private int nbrPlayer;

    public Lobby(Player admin){
        this.admin   = admin;
        this.players = new ArrayList<Player>();
        isOpened     = false;
        slot         = 4;
        nbrPlayer    = 1;
    }

    public int openLobby(Player player){

        if(player != admin){
            player.getClient().print(MinesweeperProtocol.STATUS_450 + " " + MinesweeperProtocol.REPLY_ACTION_DENIED);
            return Integer.parseInt(MinesweeperProtocol.STATUS_450);
        }

        isOpened = true;
        return 0;
    }

    public int closeLobby(Player player){

        if(player != admin){
            player.getClient().print(MinesweeperProtocol.STATUS_450 + " " + MinesweeperProtocol.REPLY_ACTION_DENIED);
            return Integer.parseInt(MinesweeperProtocol.STATUS_450);
        }

        isOpened = false;

        return 0;
    }

    public void joinLobby(Player player){

        // Check if the lobby is opened
        if(!isOpened){

        }
    }

    private void sendAllPlayer(String answer){
        admin.getClient().print(answer);
        for(Player player : players){
            player.getClient().print(answer);
        }
    }
}
