package ch.heigvd.gen.mpms.model.net.server;

import ch.heigvd.gen.mpms.GameComponent.Configuration;
import ch.heigvd.gen.mpms.GameComponent.Player;
import ch.heigvd.gen.mpms.game.MineSweeperGame;
import ch.heigvd.gen.mpms.lobby.Lobby;
import ch.heigvd.gen.mpms.model.net.Protocol.MinesweeperProtocol;

import java.io.*;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * This class implements a Servant that will work only with one client. It will manage the command
 * the client sents to him. It will work as a thread.
 *
 * @source cf https://wasadigi.gitbooks.io/res-heigvd-network-programming-application-protoc/content/tcp_programming.html
 * @author Olivier Liechti
 *
 * @author Corentin Basler, Antonio Cusanelli, Marc Labie, Simon Jobin
 */
public class ServantWorker implements Runnable{

    final static Logger LOG = Logger.getLogger(ServantWorker.class.getName());


    private Socket clientSocket;

    private BufferedReader  br = null;
    private PrintWriter     pw = null;


    private Player          player          = null;
    private Lobby           lobby           = null;
    private MineSweeperGame mineSweeperGame = null;



    /**
     * Used for critical section of the thread.
     */
    public final Object lock = new Object();


    /**
     * Constructor of the class. It must at least has a Socket.
     * @param clientSocket
     */
    public ServantWorker(Socket clientSocket) {

        try {
            this.clientSocket = clientSocket;
            br = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            pw = new PrintWriter(new OutputStreamWriter(clientSocket.getOutputStream()));
        } catch (IOException e) {
            LOG.log(Level.SEVERE, e.getMessage(), e);
        }
    }


    /**
     * Getters and setters
     */
    public Lobby getLobby(){
        return lobby;
    }
    public void setLobby(Lobby lobby){
        this.lobby = lobby;
    }
    public Player getPlayer(){
        return player;
    }
    public void setPlayer(Player player){
        this.player = player;
    }


    /**
     * Run function of the thread. Listen continiusly to the client, and manage the command
     * it sends to him.
     */
    @Override
    public void run() {

        String request;         // The command send by the client
        String answer;          // The answer from the server
        boolean shouldRun;
        int manageRes;


        shouldRun = true;
        answer    = MinesweeperProtocol.STATUS_220 + MinesweeperProtocol.DELIMITER + MinesweeperProtocol.WELCOME;

        LOG.info(answer);
        print(answer);

        try {

            LOG.info("Reading until client sends " + MinesweeperProtocol.CMD_DISCONNECT + " or closes the connection...");

            while ((shouldRun) && (request = br.readLine()) != null) {

                request = request.toUpperCase();

                LOG.info(request);

                synchronized (this.lock){
                    manageRes = manageCommand(request);
                }


                // Depending on the result of the manageCommand, the connection will be closed or not
                // (-1 : close connection, 0 : continue)
                if(manageRes == -1){
                    shouldRun = false;
                }

            }

            LOG.info("Cleaning up resources...");

            if(lobby != null){
                if(Lobby.findLobby(lobby.getName()) != null){
                    lobby.quitLobby(player);
                }
                lobby = null;
            }

            br.close();
            pw.close();
            clientSocket.close();

        } catch (IOException e) {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException ex) {
                    LOG.log(Level.SEVERE, ex.getMessage(), ex);
                }
            }
            if (pw != null) {
                pw.close();
            }
            if (clientSocket != null) {
                try {
                    clientSocket.close();
                } catch (IOException ex) {
                    LOG.log(Level.SEVERE, ex.getMessage(), ex);
                }
            }
            LOG.log(Level.SEVERE, e.getMessage(), e);
        }
    }


    /**
     * Manage the command send by the player
     * @param request   the line sent by the player
     * @return  -1 if the player wants to end the connection, 0 if he wants to continue it.
     */
    private int manageCommand(String request){

        String   answer;
        String   command;

        String[] parameters;
        int      parametersAmount;

        boolean commandExist;
        int     iter_command;

        int status;

        commandExist = false;



        // Check if the command exists
        for(iter_command = 0; iter_command < MinesweeperProtocol.SUPPORTED_COMMANDS.length; iter_command++)
            if ((commandExist = request.startsWith(MinesweeperProtocol.SUPPORTED_COMMANDS[iter_command] + MinesweeperProtocol.DELIMITER)))
                break;



        if(commandExist){
            command = MinesweeperProtocol.SUPPORTED_COMMANDS[iter_command];
        } else {
            command = request;
        }


        // Get the parameters of the command
        parameters       = parameter(request.replace(command, ""),
                                     MinesweeperProtocol.DELIMITER);
        parametersAmount = parameters.length;


        switch (command){


            // - - - - - - - - - - - - - - -         CLOSE LOBBY         - - - - - - - - - - - - - - - //
            case MinesweeperProtocol.CMD_CLOSE_LOBBY:

                if(lobby == null){
                    answer = MinesweeperProtocol.STATUS_550 + " " + MinesweeperProtocol.REPLY_NO_LOBBY_CREATED;
                }
                else {
                    synchronized (lobby.getLobbyLocker()){
                        status = lobby.closeLobby(player);

                        if(status == MinesweeperProtocol.STATUS_250_I){
                            answer = MinesweeperProtocol.STATUS_250 + " " + MinesweeperProtocol.REPLY_OK;
                        }else {
                            break;
                        }
                    }
                }

                this.print(answer);
                LOG.log(Level.INFO, answer);
                break;




            // - - - - - - - - - - - - - - -         CREATE LOBBY         - - - - - - - - - - - - - - - //
            case MinesweeperProtocol.CMD_CREATE_LOBBY:

                // check if the number of arguments is correct
                if(parametersAmount > MinesweeperProtocol.NBR_PARAM_CREATE_LOBBY){
                    answer = MinesweeperProtocol.STATUS_550 + " " + MinesweeperProtocol.REPLY_TOO_MANY_ARGUMENTS;
                }
                else if(parametersAmount < MinesweeperProtocol.NBR_PARAM_CREATE_LOBBY){
                    answer = MinesweeperProtocol.STATUS_550 + " " + MinesweeperProtocol.REPLY_NOT_ENOUGH_ARGUMENTS;
                }
                else if(lobby != null){
                    answer = MinesweeperProtocol.STATUS_550 + " " + MinesweeperProtocol.REPLY_ALREADY_IN_A_LOBBY;
                }
                else{


                    player = new Player(this, parameters[1]);
                    lobby  = new Lobby(parameters[0], player);

                    if(Lobby.addLobby(lobby) == -1){
                        answer = MinesweeperProtocol.STATUS_650 + " " + MinesweeperProtocol.REPLY_LOBBY_NAME_NOT_AVAIABLE;
                        player = null;
                        lobby  = null;
                    }else {
                        // We send the current lobby status to the joining player

                        // Send if the lobby is opened or closed
                        if(lobby.isOpened()){
                            player.getClient().print(MinesweeperProtocol.STATUS_350 + " " + MinesweeperProtocol.REPLY_LOBBY_OPENED);
                        }else {
                            player.getClient().print(MinesweeperProtocol.STATUS_350 + " " + MinesweeperProtocol.REPLY_LOBBY_CLOSED);
                        }

                        // Send the actual configuration of the lobby.
                        lobby.sendActualConfig(player);

                        // send the actual players
                        lobby.sendActualPlayers(player);

                        answer = MinesweeperProtocol.STATUS_250 + " " + MinesweeperProtocol.REPLY_LOBBY_CREATED;
                    }
                }

                this.print(answer);
                LOG.log(Level.INFO, answer);
                break;




            // - - - - - - - - - - - - - - -      DISABLE BONUS MALUS      - - - - - - - - - - - - - - - //
            case MinesweeperProtocol.CMD_DISABLE_BONUS_MALUS:
                // check if the player has a lobby
                if(lobby == null){
                    answer = MinesweeperProtocol.STATUS_550 + " " + MinesweeperProtocol.REPLY_NO_LOBBY_JOINED;
                }
                else{
                    synchronized (lobby.getLobbyLocker()){
                        if(lobby.disableBonusMalus(player) == MinesweeperProtocol.STATUS_250_I){
                            answer = MinesweeperProtocol.STATUS_250 + " " + MinesweeperProtocol.REPLY_OK;
                        }else{
                            break;
                        }
                    }
                }

                this.print(answer);
                LOG.log(Level.INFO, answer);
                break;



            // - - - - - - - - - - - - - - -          DISCONNECT          - - - - - - - - - - - - - - - //
            case MinesweeperProtocol.CMD_DISCONNECT:
                answer = MinesweeperProtocol.STATUS_220 + MinesweeperProtocol.DELIMITER + MinesweeperProtocol.GOODBYE;
                this.print(answer);
                LOG.log(Level.INFO, answer);
                return -1;



            // - - - - - - - - - - - - - - -      ENABLE BONUS MALUS      - - - - - - - - - - - - - - - //
            case MinesweeperProtocol.CMD_ENABLE_BONUS_MALUS:
                // check if the player has a lobby
                if(lobby == null){
                    answer = MinesweeperProtocol.STATUS_550 + " " + MinesweeperProtocol.REPLY_NO_LOBBY_JOINED;
                }
                else{
                    synchronized (lobby.getLobbyLocker()){
                        if(lobby.enableBonusMalus(player) == MinesweeperProtocol.STATUS_250_I){
                            answer = MinesweeperProtocol.STATUS_250 + " " + MinesweeperProtocol.REPLY_OK;
                        }else{
                            break;
                        }
                    }
                }

                this.print(answer);
                LOG.log(Level.INFO, answer);
                break;


            // - - - - - - - - - - - - - - -         EXPEL LOBBY         - - - - - - - - - - - - - - - //
            case MinesweeperProtocol.CMD_EXPEL_LOBBY:

                // Check the number of arguments
                if(parametersAmount > MinesweeperProtocol.NBR_PARAM_EXPEL_LOBBY){
                    answer = MinesweeperProtocol.STATUS_550 + " " + MinesweeperProtocol.REPLY_TOO_MANY_ARGUMENTS;
                }
                else if(parametersAmount < MinesweeperProtocol.NBR_PARAM_EXPEL_LOBBY){
                    answer = MinesweeperProtocol.STATUS_550 + " " + MinesweeperProtocol.REPLY_NOT_ENOUGH_ARGUMENTS;
                }
                //Check if a lobby has been joined
                else if(lobby == null){
                    answer = MinesweeperProtocol.STATUS_550 + " " + MinesweeperProtocol.REPLY_NO_LOBBY_JOINED;
                }
                else {
                    synchronized (lobby.getLobbyLocker()){
                        if(lobby.expelLobby(player, parameters[0]) != MinesweeperProtocol.STATUS_250_I){
                            break;
                        }
                        else {
                            answer = MinesweeperProtocol.STATUS_250 + " " + MinesweeperProtocol.REPLY_OK;
                        }
                    }
                }

                this.print(answer);
                LOG.log(Level.INFO, answer);
                break;



            // - - - - - - - - - - - - - - -          JOIN LOBBY         - - - - - - - - - - - - - - - //
            case MinesweeperProtocol.CMD_JOIN_LOBBY:

                // check if the number of arguments is correct
                if(parametersAmount > MinesweeperProtocol.NBR_PARAM_JOIN_LOBBY){
                    answer = MinesweeperProtocol.STATUS_550 + " " + MinesweeperProtocol.REPLY_TOO_MANY_ARGUMENTS;
                }
                else if(parametersAmount < MinesweeperProtocol.NBR_PARAM_JOIN_LOBBY){
                    answer = MinesweeperProtocol.STATUS_550 + " " + MinesweeperProtocol.REPLY_NOT_ENOUGH_ARGUMENTS;
                }
                // check if the player already has a lobby
                else if(lobby != null){
                    answer = MinesweeperProtocol.STATUS_550 + " " + MinesweeperProtocol.REPLY_ALREADY_IN_A_LOBBY;
                }
                else{

                    // Check if the lobby exists
                    player = new Player(this, parameters[1]);
                    lobby  = Lobby.findLobby(parameters[0]);

                    if(lobby == null){
                        player = null;
                        answer = MinesweeperProtocol.STATUS_650 + " " + MinesweeperProtocol.REPLY_LOBBY_NOT_FOUND;
                    }
                    // try to join the lobby
                    else if(lobby.joinLobby(player) == MinesweeperProtocol.STATUS_650_I){
                        player = null;
                        lobby  = null;
                        break;
                    } else {
                        answer = MinesweeperProtocol.STATUS_250 + " " + MinesweeperProtocol.REPLY_LOBBY_JOINED;
                    }
                }

                this.print(answer);
                LOG.log(Level.INFO, answer);
                break;




            // - - - - - - - - - - - - - - -          OPEN LOBBY          - - - - - - - - - - - - - - - //
            case MinesweeperProtocol.CMD_OPEN_LOBBY:

                // Check if there actually is a lobby
                if(lobby == null){
                    answer = MinesweeperProtocol.STATUS_550 + " " + MinesweeperProtocol.REPLY_NO_LOBBY_CREATED;
                }
                else {
                    // try to open the lobby
                    status = lobby.openLobby(player);

                    if(status == MinesweeperProtocol.STATUS_250_I){
                        answer = MinesweeperProtocol.STATUS_250 + " " + MinesweeperProtocol.REPLY_OK;
                    }else {
                        break;
                    }

                }

                this.print(answer);
                LOG.log(Level.INFO, answer);
                break;



            //TODO
            // - - - - - - - - - - - - - - -          QUIT GAME          - - - - - - - - - - - - - - - //
            case MinesweeperProtocol.CMD_QUIT_GAME:
                answer = MinesweeperProtocol.STATUS_650 + " the command \"" + MinesweeperProtocol.CMD_QUIT_GAME +
                        "\" has not been implemented yet.";
                this.print(answer);
                LOG.log(Level.INFO, answer);
                break;


            // - - - - - - - - - - - - - - -          QUIT LOBBY         - - - - - - - - - - - - - - - //
            case MinesweeperProtocol.CMD_QUIT_LOBBY:
                if(lobby == null){
                    answer = MinesweeperProtocol.STATUS_550 + " " + MinesweeperProtocol.REPLY_NO_LOBBY_JOINED;
                }
                else {
                    synchronized (lobby.getLobbyLocker()){
                        if(lobby.quitLobby(player) == MinesweeperProtocol.STATUS_250_I){
                            player = null;
                            lobby  = null;
                            answer = MinesweeperProtocol.STATUS_250 + " " + MinesweeperProtocol.REPLY_LOBBY_LEFT;
                        }
                        else {
                            break;
                        }
                    }
                }

                this.print(answer);
                LOG.log(Level.INFO, answer);
                break;



            // - - - - - - - - - - - - - - -     SET MINE PROPORTION     - - - - - - - - - - - - - - - //
            case MinesweeperProtocol.CMD_SET_MINE_PROPORTION:
                // check if the number of arguments is correct
                if(parametersAmount > MinesweeperProtocol.NBR_PARAM_SET_MINE_PROPORTION){
                    answer = MinesweeperProtocol.STATUS_550 + " " + MinesweeperProtocol.REPLY_TOO_MANY_ARGUMENTS;
                }
                else if(parametersAmount < MinesweeperProtocol.NBR_PARAM_SET_MINE_PROPORTION){
                    answer = MinesweeperProtocol.STATUS_550 + " " + MinesweeperProtocol.REPLY_NOT_ENOUGH_ARGUMENTS;
                }
                // check if the player has a lobby
                else if(lobby == null){
                    answer = MinesweeperProtocol.STATUS_550 + " " + MinesweeperProtocol.REPLY_NO_LOBBY_JOINED;
                }
                else{
                    synchronized (lobby.getLobbyLocker()){
                        if(lobby.setMineProportion(player, Integer.parseInt(parameters[0])) == MinesweeperProtocol.STATUS_250_I){
                            answer = MinesweeperProtocol.STATUS_250 + " " + MinesweeperProtocol.REPLY_OK;
                        }else{
                            break;
                        }
                    }
                }
                this.print(answer);
                LOG.log(Level.INFO, answer);
                break;



            // - - - - - - - - - - - - - - -      SET PLAYER AMOUNT     - - - - - - - - - - - - - - - //
            case MinesweeperProtocol.CMD_SET_PLAYER_AMOUNT:
                // check if the number of arguments is correct
                if(parametersAmount > MinesweeperProtocol.NBR_PARAM_SET_PLAYER_AMOUNT){
                    answer = MinesweeperProtocol.STATUS_550 + " " + MinesweeperProtocol.REPLY_TOO_MANY_ARGUMENTS;
                }
                else if(parametersAmount < MinesweeperProtocol.NBR_PARAM_SET_PLAYER_AMOUNT){
                    answer = MinesweeperProtocol.STATUS_550 + " " + MinesweeperProtocol.REPLY_NOT_ENOUGH_ARGUMENTS;
                }
                // check if the player has a lobby
                else if(lobby == null){
                    answer = MinesweeperProtocol.STATUS_550 + " " + MinesweeperProtocol.REPLY_NO_LOBBY_JOINED;
                }
                else{
                    synchronized (lobby.getLobbyLocker()){
                        if(lobby.setPlayerAmount(player, Integer.parseInt(parameters[0])) == MinesweeperProtocol.STATUS_250_I){
                            answer = MinesweeperProtocol.STATUS_250 + " " + MinesweeperProtocol.REPLY_OK;
                        }else{
                            break;
                        }
                    }
                }

                this.print(answer);
                LOG.log(Level.INFO, answer);
                break;




            // - - - - - - - - - - - - - - -       SET SCORE MODE      - - - - - - - - - - - - - - - //
            case MinesweeperProtocol.CMD_SET_SCORE_MODE:
                // check if the number of arguments is correct
                if(parametersAmount > MinesweeperProtocol.NBR_PARAM_SET_SCORE_MODE){
                    answer = MinesweeperProtocol.STATUS_550 + " " + MinesweeperProtocol.REPLY_TOO_MANY_ARGUMENTS;
                }
                else if(parametersAmount < MinesweeperProtocol.NBR_PARAM_SET_SCORE_MODE){
                    answer = MinesweeperProtocol.STATUS_550 + " " + MinesweeperProtocol.REPLY_NOT_ENOUGH_ARGUMENTS;
                }
                // check if the player has a lobby
                else if(lobby == null){
                    answer = MinesweeperProtocol.STATUS_550 + " " + MinesweeperProtocol.REPLY_NO_LOBBY_JOINED;
                }
                else{
                    synchronized (lobby.getLobbyLocker()){
                        if(lobby.setScoreMode(player, parameters[0]) == MinesweeperProtocol.STATUS_250_I){
                            answer = MinesweeperProtocol.STATUS_250 + " " + MinesweeperProtocol.REPLY_OK;
                        }else{
                            break;
                        }
                    }
                }
                this.print(answer);
                LOG.log(Level.INFO, answer);
                break;




            // - - - - - - - - - - - - - - -          SET SIZE         - - - - - - - - - - - - - - - //
            case MinesweeperProtocol.CMD_SET_SIZE:
                // check if the number of arguments is correct
                if(parametersAmount > MinesweeperProtocol.NBR_PARAM_SET_SIZE){
                    answer = MinesweeperProtocol.STATUS_550 + " " + MinesweeperProtocol.REPLY_TOO_MANY_ARGUMENTS;
                }
                else if(parametersAmount < MinesweeperProtocol.NBR_PARAM_SET_SIZE){
                    answer = MinesweeperProtocol.STATUS_550 + " " + MinesweeperProtocol.REPLY_NOT_ENOUGH_ARGUMENTS;
                }
                // check if the player has a lobby
                else if(lobby == null){
                    answer = MinesweeperProtocol.STATUS_550 + " " + MinesweeperProtocol.REPLY_NO_LOBBY_JOINED;
                }
                else{
                    synchronized (lobby.getLobbyLocker()){
                        if(lobby.setFieldSize(player, Integer.parseInt(parameters[0]), Integer.parseInt(parameters[1]))
                                == MinesweeperProtocol.STATUS_250_I){
                            answer = MinesweeperProtocol.STATUS_250 + " " + MinesweeperProtocol.REPLY_OK;
                        }else{
                            break;
                        }
                    }
                }
                this.print(answer);
                LOG.log(Level.INFO, answer);
                break;




            // - - - - - - - - - - - - - - -         START GAME        - - - - - - - - - - - - - - - //
            case MinesweeperProtocol.CMD_START_GAME:
                if(lobby == null){
                    // Check that the player is in a lobby
                    answer = MinesweeperProtocol.STATUS_550 + " " + MinesweeperProtocol.REPLY_NO_LOBBY_CREATED;
                } else if(mineSweeperGame != null){
                    // Check that the player is in a game
                    answer = MinesweeperProtocol.STATUS_550 + " " + MinesweeperProtocol.REPLY_ALREADY_IN_A_GAME;
                }else if(lobby.getPlayers().size() < Configuration.MIN_SLOT || lobby.getPlayers().size() > Configuration.MAX_SLOT) {
                    // Check that the number of player in the slot is allowed
                    answer = MinesweeperProtocol.STATUS_550 + " " + MinesweeperProtocol.REPLY_NOT_ENOUGH_PLAYER;
                }else if(lobby.isOpened()){
                    // Check that the lobby is closed
                    answer = MinesweeperProtocol.STATUS_550 + " " + MinesweeperProtocol.REPLY_LOBBY_OPENED;
                }else {

                    synchronized (lobby.getLobbyLocker()){
                        // creating a new game
                        this.mineSweeperGame = new MineSweeperGame(lobby.getPlayers(), lobby.getConfig());

                        // Deletes the lobby
                        Lobby.deleteLobby(lobby);
                    }

                    lobby = null;

                    answer = MinesweeperProtocol.STATUS_350 + " " + MinesweeperProtocol.REPLY_GAME_STARTED;
                    mineSweeperGame.sendAllPlayer(answer);
                    answer = MinesweeperProtocol.STATUS_250 + " " + MinesweeperProtocol.REPLY_OK;
                }

                this.print(answer);
                LOG.log(Level.INFO, answer);
                break;


            //TODO
            // - - - - - - - - - - - - - - -            SWEEP          - - - - - - - - - - - - - - - //
            case MinesweeperProtocol.CMD_SWEEP:
                answer = MinesweeperProtocol.STATUS_650 + " the command \"" + MinesweeperProtocol.CMD_SWEEP +
                        "\" has not been implemented yet.";
                this.print(answer);
                LOG.log(Level.INFO, answer);
                break;



            // - - - - - - - - - - - - - - -      UNKNOWN COMMAND      - - - - - - - - - - - - - - - //
            default:
                answer = MinesweeperProtocol.STATUS_750 + " " + MinesweeperProtocol.REPLY_UNKNOWN_COMMAND;
                this.print(answer);
                LOG.log(Level.INFO, answer);
                break;
        }

        return 0;
    }


    /**
     * Function that sends an answer to the client, using the CARRIAGE_RETURN character.
     *
     * @param answer    : The answer to send
     */
    public void print(String answer){
        pw.println(answer);
        pw.flush();
    }


    /**
     * Function that split the parameters using a delimiter and return an array containing the parameters
     *
     * @param parameters    the line in which the parameters are included
     * @param delim         the delimiter
     *
     * @return An array with the different parameters
     */
    private String[] parameter(String parameters, String delim){

        String regex = delim + "+";
        String[] paramTable;

        // Get rid of the first delim string, so that we won't have an
        // empty String in our table.
        if(parameters.startsWith(delim))
            parameters = parameters.replaceFirst(regex, "");

        paramTable = parameters.split(regex, 0);

        // If the first parameter is an empty string, we return an empty table.
        // better solution ?
        if(paramTable.length > 0){
            if(paramTable[0].equals("")){
                paramTable = new String[0];
            }
        }

        return paramTable;
    }
}
