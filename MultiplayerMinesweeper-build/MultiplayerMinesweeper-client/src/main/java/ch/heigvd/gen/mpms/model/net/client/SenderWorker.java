package ch.heigvd.gen.mpms.model.net.client;

import ch.heigvd.gen.mpms.controller.MainController;
import ch.heigvd.gen.mpms.model.net.Protocol.MinesweeperProtocol;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @brief This class implements a client request sender. It will send all the messages to the server but
 *        it won't handle the answers.
 */
public class SenderWorker {

    final static Logger LOG = Logger.getLogger(ReceptionistWorker.class.getName());

    private Socket      clientSocket;
    private PrintWriter pw;
    private boolean     connected;


    public SenderWorker(){
        connected = false;
    }


    /**
     * Connects to a remote server.
     *
     * @param addressServer     : The server address
     * @param port              : the server port
     *
     * @return the connection Socket, or null if the connection was not possible.
     */
    public Socket connect(String addressServer, int port){
        try {
            clientSocket = new Socket(addressServer, port);
            pw           = new PrintWriter(new OutputStreamWriter(clientSocket.getOutputStream()), true);
            connected    = true;

            return clientSocket;

        } catch (IOException e) {
            LOG.log(Level.INFO, "Impossible to connect to the server.");
            return null;
        }
    }

    /**
     * @brief send a command to the server to disconnect from it.
     *
     * @return -1 if the clientSocket was not initialised, or if we are not connected to the server
     *          0 if the command was sent.
     */
    public int disconnect() {
        String command;

        if(clientSocket == null || !clientSocket.isConnected())
            return -1;

        command = MinesweeperProtocol.CMD_DISCONNECT;

        this.print(command);

        return 0;
    }


    /**
     * @brief Sends a command to the server to create a new lobby. The player will be the admin of
     *        the lobby.
     *
     * @param lobbyName     : The name of the lobby to create
     * @param playerName    : The name of the admin
     *
     * @return -1 if the clientSocket was not initialised, or if we are not connected to the server
     *          0 if the command was sent.
     */
    public int createLobby(String lobbyName, String playerName){

        String command;

        if(clientSocket == null || !clientSocket.isConnected())
            return -1;

        command = MinesweeperProtocol.CMD_CREATE_LOBBY + MinesweeperProtocol.DELIMITER + lobbyName +
                MinesweeperProtocol.DELIMITER + playerName;

        this.print(command);



        return 0;
    }

    /**
     * @brief Sends a command to the server to join a lobby.
     *
     * @param lobbyName     : The name of the lobby to join
     * @param playerName    : The name of the player
     *
     * @return -1 if the clientSocket was not initialised, or if we are not connected to the server
     *          0 if the command was sent.
     */
    public int joinLobby(String lobbyName, String playerName){

        String command;

        if(clientSocket == null || !clientSocket.isConnected())
            return -1;

        command = MinesweeperProtocol.CMD_JOIN_LOBBY + MinesweeperProtocol.DELIMITER + lobbyName +
                MinesweeperProtocol.DELIMITER + playerName;

        this.print(command);

        return 0;
    }


    /**
     * @brief Sends a command to server to open the lobby in which the player is in.
     *        The serveur will check that the player is in a lobby, and that the player
     *        is the admin.
     *
     * @return -1 if the clientSocket was not initialised, or if we are not connected to the server
     *          0 if the command was sent.
     */
    public int openLobby(){

        String command;

        if(clientSocket == null || !clientSocket.isConnected())
            return -1;

        command = MinesweeperProtocol.CMD_OPEN_LOBBY;

        this.print(command);
        return 0;
    }


    /**
     * @brief Sends a command to server to close the lobby in which the player is in.
     *        The serveur will check that the player is in a lobby, and that the player
     *        is the admin.
     *
     * @return -1 if the clientSocket was not initialised, or if we are not connected to the server
     *          0 if the command was sent.
     */
    public int closeLobby(){

        String command;

        if(clientSocket == null || !clientSocket.isConnected())
            return -1;

        command = MinesweeperProtocol.CMD_CLOSE_LOBBY;

        this.print(command);
        return 0;
    }

    /**
     * @brief Sends a command to server to set the actual Player Amount.
     *
     * @return -1 if the clientSocket was not initialised, or if we are not connected to the server
     *          0 if the command was sent.
     */
    public int setPlayerAmount(int playerAmount){
        String command;

        if(clientSocket == null || !clientSocket.isConnected())
            return -1;

        command = MinesweeperProtocol.CMD_SET_PLAYER_AMOUNT + MinesweeperProtocol.DELIMITER + playerAmount;

        this.print(command);

        return 0;
    }


    /**
     * @brief cleans up the ressources of the Sender.
     */
    public void cleanup(){
        try {
            if (pw != null)
                pw.close();

            if (clientSocket != null)
                clientSocket.close();

            connected = false;

        } catch (IOException e) {
            LOG.log(Level.SEVERE, e.getMessage(), e);
        }
    }

    /**
     * @brief Function that sends an command to the server, using the CARRIAGE_RETURN character.
     *
     * @param command    : The command to send
     */
    private void print(String command){
        pw.println(command + MinesweeperProtocol.CARRIAGE_RETURN);
        LOG.log(Level.INFO, command);
    }
}
