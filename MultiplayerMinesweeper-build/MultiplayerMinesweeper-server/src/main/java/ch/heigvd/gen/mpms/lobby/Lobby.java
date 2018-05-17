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
    private final Object lobbyLocker        = new Object();

    public  static ArrayList<Lobby> lobbies = new ArrayList<>();


    private String name;

    private Player            admin;
    private ArrayList<Player> players;

    private boolean isOpened;

    private int nbrPlayer;
    private Configuration config;


    /**
     * Constructor of the class. It needs at list a lobby name and a player which will be the admin of the game.
     * @param name
     * @param admin
     */
    public Lobby(String name, Player admin){
        this.name    = name;
        this.admin   = admin;
        this.players = new ArrayList<Player>();
        isOpened     = false;
        nbrPlayer    = 1;
        config       = new Configuration("Default");
    }


    /**
     * Get the name of the lobby
     * @return the name of the lobby
     */
    public String getName(){
        return name;
    }

    /**
     * Get the locker of the lobby
     * @return
     */
    public Object getLobbyLocker(){
        return lobbyLocker;
    }

    /**
     * Check if the lobby is closed of opened.
     * @return
     */
    public boolean isOpened(){
        return isOpened;
    }



    /**
     * Open the lobby if the player asking for is the admin.
     * @param player    : the player asking to open the lobby
     * @return  The status of the command : 250 if it has been opened, another status if it was not accepted.
     */
    public int openLobby(Player player){
        synchronized (lobbyLocker){
            if(player != admin){
                player.getClient().print(MinesweeperProtocol.STATUS_450 + " " + MinesweeperProtocol.REPLY_ACTION_DENIED);
                return MinesweeperProtocol.STATUS_450_I;
            }

            isOpened = true;

            sendAllPlayer(MinesweeperProtocol.STATUS_350 + " " + MinesweeperProtocol.REPLY_LOBBY_OPENED);
            return MinesweeperProtocol.STATUS_250_I;
        }
    }


    /**
     * Close the lobby if the player asking for is the admin.
     * @param player    : the player asking to close the lobby
     * @return  The status of the command : 250 if it has been closed, another status if it was not accepted.
     */
    public int closeLobby(Player player){
        synchronized (lobbyLocker){
            if(player != admin){
                player.getClient().print(MinesweeperProtocol.STATUS_450 + " " + MinesweeperProtocol.REPLY_ACTION_DENIED);
                return MinesweeperProtocol.STATUS_450_I;
            }

            isOpened = false;
            sendAllPlayer(MinesweeperProtocol.STATUS_350 + " " + MinesweeperProtocol.REPLY_LOBBY_CLOSED);
            return MinesweeperProtocol.STATUS_250_I;
        }

    }

    /**
     * Join the actual lobby if is not closed, that there is enough slot left,
     * and that the player's name is not already used.
     *
     * @param player    The player that want to join
     *
     * @return the status of the command.
     */
    public synchronized int joinLobby(Player player){
        synchronized (lobbyLocker){
            // Check if the lobby is opened
            if(!isOpened){
                player.getClient().print(MinesweeperProtocol.STATUS_650 + " " +
                                         MinesweeperProtocol.REPLY_LOBBY_CLOSED);
                return MinesweeperProtocol.STATUS_650_I;
            }

            // check if the player name is already used in the lobby
            if(hasPlayer(player.getPlayerName()) != null){
                player.getClient().print(MinesweeperProtocol.STATUS_650 + " " +
                                         MinesweeperProtocol.REPLY_PLAYER_NAME_NOT_AVAIBALE);
                return MinesweeperProtocol.STATUS_650_I;
            }

            //Check if the lobby is full
            if(nbrPlayer >= config.getNbrSlot()){
                player.getClient().print(MinesweeperProtocol.STATUS_650 + " " + MinesweeperProtocol.REPLY_LOBBY_FULL);
                return MinesweeperProtocol.STATUS_650_I;
            }


            // We send the current lobby status to the joining player

            sendActualConfig(player);

            player.getClient().print(MinesweeperProtocol.STATUS_350 + " " +
                                     MinesweeperProtocol.REPLY_LOBBY_JOINED_BY + " " + admin.getPlayerName());

            for(Player p : players){
                player.getClient().print(MinesweeperProtocol.STATUS_350 + " " +
                                         MinesweeperProtocol.REPLY_LOBBY_JOINED_BY + " " + p.getPlayerName());
            }



            // The player can join the lobby.
            players.add(player);
            nbrPlayer++;


            // Inform all the player that a player has joined the lobby.
            sendAllPlayer(MinesweeperProtocol.STATUS_350 + " " + MinesweeperProtocol.REPLY_LOBBY_JOINED_BY + " " +
                                 player.getPlayerName());


            return MinesweeperProtocol.STATUS_250_I;
        }
    }

    /**
     * the player will quit the lobby. If it's the admin, all the players will be expelled, and the lobby removed
     * from the lobbys list.
     * @param player    the player that wants to quit.
     * @return the status of the command.
     */
    public int quitLobby(Player player){
        // if the player asking to quit is the admin, we are going to
        // expel all the player and delete the lobby.
        if(player == admin){

            // expelling all the player before delating the lobby.
            while(!players.isEmpty()){
                expelLobby(player, players.get(0).getPlayerName());
            }

            deleteLobby(this);
        }
        else if(hasPlayer(player.getPlayerName()) != null){
            String name = player.getPlayerName();
            players.remove(player);
            nbrPlayer--;

            sendAllPlayer(MinesweeperProtocol.STATUS_350 + " " +
                                 MinesweeperProtocol.REPLY_LOBBY_LEFT_BY + " " + name);
        }

        return MinesweeperProtocol.STATUS_250_I;
    }

    /**
     * Expel a player from the lobby if the player is in the lobby, that it is not the admin, and that the player
     * asking for expelling is the admin.
     *
     * @param player    The player asking for the expelling, it has to be the admin
     * @param name      The name of the player that has to be expelled
     *
     * @return The status of the command
     */
    public int expelLobby(Player player, String name){
        // Check if the player is the admin
        if(player != admin){
            player.getClient().print(MinesweeperProtocol.STATUS_450 + " " + MinesweeperProtocol.REPLY_ACTION_DENIED);
            return MinesweeperProtocol.STATUS_450_I;
        }

        Player expelled = hasPlayer(name);

        if(expelled == null || expelled == admin){
            player.getClient().print(MinesweeperProtocol.STATUS_650 + " " + MinesweeperProtocol.REPLY_PLAYER_NOT_FOUND);
            return MinesweeperProtocol.STATUS_650_I;
        }

        // Critical section. We forbid the expeled ServantWorked to execute any command during this laps.
        synchronized (expelled.getClient().lock) {
            // Remove the player from the lobby.
            players.remove(expelled);
            nbrPlayer--;

            // Set lobby of the player
            expelled.getClient().print(MinesweeperProtocol.STATUS_350 + " " +
                    MinesweeperProtocol.REPLY_YOU_HAVE_BEEN_EXPELLED);

            expelled.getClient().setLobby(null);
            expelled.getClient().setPlayer(null);
        }


        sendAllPlayer(MinesweeperProtocol.STATUS_350 + " " +
                MinesweeperProtocol.REPLY_LOBBY_LEFT_BY + " " + name);

        return MinesweeperProtocol.STATUS_250_I;
    }




    /**
     * Check if a given player name is already used in the actual lobby.
     * @param name  : The player name
     * @return returns the player if found, null else.
     */
    private Player hasPlayer(String name){
        if(admin.getPlayerName().equals(name))
            return admin;

        for(Player player : players)
            if(player.getPlayerName().equals(name))
                return player;

        return null;
    }


    /***************************                Configuration                ***************************/


    /**
     * Sets the player amount allowed for the game. If there are more player in the players list than
     * there is place after the modification, the last players that will have join the lobby will
     * be expelled.
     *
     * @param player    : The player that wants to set the players amount (has to be the admin)
     * @param amount    : The new amount of player. Configuration.MIN_SLOT min, Configuration.MIN_SLOT max.
     *
     * @return The status of the command
     */
    public int setPlayerAmount(Player player, int amount){

        // Check if the player is the admin
        if(player != admin){
            player.getClient().print(MinesweeperProtocol.STATUS_450 + " " + MinesweeperProtocol.REPLY_ACTION_DENIED);
            return MinesweeperProtocol.STATUS_450_I;
        }

        // Check if the player amount respect the limited amount set
        if(amount < Configuration.MIN_SLOT || amount > Configuration.MAX_SLOT){
            player.getClient().print(MinesweeperProtocol.STATUS_650 + " " + MinesweeperProtocol.REPLY_PLAYER_AMOUNT_NOT_ALLOWED);
            return MinesweeperProtocol.STATUS_650_I;
        }

        // Set the new player amount
        config.setNbrSlot(amount);
        sendAllPlayer(MinesweeperProtocol.STATUS_350 + " " +
                MinesweeperProtocol.REPLY_PLAYER_AMOUNT_IS + " " + amount);


        // We expel all the player that are in excess
        while (amount < nbrPlayer){
            expelLobby(player, players.get(players.size()-1).getPlayerName());
        }

        return MinesweeperProtocol.STATUS_250_I;
    }

    /**
     * Change the size of the mine field.
     *
     * @param player    : The player that wants to set the field size (has to be the admin)
     * @param width     : The new widht of the field. Configuration.MIN_WIDTH min, Configuration.MAX_WIDTH max.
     * @param height    : The new height of the field. Configuration.MIN_HEIGHT min, Configuration.MAX_HEIGHT max.
     *
     * @return The status of the command
     */
    public int setFieldSize(Player player, int width, int height){

        // Check if the player is the admin
        if(player != admin){
            player.getClient().print(MinesweeperProtocol.STATUS_450 + " " + MinesweeperProtocol.REPLY_ACTION_DENIED);
            return MinesweeperProtocol.STATUS_450_I;
        }

        // Check if the size respect the limited size set
        if(width < Configuration.MIN_WIDTH || width > Configuration.MAX_WIDTH ||
                height < Configuration.MIN_HEIGHT || height > Configuration.MAX_HEIGHT){
            player.getClient().print(MinesweeperProtocol.STATUS_650 + " " + MinesweeperProtocol.REPLY_SIZE_NOT_ALLOWED);
            return MinesweeperProtocol.STATUS_650_I;
        }


        // set the new field size
        config.setWidth(width);
        config.setHeight(height);

        sendAllPlayer(MinesweeperProtocol.STATUS_350 + " " +
                MinesweeperProtocol.REPLY_SIZE_IS + " " + width + "X" + height);


        return MinesweeperProtocol.STATUS_250_I;
    }


    /**
     * Change the score counting mode.
     *
     * @param player      : The player that wants to set the score mode (has to be the admin)
     * @param scoreMode   :
     *
     * @return The status of the command
     */
    public int setScoreMode(Player player, String scoreMode){

        // Check if the player is the admin
        if(player != admin){
            player.getClient().print(MinesweeperProtocol.STATUS_450 + " " + MinesweeperProtocol.REPLY_ACTION_DENIED);
            return MinesweeperProtocol.STATUS_450_I;
        }


        // Check which score to set
        if(scoreMode.equals(Configuration.ScoreMode.STANDARD.toString())){
            config.setScore(Configuration.ScoreMode.STANDARD);
        }
        else if(scoreMode.equals(Configuration.ScoreMode.EXPLORER.toString())){
            config.setScore(Configuration.ScoreMode.EXPLORER);
        }
        else if(scoreMode.equals(Configuration.ScoreMode.HARDSWEEPER.toString())){
            config.setScore(Configuration.ScoreMode.HARDSWEEPER);
        }else {
            // If the score mode was not found...
            player.getClient().print(MinesweeperProtocol.STATUS_650 + " " + MinesweeperProtocol.REPLY_MODE_NOT_FOUND);
            return MinesweeperProtocol.STATUS_650_I;
        }

        sendAllPlayer(MinesweeperProtocol.STATUS_350 + " " +
                MinesweeperProtocol.REPLY_SCORE_MODE_IS + " " + scoreMode);


        return MinesweeperProtocol.STATUS_250_I;
    }


    /**
     * Change the proportion of mine for the game.
     *
     * @param player      : The player that wants to set the proportion of mines (has to be the admin)
     * @param proportion  : The new proportion of mines of the field.
     *                      Configuration.PROPORTION_MIN min, Configuration.PROPORTION_MAX max.
     *
     * @return The status of the command
     */
    public int setMineProportion(Player player, int proportion){

        // Check if the player is the admin
        if(player != admin){
            player.getClient().print(MinesweeperProtocol.STATUS_450 + " " + MinesweeperProtocol.REPLY_ACTION_DENIED);
            return MinesweeperProtocol.STATUS_450_I;
        }

        // Check if the size respect the limited size set
        if(proportion < Configuration.PROPORTION_MIN || proportion > Configuration.PROPORTION_MAX){
            player.getClient().print(MinesweeperProtocol.STATUS_650 + " " + MinesweeperProtocol.REPLY_MINE_PROPORTION_NOT_ALLOWED);
            return MinesweeperProtocol.STATUS_650_I;
        }


        // set the new field size
        config.setMineProportion(proportion);

        sendAllPlayer(MinesweeperProtocol.STATUS_350 + " " +
                MinesweeperProtocol.REPLY_MINE_PROPORTION_IS + " " + proportion);


        return MinesweeperProtocol.STATUS_250_I;
    }


    /**
     * Enables the bonus et malus for the game.
     *
     * @param player    : the player that wants to enable the bonus and malus (has to be the admin).
     *
     * @return The status of the command
     */
    public int enableBonusMalus(Player player){

        // Check if the player is the admin
        if(player != admin){
            player.getClient().print(MinesweeperProtocol.STATUS_450 + " " + MinesweeperProtocol.REPLY_ACTION_DENIED);
            return MinesweeperProtocol.STATUS_450_I;
        }

        config.setBonus(true);
        config.setMalus(true);

        sendAllPlayer(MinesweeperProtocol.STATUS_350 + " " +
                MinesweeperProtocol.REPLY_BONUS_MALUS_ENABLED);


        return MinesweeperProtocol.STATUS_250_I;
    }


    /**
     * disable the bonus et malus for the game.
     *
     * @param player    : the player that wants to disable the bonus and malus (has to be the admin).
     *
     * @return The status of the command
     */
    public int disableBonusMalus(Player player){

        // Check if the player is the admin
        if(player != admin){
            player.getClient().print(MinesweeperProtocol.STATUS_450 + " " + MinesweeperProtocol.REPLY_ACTION_DENIED);
            return MinesweeperProtocol.STATUS_450_I;
        }

        config.setBonus(false);
        config.setMalus(false);

        sendAllPlayer(MinesweeperProtocol.STATUS_350 + " " +
                MinesweeperProtocol.REPLY_BONUS_MALUS_DISABLED);


        return MinesweeperProtocol.STATUS_250_I;
    }


    /**
     * Send the actual configuration to a player.
     *
     * @param player the player to whom the actual configuration will be sent.
     */
    public void sendActualConfig(Player player){

        if(player == null)
            return;

        player.getClient().print(MinesweeperProtocol.STATUS_350 + " " +
                MinesweeperProtocol.REPLY_SCORE_MODE_IS + " " + config.getScore().toString());

        player.getClient().print(MinesweeperProtocol.STATUS_350 + " " +
                MinesweeperProtocol.REPLY_PLAYER_AMOUNT_IS + " " + config.getNbrSlot());

        player.getClient().print(MinesweeperProtocol.STATUS_350 + " " +
                MinesweeperProtocol.REPLY_MINE_PROPORTION_IS + " " + config.getMineProportion());
        if(config.isMalus()){
            player.getClient().print(MinesweeperProtocol.STATUS_350 + " " +
                    MinesweeperProtocol.REPLY_BONUS_MALUS_ENABLED);
        }else {
            player.getClient().print(MinesweeperProtocol.STATUS_350 + " " +
                    MinesweeperProtocol.REPLY_BONUS_MALUS_DISABLED);
        }

        player.getClient().print(MinesweeperProtocol.STATUS_350 + " " +  MinesweeperProtocol.REPLY_SIZE_IS + " " +
                config.getWidth() + "X" + config.getHeight());

    }




    /***************************               Méthodes Privées               ***************************/


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



    /***************************               Méthodes Statics               ***************************/


    /**
     * Find a lobby int the lobbies list
     *
     * @param name  The name of the lobby
     * @return the lobby if it was found, or null if the lobby doesn't exist.
     */
    public static synchronized Lobby findLobby(String name){
        Lobby lobby = null;
        synchronized (lock){
            for(int i = 0; i < lobbies.size(); i++){
                if(lobbies.get(i).getName().equals(name)){
                    lobby = lobbies.get(i);
                    break;
                }
            }
            return lobby;
        }
    }


    /**
     * Adds a new lobby to the lobby list, as long as it doesn't contain a lobby that already has the same name.
     * @param lobby     The lobby to add
     * @return 0 if it has been added, or -1 if it already exist in the lobbies list.
     */
    public static int addLobby(Lobby lobby){
        synchronized (lock){
            if(findLobby(lobby.getName()) == null){
                lobbies.add(lobby);
                return 0;
            }else {
                //the lobby already exists.
                return -1;
            }
        }
    }

    /**
     * Remove a lobby of the lobbies list, if it exists.
     * @param lobby the lobby to delete
     * @return the position of the lobby that has been erased, or -1 if it doesn't exist.
     */
    public static int deleteLobby(Lobby lobby){
        synchronized (lock){
            if(lobbies.remove(lobby))
                return 0;
            else
                return -1;
        }
    }
}
