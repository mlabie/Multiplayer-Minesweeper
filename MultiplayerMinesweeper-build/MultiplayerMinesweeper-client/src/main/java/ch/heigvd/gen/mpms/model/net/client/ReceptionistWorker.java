package ch.heigvd.gen.mpms.model.net.client;

import ch.heigvd.gen.mpms.controller.WindowController;
import ch.heigvd.gen.mpms.model.net.Protocol.MinesweeperProtocol;
import javafx.application.Platform;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;


/**
 * @brief This class implements an answer Receptionnist for the client. It will receive all
 *        the answers from the server, and will manage them. It doesn't sends any request to the server.
 *        It's a threaded instance. Run until the connexion is stopped.
 */
public class ReceptionistWorker extends Thread {

    final static Logger LOG = Logger.getLogger(ReceptionistWorker.class.getName());

    private Socket clientSocket;
    private BufferedReader br;

    private MineSweeperClient mineSweeperClient;

    /**
     * Constructor of the receptionnist. It needs the client Socket of the connection.
     *
     * @param clientSocket              : The client socket from where the receptionnist will get the answers from
     *                                    the server.
     *
     * @throws NullPointerException     : If the clientSocket is null.
     */
    public ReceptionistWorker(Socket clientSocket) throws NullPointerException{
        if(clientSocket == null)
            throw new NullPointerException("Null pointer for clientSocket");

        this.clientSocket = clientSocket;
        mineSweeperClient = null;
    }

    public void setMineSweeperClient(MineSweeperClient mineSweeperClient) {
        this.mineSweeperClient = mineSweeperClient;
    }

    public MineSweeperClient getMineSweeperClient() {
        return mineSweeperClient;
    }


    /**
     * Run method of the thread. Starts the client receptionnist. Runs until the connection is stopped.
     */
    @Override
    public void run() {

        String answer;
        int    run;

        try {
            br  = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            run = 0;

            while (clientSocket.isConnected() && run == 0) {


                answer = br.readLine();
                synchronized (this){

                    LOG.log(Level.INFO, answer);
                    // send the answer to the answer manager.
                    run = answerManager(answer);
                }
            }

            if (br != null)
                br.close();

            if (clientSocket != null)
                clientSocket.close();

        }catch (IOException e){
            LOG.log(Level.SEVERE, e.getMessage(), e);
        }

    }

    /**
     * Split the answer received from the server and send it to one of those following function depending
     * on the command status :
     *
     *         Function                      Status
     *
     *  - connexionManager            :       220
     *  - confirmationManager         :       250
     *  - informationManager          :       350
     *  - denialManager               :       450
     *  - unauthorizedCommandManager  :       550
     *  - rejectionManager            :       650
     *  - unknownCommandManager       :       750
     *
     *
     * @param fullAnswer    : The entire answer received from the server, with the status command.
     *
     * @return
     */
    private int answerManager(String fullAnswer){


        String status;
        String message;
        String parameters;
        String answer;          // The answer without the status

        String[] splitedAnswer;


        status  = fullAnswer.substring(0, fullAnswer.indexOf(MinesweeperProtocol.DELIMITER));
        answer  = fullAnswer.replaceFirst(status + MinesweeperProtocol.DELIMITER, "");

        splitedAnswer = answer.split(MinesweeperProtocol.REPLY_PARAM_DELIMITER);


        switch (splitedAnswer.length){
            case 1 :
                message    = splitedAnswer[0];
                parameters = "";
                break;
            case 2:
                message    = splitedAnswer[0];
                parameters = splitedAnswer[1];
                break;
                default:
                    LOG.log(Level.INFO, " This Parameters amount is not handled : " + splitedAnswer.length);
                    return 0;
        }


        switch (status){

            case MinesweeperProtocol.STATUS_220 :
                return connexionManager(message, parameters);
            case MinesweeperProtocol.STATUS_250 :
                return confirmationManager(message, parameters);
            case MinesweeperProtocol.STATUS_350 :
                return informationManager(message, parameters);
            case MinesweeperProtocol.STATUS_450 :
                return denialManager(message, parameters);
            case MinesweeperProtocol.STATUS_550 :
                return unauthorizedCommandManager(message, parameters);
            case MinesweeperProtocol.STATUS_650 :
                return rejectionManager(message, parameters);
            case MinesweeperProtocol.STATUS_750 :
                return unknownCommandManager(message, parameters);
                default:
                    LOG.log(Level.INFO, status + " : " + "Unhandled command status.");
                    break;
        }
        return 0;
    }


    /**
     * Manage the message of the server if it's a connexion message, which means its status
     * is 220.
     *
     * @param message       : The message of the answer
     * @param parameters    : The parameters of the message. It's an empty String if there were none.
     *
     * @return
     */
    private int connexionManager(String message, String parameters){

        switch (message){

            case MinesweeperProtocol.WELCOME :
                notify();
                break;


            case  MinesweeperProtocol.GOODBYE:
                notify();
                return -1;

            default:
                LOG.log(Level.INFO, "Unhandled confirmation message.");
                break;
        }

        return 0;
    }


    /**
     * Manage the message of the server if it's a confirmation message, which means its status
     * is 250.
     *
     * @param message       : The message of the answer
     * @param parameters    : The parameters of the message. It's an empty String if there were none.
     *
     * @return
     */
    private int confirmationManager(String message, String parameters){

        switch (message){

            case MinesweeperProtocol.REPLY_OK :
                LOG.log(Level.INFO, "Unhandled command answer yet.");
                break;


            case  MinesweeperProtocol.REPLY_LOBBY_CREATED:
                // Change the UI Window to lobby window.
                Platform.runLater(()->{
                    mineSweeperClient.getMainController().getWindowController().activate(WindowController.LOBBY_WINDOW);
                    mineSweeperClient.getMainController().getLobbyWindowController().setAdminLobby();
                });
                break;

            case  MinesweeperProtocol.REPLY_LOBBY_JOINED:
                // Change the UI Window to lobby window.
                Platform.runLater(()->{
                    mineSweeperClient.getMainController().getWindowController().activate(WindowController.LOBBY_WINDOW);
                    mineSweeperClient.getMainController().getLobbyWindowController().setPlayerLobby();
                });
                break;


            default:
                LOG.log(Level.INFO, "Unhandled confirmation message.");
                break;
        }

        return 0;
    }


    private int informationManager(String message, String parameters){

        switch (message){

            case  MinesweeperProtocol.REPLY_LOBBY_OPENED:
                Platform.runLater(()->{
                    mineSweeperClient.getMainController().getLobbyWindowController().setOpenedLobby();
                });
                break;


            case  MinesweeperProtocol.REPLY_LOBBY_CLOSED:
                Platform.runLater(()->{
                    mineSweeperClient.getMainController().getLobbyWindowController().setClosedLobby();
                });
                break;


            case MinesweeperProtocol.REPLY_LOBBY_JOINED_BY :
                LOG.log(Level.INFO, "Unhandled command answer yet.");
                break;


            case  MinesweeperProtocol.REPLY_LOBBY_LEFT_BY:
                LOG.log(Level.INFO, "Unhandled command answer yet.");
                break;


            case  MinesweeperProtocol.REPLY_YOU_HAVE_BEEN_EXPELLED:
                LOG.log(Level.INFO, "Unhandled command answer yet.");
                break;


            case  MinesweeperProtocol.REPLY_MINE_PROPORTION_IS:
                LOG.log(Level.INFO, "Unhandled command answer yet.");
                break;


            case  MinesweeperProtocol.REPLY_PLAYER_AMOUNT_IS:
                // Check that the amount of parameters is correct.
                /*if(parameters.length() != MinesweeperProtocol.NBR_PARAM_SET_PLAYER_AMOUNT)
                    break;*/


                Platform.runLater(()->{
                    try {
                        mineSweeperClient.getMainController().getLobbyWindowController().setPlayerAmount(Integer.parseInt(parameters));
                    }catch (NumberFormatException e){

                    }
                });

                break;


            case  MinesweeperProtocol.REPLY_SCORE_MODE_IS:
                Platform.runLater(()->{
                    mineSweeperClient.getMainController().getLobbyWindowController().setScoreMode(parameters);
                });
                break;


            case  MinesweeperProtocol.REPLY_SIZE_IS:
                LOG.log(Level.INFO, "Unhandled command answer yet.");
                break;


            case  MinesweeperProtocol.REPLY_BONUS_MALUS_ENABLED:
                LOG.log(Level.INFO, "Unhandled command answer yet.");
                break;


            case  MinesweeperProtocol.REPLY_BONUS_MALUS_DISABLED:
                LOG.log(Level.INFO, "Unhandled command answer yet.");
                break;


                default:
                    LOG.log(Level.INFO, "Unhandled information message.");
                    break;
        }


        return 0;
    }

    private int denialManager(String message, String parameters){
        switch (message){

            case MinesweeperProtocol.REPLY_ACTION_DENIED :
                LOG.log(Level.INFO, "Unhandled command answer yet.");
                break;

            default:
                LOG.log(Level.INFO, "Unhandled denial message.");
                break;
        }
        return 0;
    }

    private int unauthorizedCommandManager(String message, String parameters){

        switch (message){

            case MinesweeperProtocol.REPLY_NOT_ENOUGH_ARGUMENTS :
                LOG.log(Level.INFO, "Unhandled command answer yet.");
                break;


            case MinesweeperProtocol.REPLY_TOO_MANY_ARGUMENTS :
                LOG.log(Level.INFO, "Unhandled command answer yet.");
                break;


            case MinesweeperProtocol.REPLY_ALREADY_IN_A_LOBBY :
                Platform.runLater(()->{
                    // Disconnect.
                    mineSweeperClient.disconnect();
                });
                break;


            case MinesweeperProtocol.REPLY_NO_LOBBY_CREATED :
                Platform.runLater(()->{
                    // Disconnect.
                    mineSweeperClient.disconnect();
                });
                break;

            case MinesweeperProtocol.REPLY_NO_LOBBY_JOINED :
                Platform.runLater(()->{
                    // Disconnect.
                    mineSweeperClient.disconnect();
                });
                break;


            default:
                LOG.log(Level.INFO, "Unhandled unauthorized command message.");
                break;
        }

        return 0;
    }

    private int rejectionManager(String message, String parameters){

        switch (message){

            case MinesweeperProtocol.REPLY_LOBBY_NAME_NOT_AVAIABLE :
                // Set rejection message
                Platform.runLater(()->{
                    mineSweeperClient.getMainController().getMainWindowController().setInfoLabel(MinesweeperProtocol.REPLY_LOBBY_NAME_NOT_AVAIABLE);
                    // Disconnect.
                    mineSweeperClient.disconnect();
                });
                break;


            case MinesweeperProtocol.REPLY_LOBBY_FULL :
                // Set rejection message
                Platform.runLater(()->{
                    mineSweeperClient.getMainController().getMainWindowController().setInfoLabel(MinesweeperProtocol.REPLY_LOBBY_FULL);
                    // Disconnect.
                    mineSweeperClient.disconnect();
                });
                break;


            case MinesweeperProtocol.REPLY_LOBBY_CLOSED :
                // Set rejection message
                Platform.runLater(()->{
                    mineSweeperClient.getMainController().getMainWindowController().setInfoLabel(MinesweeperProtocol.REPLY_LOBBY_CLOSED);
                    // Disconnect.
                    mineSweeperClient.disconnect();
                });
                break;


            case MinesweeperProtocol.REPLY_LOBBY_NOT_FOUND :
                // Set rejection message
                Platform.runLater(()->{
                    mineSweeperClient.getMainController().getMainWindowController().setInfoLabel(MinesweeperProtocol.REPLY_LOBBY_NOT_FOUND);
                    // Disconnect.
                    mineSweeperClient.disconnect();
                });
                break;

            case MinesweeperProtocol.REPLY_PLAYER_NAME_NOT_AVAIBALE :
                // Set rejection message
                Platform.runLater(()->{
                    mineSweeperClient.getMainController().getMainWindowController().setInfoLabel(MinesweeperProtocol.REPLY_PLAYER_NAME_NOT_AVAIBALE);
                    // Disconnect.
                    mineSweeperClient.disconnect();
                });
                break;

            case MinesweeperProtocol.REPLY_PLAYER_NOT_FOUND :
                LOG.log(Level.INFO, "Unhandled command answer yet.");
                break;

            case MinesweeperProtocol.REPLY_MODE_NOT_FOUND :
                LOG.log(Level.INFO, "Unhandled command answer yet.");
                break;

            case MinesweeperProtocol.REPLY_MINE_PROPORTION_NOT_ALLOWED :
                LOG.log(Level.INFO, "Unhandled command answer yet.");
                break;

            case MinesweeperProtocol.REPLY_SIZE_NOT_ALLOWED :
                LOG.log(Level.INFO, "Unhandled command answer yet.");
                break;


            case MinesweeperProtocol.REPLY_PLAYER_AMOUNT_NOT_ALLOWED :
                LOG.log(Level.INFO, "Unhandled command answer yet.");
                break;


            default:
                LOG.log(Level.INFO, "Unhandled rejection message.");
                break;
        }

        return 0;
    }

    private int unknownCommandManager(String message, String parameters){


        switch (message){

            case MinesweeperProtocol.REPLY_UNKNOWN_COMMAND :
                LOG.log(Level.INFO, "Unhandled command answer yet.");
                break;

            default:
                LOG.log(Level.INFO, "Unhandled unknown command message.");
                break;
        }
        return 0;
    }
}
