package ch.heigvd.gen.mpms.game;

import ch.heigvd.gen.mpms.GameComponent.Configuration;
import ch.heigvd.gen.mpms.GameComponent.Player;
import ch.heigvd.gen.mpms.model.net.server.JsonObjectMapper;
import ch.heigvd.gen.mpms.model.net.Protocol.MinesweeperProtocol;
import ch.heigvd.gen.mpms.model.net.server.ServantWorker;
import com.fasterxml.jackson.core.JsonProcessingException;

import java.util.ArrayList;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @brief This class will implement the Minesweeper Game, with the set of function the client will need
 *         to manage it.
 *         The MineSweeper Game contains a list of players, and a board game, on which the player will play.
 *
 * @author Corentin Basler, Antonio Cusanelli, Marc Labie, Simon Jobin
 */
public class MineSweeperGame {

    final static Logger LOG = Logger.getLogger(ServantWorker.class.getName());

    private ArrayList<Player> players;
    private BoardGame         boardGame;
    private int               playerAlive;
    private int               playerDead;
    private boolean           gameFinished;


    /**
     * @brief Constructor of the class.
     *
     * @param players           : The list of players.
     * @param configuration     : The configuration of the game.
     */
    public MineSweeperGame(ArrayList<Player> players, Configuration configuration){
        this.players      = players;
        this.boardGame    = new BoardGame(configuration);
        this.playerAlive  = players.size();
        this.playerDead   = 0;
        this.gameFinished = false;
        LOG.log(Level.INFO, "\n" + boardGame.toString());
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
     * @brief Function called when a player wants to leave a game.
     *
     * @param player    : The name of the players who's quitting
     *
     * @return the status of the command.
     */
    public int quitGame(Player player){
        String answer;
        Player p;


        p = getPlayer(player.getPlayerName());

        if(p == null){

            return MinesweeperProtocol.STATUS_650_I;
        }

        players.remove(p);

        answer = MinesweeperProtocol.STATUS_350 + MinesweeperProtocol.REPLY_PLAYER_LEFT +
                 MinesweeperProtocol.REPLY_PARAM_DELIMITER + player.getPlayerName();

        sendAllPlayer(answer);
        LOG.log(Level.INFO, answer);

        return MinesweeperProtocol.STATUS_250_I;
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
     * @brief Sweep a square in the mine field. Send an answer to the players to
     *        notify them if the player is dead, or to send them all the squares
     *        that have been swept by the player.
     *        If the game is finished after this action, sends a message to all the player to tell them that the
     *        game is finished, with the name of the winner.
     *
     * @param x         : The x coordinate of the square
     * @param y         : The y coordinate of the square
     * @param player    : The player that wants to sweep the mine.
     *
     * @return the Status of the command
     */
    public synchronized int sweep(int x, int y, Player player){
        Vector<Square> sweptSquare;
        boolean        isAlive;
        String         answer;
        Player         winner;

        // Check that the square is in the field.
        if(x < 0 || x >= boardGame.getConfig().getWidth() || y < 0 || y >= boardGame.getConfig().getHeight()){
            answer = MinesweeperProtocol.STATUS_650 + MinesweeperProtocol.DELIMITER + MinesweeperProtocol.REPLY_SQUARE_NOT_FOUND;
            player.getClient().print(answer);
            return MinesweeperProtocol.STATUS_650_I;

            // Check that the game is not finished
        }else if(gameFinished){
            answer = MinesweeperProtocol.STATUS_650 + MinesweeperProtocol.DELIMITER + MinesweeperProtocol.REPLY_GAME_IS_FINISHED;
            player.getClient().print(answer);
            return MinesweeperProtocol.STATUS_650_I;
        }

        sweptSquare = new Vector<Square>();
        isAlive     = boardGame.sweep(x,y,player,sweptSquare);

        if(!isAlive){

            try {
                // Show the player who died the location of the mines.
                answer = MinesweeperProtocol.STATUS_350 + MinesweeperProtocol.DELIMITER + MinesweeperProtocol.REPLY_MINES_ARE +
                         MinesweeperProtocol.REPLY_PARAM_DELIMITER + JsonObjectMapper.toJson(sweptSquare);

                player.getClient().print(answer);
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }

            answer = MinesweeperProtocol.STATUS_350 + MinesweeperProtocol.DELIMITER + MinesweeperProtocol.REPLY_PLAYER_DIED +
                     MinesweeperProtocol.REPLY_PARAM_DELIMITER + player.getPlayerName();

            player.kill();
            playerDead++;
            playerAlive--;

            sendAllPlayer(answer);

        }else if(sweptSquare.isEmpty()){
            answer = MinesweeperProtocol.STATUS_650 + MinesweeperProtocol.DELIMITER + MinesweeperProtocol.REPLY_SQUARE_ALREADY_SWEPT;
            player.getClient().print(answer);
            return MinesweeperProtocol.STATUS_650_I;
        }else{
            answer = MinesweeperProtocol.STATUS_350 + MinesweeperProtocol.DELIMITER + MinesweeperProtocol.REPLY_SQUARE_SWEPT +
                     MinesweeperProtocol.REPLY_PARAM_DELIMITER;
            try {
                answer += JsonObjectMapper.toJson(sweptSquare);
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }

            sendAllPlayer(answer);

            // Send the new players score.
            answer = MinesweeperProtocol.STATUS_350 + MinesweeperProtocol.DELIMITER + MinesweeperProtocol.REPLY_PLAYER_SCORE +
                     MinesweeperProtocol.REPLY_PARAM_DELIMITER + player.getPlayerName() + MinesweeperProtocol.DELIMITER +
                     player.getScore();

            sendAllPlayer(answer);
        }



        gameFinished = isGameFinished();

        LOG.log(Level.INFO, "\n" + boardGame.toString());


        // if the game is finisehd, send to all the player who's the winner.
        if(gameFinished){
            winner = getBestScore();

            answer = MinesweeperProtocol.STATUS_350 + MinesweeperProtocol.DELIMITER +
                     MinesweeperProtocol.REPLY_GAME_FINISHED + MinesweeperProtocol.REPLY_PARAM_DELIMITER;

            if(winner == null){
                answer += "-";
            }else {
                answer += winner.getPlayerName();
            }

            sendAllPlayer(answer);
        }

        return MinesweeperProtocol.STATUS_250_I;
    }


    /**
     * @brief Function that determines if a game is finished or not. A game is finished when all
     *        all the players are dead, or that all the squares without mines have been swept.
     *
     * @return true if the game is finished, false else.
     */
    private boolean isGameFinished(){
        if (playerAlive == 0){
            return true;
        }

        if((boardGame.getSquareAmount() - boardGame.getMineAmount() - boardGame.getSquareSweptAmount()) == 0){
            return true;
        }

        return false;
    }


    /**
     * @brief Return the player of the game with the actual best score.
     *
     * @return  The player with the best score.
     */
    private Player getBestScore(){

        Player winner;
        int    bestScore;


        bestScore = 0;
        winner    = null;

        for(Player player : players){
            if (player.getScore() > bestScore){
                bestScore = player.getScore();
                winner = player;
            }
        }

        return winner;
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
