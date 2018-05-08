package ch.heigvd.gen.mpms.lobby;

import ch.heigvd.gen.mpms.GameComponent.Player;
import ch.heigvd.gen.mpms.GameComponent.Configuration;
import ch.heigvd.gen.mpms.net.Protocol.MinesweeperProtocol;

import java.util.ArrayList;

/**
 * @brief: This class will implement the lobbys created by the players, with the set of function the client will need
 *         to manage it.
 *         The lobby contains one administrator, a list of player, and different information like the number
 *         the number of player actually in the lobby, the state of the lobby (open/close).
 *         It contains aswell a configuration, that will give him information like the number of slots.
 *         Most importantly, it has a name, that will be used to identify it in the lobby list.
 *
 * @author Corentin Basler, Antonio Cusanelli, Marc Labie, Simon Jobin
 */
public class Lobby {

    private static final Object lock        = new Object();

    public  static ArrayList<Lobby> lobbies = new ArrayList<>();


    private String name;

    private Player            admin;
    private ArrayList<Player> players;

    private boolean isOpened;

    private int nbrPlayer;
    private Configuration config;

    public Lobby(String name, Player admin){
        this.name    = name;
        this.admin   = admin;
        this.players = new ArrayList<Player>();
        isOpened     = false;
        nbrPlayer    = 1;
        config       = new Configuration();
    }

    public int openLobby(Player player){

        if(player != admin){
            player.getClient().print(MinesweeperProtocol.STATUS_450 + " " + MinesweeperProtocol.REPLY_ACTION_DENIED);
            return Integer.parseInt(MinesweeperProtocol.STATUS_450);
        }

        isOpened = true;

        sendAllPlayer(MinesweeperProtocol.STATUS_350 + " " + MinesweeperProtocol.REPLY_LOBBY_OPENED);
        return Integer.parseInt(MinesweeperProtocol.STATUS_350);
    }

    public int closeLobby(Player player){

        if(player != admin){
            player.getClient().print(MinesweeperProtocol.STATUS_450 + " " + MinesweeperProtocol.REPLY_ACTION_DENIED);
            return Integer.parseInt(MinesweeperProtocol.STATUS_450);
        }

        isOpened = false;
        sendAllPlayer(MinesweeperProtocol.STATUS_350 + " " + MinesweeperProtocol.REPLY_LOBBY_CLOSED);
        return Integer.parseInt(MinesweeperProtocol.STATUS_350);
    }

    public synchronized int joinLobby(Player player){

        // Check if the lobby is opened
        if(!isOpened){

        }

        return 0;
    }


    public String getName(){
        return name;
    }

    public boolean isOpened(){
        return isOpened;
    }



    /**
     * Find a lobby int the lobbies list
     *
     * @param name  The name of the lobby
     * @return the position of the lobby, going from 0 to lobbies.size() - 1, or -1 if the lobby doesn't exist.
     */
    public static synchronized int findLobby(String name){
        int pos = -1;
        synchronized (lock){
            for(int i = 0; i < lobbies.size(); i++){
                if(lobbies.get(i).getName().equals(name)){
                    pos = i;
                    break;
                }
            }
            return pos;
        }
    }


    /**
     * Adds a new lobby to the lobby list, as long as it doesn't contain a lobby that already has the same name.
     * @param lobby     The lobby to add
     * @return the lobby position in the lobbies array, or -1 if it already exist in the lobbies list.
     */
    public static int addLobby(Lobby lobby){
        synchronized (lock){
            int pos = findLobby(lobby.getName());
            if(pos == -1){
                lobbies.add(lobby);
                return lobbies.size() - 1;
            }else {
                //the lobby already exists.
                return -1;
            }
        }
    }

    /**
     * Remove a lobby of the lobbies list, if it exists.
     * @param name the lobby to delete
     * @return the position of the lobby that has been erased, or -1 if it doesn't exist.
     */
    public static int deleteLobby(String name){
        synchronized (lock){
            int pos = findLobby(name);
            if(pos == -1){
                //the lobby doesn't exist.
                return -1;
            }else {
                lobbies.remove(pos);
                return pos;
            }
        }
    }

    /**
     * Send a message to all the player actually in the lobby.
     * @param answer the message to send
     */
    private void sendAllPlayer(String answer){
        admin.getClient().print(answer);
        for(Player player : players){
            player.getClient().print(answer);
        }
    }
}
