package ch.heigvd.gen.mpms.model.net.client;

import ch.heigvd.gen.mpms.model.net.Protocol.MinesweeperProtocol;

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
public class ReceptionistWorker implements Runnable {

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


                System.out.println(clientSocket.isConnected());
                answer = br.readLine();
                LOG.log(Level.INFO, answer);

                System.out.println("pouet_4");

                // send the answer to the answer manager.
                run = answerManager(answer);

            }


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
                break;
            case MinesweeperProtocol.STATUS_250 :
                confirmationManager(message, parameters);
                break;
            case MinesweeperProtocol.STATUS_350 :
                informationManager(message, parameters);
                break;
            case MinesweeperProtocol.STATUS_450 :
                denialManager(message, parameters);
                break;
            case MinesweeperProtocol.STATUS_550 :
                unauthorizedCommandManager(message, parameters);
                break;
            case MinesweeperProtocol.STATUS_650 :
                rejectionManager(message, parameters);
                break;
            case MinesweeperProtocol.STATUS_750 :
                unknownCommandManager(message, parameters);
                break;

                default:
                    LOG.log(Level.INFO, status + " : " + "Unhandled command status.");
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
                //mineSweeperClient.getMainController().getMainWindowController().setInfoLabel("pouet !");
                LOG.log(Level.INFO, "Unhandled command answer yet.");
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
                LOG.log(Level.INFO, "Unhandled command answer yet.");
                break;


            case  MinesweeperProtocol.REPLY_LOBBY_CLOSED:
                LOG.log(Level.INFO, "Unhandled command answer yet.");
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
                LOG.log(Level.INFO, "Unhandled command answer yet.");
                break;


            case  MinesweeperProtocol.REPLY_SCORE_MODE_IS:
                LOG.log(Level.INFO, "Unhandled command answer yet.");
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
                LOG.log(Level.INFO, "Unhandled command answer yet.");
                break;


            case MinesweeperProtocol.REPLY_NO_LOBBY_CREATED :
                LOG.log(Level.INFO, "Unhandled command answer yet.");
                break;

            case MinesweeperProtocol.REPLY_NO_LOBBY_JOINED :
                LOG.log(Level.INFO, "Unhandled command answer yet.");
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
                LOG.log(Level.INFO, "Unhandled command answer yet.");
                break;


            case MinesweeperProtocol.REPLY_LOBBY_FULL :
                LOG.log(Level.INFO, "Unhandled command answer yet.");
                break;


            case MinesweeperProtocol.REPLY_LOBBY_CLOSED :
                LOG.log(Level.INFO, "Unhandled command answer yet.");
                break;


            case MinesweeperProtocol.REPLY_LOBBY_NOT_FOUND :
                LOG.log(Level.INFO, "Unhandled command answer yet.");
                break;

            case MinesweeperProtocol.REPLY_PLAYER_NAME_NOT_AVAIBALE :
                LOG.log(Level.INFO, "Unhandled command answer yet.");
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
