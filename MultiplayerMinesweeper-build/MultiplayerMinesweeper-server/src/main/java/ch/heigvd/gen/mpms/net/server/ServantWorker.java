package ch.heigvd.gen.mpms.net.server;

import ch.heigvd.gen.mpms.net.Protocol.MinesweeperProtocol;

import java.io.*;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * cf https://wasadigi.gitbooks.io/res-heigvd-network-programming-application-protoc/content/tcp_programming.html
 */
public class ServantWorker implements Runnable{

    final static Logger LOG = Logger.getLogger(ServantWorker.class.getName());

    private Socket clientSocket;

    private BufferedReader  br = null;
    private PrintWriter     pw = null;


    public ServantWorker(Socket clientSocket) {

        try {
            this.clientSocket = clientSocket;
            br = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            pw = new PrintWriter(new OutputStreamWriter(clientSocket.getOutputStream()), true);
        } catch (IOException e) {
            LOG.log(Level.SEVERE, e.getMessage(), e);
        }
    }

    public void run() {

        String request;         // The command send by the client
        String answer;          // The answer from the server
        boolean shouldRun;
        int manageRes;


        shouldRun = true;
        answer    = "Welcome to the Multiplayer MineSweeper game !";

        LOG.info(answer);
        print(answer);

        try {

            LOG.info("Reading until client sends QUIT or closes the connection...");

            while ((shouldRun) && (request = br.readLine()) != null) {

                request = request.toUpperCase();

                LOG.info(request);

                manageRes = manageCommand(request);

                // Depending on the result of the manageCommand, the connection will be closed or not
                // (-1 : close connection, 0 : continue)
                if(manageRes == -1){
                    shouldRun = false;
                }

            }

            LOG.info("Cleaning up resources...");

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
     *
     * @param request
     */
    private int manageCommand(String request){

        String answer;
        String command;

        int iter_command;

        for(iter_command = 0; iter_command < MinesweeperProtocol.SUPPORTED_COMMANDS.length; iter_command ++)
            if (request.startsWith(MinesweeperProtocol.SUPPORTED_COMMANDS[iter_command]))
                break;

        command = MinesweeperProtocol.SUPPORTED_COMMANDS[iter_command];

        switch (command){

            case MinesweeperProtocol.CMD_CLOSE_LOBBY:
                answer = MinesweeperProtocol.STATUS_650 + " the command \"" + MinesweeperProtocol.CMD_CLOSE_LOBBY +
                         "\" has not been implemented yet.";
                this.print(answer);
                LOG.log(Level.INFO, answer);
                break;

            case MinesweeperProtocol.CMD_CREATE_LOBBY:
                answer = MinesweeperProtocol.STATUS_650 + " the command \"" + MinesweeperProtocol.CMD_CREATE_LOBBY +
                        "\" has not been implemented yet.";
                this.print(answer);
                LOG.log(Level.INFO, answer);
                break;

            case MinesweeperProtocol.CMD_DISABLE_BONUS_MALUS:
                answer = MinesweeperProtocol.STATUS_650 + " the command \"" + MinesweeperProtocol.CMD_DISABLE_BONUS_MALUS +
                        "\" has not been implemented yet.";
                this.print(answer);
                LOG.log(Level.INFO, answer);
                break;

            case MinesweeperProtocol.CMD_DISCONNECT:
                answer = "Thank's for playing ! see you soon.";
                this.print(answer);
                LOG.log(Level.INFO, answer);
                return -1;

            case MinesweeperProtocol.CMD_ENABLE_BONUS_MALUS:
                answer = MinesweeperProtocol.STATUS_650 + " the command \"" + MinesweeperProtocol.CMD_ENABLE_BONUS_MALUS +
                        "\" has not been implemented yet.";
                this.print(answer);
                LOG.log(Level.INFO, answer);
                break;

            case MinesweeperProtocol.CMD_EXPEL_LOBBY:
                answer = MinesweeperProtocol.STATUS_650 + " the command \"" + MinesweeperProtocol.CMD_EXPEL_LOBBY +
                        "\" has not been implemented yet.";
                this.print(answer);
                LOG.log(Level.INFO, answer);
                break;


            case MinesweeperProtocol.CMD_JOIN_LOBBY:
                answer = MinesweeperProtocol.STATUS_650 + " the command \"" + MinesweeperProtocol.CMD_JOIN_LOBBY +
                        "\" has not been implemented yet.";
                this.print(answer);
                LOG.log(Level.INFO, answer);
                break;

            case MinesweeperProtocol.CMD_OPEN_LOBBY:
                answer = MinesweeperProtocol.STATUS_650 + " the command \"" + MinesweeperProtocol.CMD_OPEN_LOBBY +
                        "\" has not been implemented yet.";
                this.print(answer);
                LOG.log(Level.INFO, answer);
                break;

            case MinesweeperProtocol.CMD_QUIT_GAME:
                answer = MinesweeperProtocol.STATUS_650 + " the command \"" + MinesweeperProtocol.CMD_QUIT_GAME +
                        "\" has not been implemented yet.";
                this.print(answer);
                LOG.log(Level.INFO, answer);
                break;

            case MinesweeperProtocol.CMD_QUIT_LOBBY:
                answer = MinesweeperProtocol.STATUS_650 + " the command \"" + MinesweeperProtocol.CMD_QUIT_LOBBY +
                        "\" has not been implemented yet.";
                this.print(answer);
                LOG.log(Level.INFO, answer);
                break;


            case MinesweeperProtocol.CMD_SET_MINE_AMOUNT:
                answer = MinesweeperProtocol.STATUS_650 + " the command \"" + MinesweeperProtocol.CMD_SET_MINE_AMOUNT +
                        "\" has not been implemented yet.";
                this.print(answer);
                LOG.log(Level.INFO, answer);
                break;


            case MinesweeperProtocol.CMD_SET_PLAYER_AMOUNT:
                answer = MinesweeperProtocol.STATUS_650 + " the command \"" + MinesweeperProtocol.CMD_SET_PLAYER_AMOUNT +
                        "\" has not been implemented yet.";
                this.print(answer);
                LOG.log(Level.INFO, answer);
                break;

            case MinesweeperProtocol.CMD_SET_SCORE_MODE:
                answer = MinesweeperProtocol.STATUS_650 + " the command \"" + MinesweeperProtocol.CMD_SET_SCORE_MODE +
                        "\" has not been implemented yet.";
                this.print(answer);
                LOG.log(Level.INFO, answer);
                break;


            case MinesweeperProtocol.CMD_SET_SIZE:
                answer = MinesweeperProtocol.STATUS_650 + " the command \"" + MinesweeperProtocol.CMD_SET_SIZE +
                        "\" has not been implemented yet.";
                this.print(answer);
                LOG.log(Level.INFO, answer);
                break;


            case MinesweeperProtocol.CMD_START_GAME:
                answer = MinesweeperProtocol.STATUS_650 + " the command \"" + MinesweeperProtocol.CMD_START_GAME +
                        "\" has not been implemented yet.";
                this.print(answer);
                LOG.log(Level.INFO, answer);
                break;

            case MinesweeperProtocol.CMD_SWEEP:
                answer = MinesweeperProtocol.STATUS_650 + " the command \"" + MinesweeperProtocol.CMD_SWEEP +
                        "\" has not been implemented yet.";
                this.print(answer);
                LOG.log(Level.INFO, answer);
                break;


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
     * @param answer    : The answer to send
     */
    private void print(String answer){
        pw.println(answer + MinesweeperProtocol.CARRIAGE_RETURN);
    }
}
