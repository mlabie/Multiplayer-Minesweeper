package ch.heigvd.gen.mpms.game;

import ch.heigvd.gen.mpms.GameComponent.Configuration;
import ch.heigvd.gen.mpms.GameComponent.Player;

import java.util.ArrayList;

/**
 * @brief This class will implement the Minesweeper Game, with the set of function the client will need
 *         to manage it.
 *         The MineSweeper Game contains a list of players, and a board game, on which the player will play.
 *
 * @author Corentin Basler, Antonio Cusanelli, Marc Labie, Simon Jobin
 */
public class MineSweeperGame {

    private ArrayList<Player> players;
    private BoardGame         boardGame;
    private int               playerAlive;
    private int               playerDead;


    /**
     * @brief Constructor of the class.
     *
     * @param players           : The list of players.
     * @param configuration     : The configuration of the game.
     */
    public MineSweeperGame(ArrayList<Player> players, Configuration configuration){
        this.players     = players;
        this.boardGame   = new BoardGame(configuration);
        this.playerAlive = players.size();
        this.playerDead  = 0;
    }


    /**
     * @brief Get a player in the players list.
     *
     * @param playerName    : The name of the player to return
     *
     * @return The player, or null if he was not found.
     */
    public Player getPlayer(String playerName){
        for(Player player : players)
            if(player.getPlayerName().equals(playerName))
                return player;

        return null;
    }


    /**
     * @brief Get the players of the game
     *
     * @return the list of players
     */
    public ArrayList<Player> getPlayers(){
        return players;
    }


    /**
     * @brief Get the board game.
     *
     * @return the board game
     */
    public BoardGame getBoardGame() {
        return boardGame;
    }

    /**
     * Send a message to all the player actually in the game
     *
     * @param answer    : the message to send
     */
    public void sendAllPlayer(String answer){
        for(Player player : players){
            player.getClient().print(answer);
        }
    }
}
