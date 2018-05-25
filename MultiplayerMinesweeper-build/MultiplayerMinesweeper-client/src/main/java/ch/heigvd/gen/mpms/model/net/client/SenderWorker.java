package ch.heigvd.gen.mpms.model.net.client;

import ch.heigvd.gen.mpms.model.net.Protocol.MinesweeperProtocol;

import java.io.IOException;
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
            pw           = new PrintWriter(clientSocket.getOutputStream());
            connected    = true;

            return clientSocket;

        } catch (IOException e) {
            LOG.log(Level.INFO, "Impossible to connect to the server.");
            return null;
        }
    }

    /**
     * Disconnects to a remot server
     *
     * @return
     */
    public int disconnect() {
        String command;

        if(clientSocket == null || !clientSocket.isConnected())
            return -1;

        command = MinesweeperProtocol.CMD_DISCONNECT;

        this.print(command);

        this.cleanup();
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
        LOG.log(Level.INFO, command);

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
        LOG.log(Level.INFO, command);

        return 0;
    }



    public void cleanup(){
        try {
            if (pw != null)
                pw.close();

            if (clientSocket != null)
                clientSocket.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * @brief Function that sends an command to the server, using the CARRIAGE_RETURN character.
     *
     * @param answer    : The command to send
     */
    private void print(String answer){
        pw.println(answer + MinesweeperProtocol.CARRIAGE_RETURN);
    }
}
