package ch.heigvd.gen.mpms.model.net.client;

import ch.heigvd.gen.mpms.controller.WindowController;
import ch.heigvd.gen.mpms.model.Game.MineSweeperGame;
import ch.heigvd.gen.mpms.model.GameComponent.Configuration;
import ch.heigvd.gen.mpms.model.GameComponent.Player;
import ch.heigvd.gen.mpms.model.GameComponent.Square;
import ch.heigvd.gen.mpms.model.net.Protocol.MinesweeperProtocol;
import ch.heigvd.gen.mpms.view.MineSweeperWindowStyle;
import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.application.Platform;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;


/**
 * @brief This class implements an answer Receptionnist for the client. It will receive all
 *        the answers from the server, and will manage them. It doesn't sends any request to the server.
 *        It's a threaded instance. Run until the connexion is stopped.
 */
public class ReceptionistWorker extends Thread {

    final static Logger LOG = Logger.getLogger(ReceptionistWorker.class.getName());

	private static final ObjectMapper objectMapper = new ObjectMapper();

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

            while (run == 0 && clientSocket.isConnected()) {


                answer = br.readLine();
                synchronized (this){
                    LOG.log(Level.INFO, answer);
                    // send the answer to the answer manager.
                    run = answerManager(answer);
                }

            }

            if (br != null)
                br.close();

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
                break;


            case  MinesweeperProtocol.REPLY_LOBBY_CREATED:
                // Change the UI Window to lobby window.
                Platform.runLater(()->{
                    mineSweeperClient.getMainController().getWindowController().activate(WindowController.LOBBY_WINDOW);
                    mineSweeperClient.getMainController().getLobbyWindowController().setLobbyNameLabel(mineSweeperClient.getLobby().getName());
                    mineSweeperClient.getMainController().getLobbyWindowController().setAdminLobby();
                });
                break;

            case  MinesweeperProtocol.REPLY_LOBBY_JOINED:
                // Change the UI Window to lobby window.
                Platform.runLater(()->{
                    mineSweeperClient.getMainController().getWindowController().activate(WindowController.LOBBY_WINDOW);
                    mineSweeperClient.getMainController().getLobbyWindowController().setLobbyNameLabel(mineSweeperClient.getLobby().getName());
                    mineSweeperClient.getMainController().getLobbyWindowController().setPlayerLobby();
                });
                break;

            case  MinesweeperProtocol.REPLY_LOBBY_LEFT:
                // Change the UI Window to lobby window.
                Platform.runLater(()->{
                    mineSweeperClient.getMainController().getWindowController().activate(WindowController.MAIN_WINDOW);
                    mineSweeperClient.getMainController().getLobbyWindowController().cleanUp();
                    mineSweeperClient.disconnect();
                });
                break;

            case  MinesweeperProtocol.REPLY_GAME_LEFT:
                // Change the UI Window to lobby window.
                Platform.runLater(()->{
                    mineSweeperClient.getMainController().getWindowController().activate(WindowController.MAIN_WINDOW);
                    //mineSweeperClient.getMainController().getLobbyWindowController().cleanUp();
                    mineSweeperClient.setMineSweeperGame(null);
                    mineSweeperClient.disconnect();
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
                Platform.runLater(()->{
                    mineSweeperClient.getLobby().addPlayer(new Player(parameters));
                    mineSweeperClient.getMainController().getLobbyWindowController().addPlayer(parameters);
                });
                break;


            case  MinesweeperProtocol.REPLY_LOBBY_LEFT_BY:
                Platform.runLater(()->{
                    mineSweeperClient.getLobby().removePlayer(parameters);
                    mineSweeperClient.getMainController().getLobbyWindowController().removePlayer(parameters);
                });
                break;


            case  MinesweeperProtocol.REPLY_YOU_HAVE_BEEN_EXPELLED:
                Platform.runLater(()->{
                    mineSweeperClient.getMainController().getWindowController().activate(WindowController.MAIN_WINDOW);
                    mineSweeperClient.getMainController().getLobbyWindowController().cleanUp();
                    mineSweeperClient.getMainController().getMainWindowController().setInfoLabel(MinesweeperProtocol.REPLY_YOU_HAVE_BEEN_EXPELLED);
                    mineSweeperClient.disconnect();
                });
                break;


            case  MinesweeperProtocol.REPLY_MINE_PROPORTION_IS:
                Platform.runLater(()->{
                    int mine_proportion;
                    try {
                        mine_proportion = Integer.parseInt(parameters);
                        mineSweeperClient.getLobby().getConfig().setMineProportion(mine_proportion);
                        mineSweeperClient.getMainController().getLobbyWindowController().setMineProportion(mine_proportion);
                    }catch (NumberFormatException e){
                        LOG.log(Level.SEVERE, e.getMessage(), e);
                    }
                });
                break;


            case  MinesweeperProtocol.REPLY_PLAYER_AMOUNT_IS:
                Platform.runLater(()->{
                    int player_amount;
                    try {
                        player_amount = Integer.parseInt(parameters);
                        mineSweeperClient.getLobby().getConfig().setNbrSlot(player_amount);
                        mineSweeperClient.getMainController().getLobbyWindowController().setPlayerAmount(player_amount);
                    }catch (NumberFormatException e){
                        LOG.log(Level.SEVERE, e.getMessage(), e);
                    }
                });

                break;


            case  MinesweeperProtocol.REPLY_SCORE_MODE_IS:
                Platform.runLater(()->{

                    for(Configuration.ScoreMode sm : Configuration.ScoreMode.class.getEnumConstants()){
                        if(sm.toString().equals(parameters)) {
                            mineSweeperClient.getLobby().getConfig().setScore(sm);
                            mineSweeperClient.getMainController().getLobbyWindowController().setScoreMode(sm);
                        }
                    }
                });
                break;


            case  MinesweeperProtocol.REPLY_SIZE_IS:
                Platform.runLater(()->{
                    int width;
                    int height;
                    String size[];

                    size = parameters.split(MinesweeperProtocol.FIELD_SIZE_DELIMITER);


                    if(size.length != 2)
                        return;

                    try {
                        width  = Integer.parseInt(size[0]);
                        height = Integer.parseInt(size[1]);

                        mineSweeperClient.getLobby().getConfig().setWidth(width);
                        mineSweeperClient.getLobby().getConfig().setHeight(height);

                        mineSweeperClient.getMainController().getLobbyWindowController().setFieldSize(parameters);
                    }catch (NumberFormatException e){
                        e.printStackTrace();
                    }

                });
                break;


            case  MinesweeperProtocol.REPLY_BONUS_MALUS_ENABLED:
                Platform.runLater(()->{
                    mineSweeperClient.getLobby().getConfig().setBonus(true);
                    mineSweeperClient.getLobby().getConfig().setMalus(true);
                    mineSweeperClient.getMainController().getLobbyWindowController().enableBonusMalusCheckbox();
                });
                break;


            case  MinesweeperProtocol.REPLY_BONUS_MALUS_DISABLED:
                Platform.runLater(()->{
                    mineSweeperClient.getLobby().getConfig().setBonus(false);
                    mineSweeperClient.getLobby().getConfig().setMalus(false);
                    mineSweeperClient.getMainController().getLobbyWindowController().disableBonusMalusCheckbox();
                });
                break;

            case  MinesweeperProtocol.REPLY_GAME_STARTED:
                Platform.runLater(()->{

                    mineSweeperClient.getMainController().getMineSweeperWindowController().setField(
                            mineSweeperClient.getLobby().getConfig().getWidth(),mineSweeperClient.getLobby().getConfig().getHeight());

                    mineSweeperClient.setMineSweeperGame(new MineSweeperGame(mineSweeperClient.getLobby().getPlayers()));
                    mineSweeperClient.getMainController().getLobbyWindowController().cleanUp();
                    mineSweeperClient.setLobby(null);

                    mineSweeperClient.getMainController().getMineSweeperWindowController().setInfoLabel(MineSweeperWindowStyle.INFO_DEFAULT);

                    mineSweeperClient.getMainController().getMineSweeperWindowController().setPlayer(mineSweeperClient.getMineSweeperGame().getPlayers());
                    mineSweeperClient.getMainController().getWindowController().activate(WindowController.MINESWEEPER_WINDOW);
                });
                break;

            case  MinesweeperProtocol.REPLY_SQUARE_SWEPT:
                Platform.runLater(()->{
                    Vector<Square> sweptSquare;
                    try {
                    	sweptSquare = objectMapper.readValue(parameters, objectMapper.getTypeFactory().constructCollectionType(Vector.class, Square.class));
                        mineSweeperClient.getMainController().getMineSweeperWindowController().refreshGame(sweptSquare);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });
                break;


            case  MinesweeperProtocol.REPLY_PLAYER_SCORE:
                Platform.runLater(()->{
                    Player   player;
                    int      score;
                    String[] param;

                    param = parameters.split(MinesweeperProtocol.DELIMITER);

                    if(param.length != 2)
                        return;

                    player = mineSweeperClient.getMineSweeperGame().getPlayer(param[0]);

                    if(player == null)
                        return;

                    try {
                        score = Integer.parseInt(param[1]);
                    } catch (NumberFormatException e) {
                        e.printStackTrace();
                        return;
                    }

                    player.setScore(score);

                    mineSweeperClient.getMainController().getMineSweeperWindowController().setScore(player);
                });
                break;


            case  MinesweeperProtocol.REPLY_PLAYER_LEFT:
                LOG.log(Level.INFO, "Unhandled command answer yet.");
                break;


            case  MinesweeperProtocol.REPLY_PLAYER_DIED:
                Platform.runLater(()->{
                    mineSweeperClient.getMainController().getMineSweeperWindowController().setDead(mineSweeperClient.getMineSweeperGame().getPlayer(parameters));
                });
                break;


            case  MinesweeperProtocol.REPLY_MINES_ARE:
                Platform.runLater(()->{
                    Vector<Square> mines;
                    try {
                    	mines = objectMapper.readValue(parameters, objectMapper.getTypeFactory().constructCollectionType(Vector.class, Square.class));
                    	mineSweeperClient.getMainController().getMineSweeperWindowController().showMines(mines);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });
                break;


            case  MinesweeperProtocol.REPLY_GAME_FINISHED:
                Platform.runLater(()->{
                    mineSweeperClient.getMainController().getMineSweeperWindowController().setInfoLabel(MineSweeperWindowStyle.INFO_WINNER + parameters);
                });
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

            case MinesweeperProtocol.REPLY_NOT_ENOUGH_PLAYER :
                Platform.runLater(()->{
                    mineSweeperClient.getMainController().getLobbyWindowController().setInfoLabel(MinesweeperProtocol.REPLY_NOT_ENOUGH_PLAYER);
                });
                break;


            case MinesweeperProtocol.REPLY_LOBBY_OPENED :
                Platform.runLater(()->{
                    mineSweeperClient.getMainController().getLobbyWindowController().setInfoLabel(MinesweeperProtocol.REPLY_LOBBY_OPENED);
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
