package ch.heigvd.gen.mpms.model.net.client;

import ch.heigvd.gen.mpms.controller.*;
import ch.heigvd.gen.mpms.model.Game.MineSweeperGame;
import ch.heigvd.gen.mpms.model.Lobby.Lobby;

import java.net.Socket;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;


/**
 * @brief
 *
 * @author Corentin Basler, Antonio Cusanelli, Marc Labie, Simon Jobin
 */
public class MineSweeperClient {

    final static Logger LOG = Logger.getLogger(ReceptionistWorker.class.getName());


    private SenderWorker       senderWorker;
    private ReceptionistWorker receptionistWorker;

    private MainController     mainController;
    private Lobby              lobby;
    private MineSweeperGame    mineSweeperGame;

    /**
     *  @brief Constructor of the class.
     */
    public MineSweeperClient(){
        this.senderWorker            = null;
        this.receptionistWorker      = null;
        this.mainController          = null;
        this.lobby                   = null;
    }



    public void setMainController(MainController mainController) {
        this.mainController = mainController;
    }

    /**
     *
     * @return
     */
    public MainController getMainController() {
        return mainController;
    }


    /**
     *
     * @return
     */
    public SenderWorker getSenderWorker() {
        return senderWorker;
    }


    /**
     *
     * @param senderWorker
     */
    public void setSenderWorker(SenderWorker senderWorker) {
        this.senderWorker = senderWorker;
    }


    /**
     *
     * @return
     */
    public ReceptionistWorker getReceptionistWorker() {
        return receptionistWorker;
    }


    /**
     *
     * @param receptionistWorker
     */
    public void setReceptionistWorker(ReceptionistWorker receptionistWorker) {
        this.receptionistWorker = receptionistWorker;
    }

    public Lobby getLobby() {
        return lobby;
    }

    public void setLobby(Lobby lobby) {
        this.lobby = lobby;
    }

    public MineSweeperGame getMineSweeperGame() {
        return mineSweeperGame;
    }

    public void setMineSweeperGame(MineSweeperGame mineSweeperGame) {
        this.mineSweeperGame = mineSweeperGame;
    }

    /**
     *
     * @param serverAddress
     * @param port
     */
    public boolean connect(String serverAddress, int port){

        Socket             clientSocket;
        SenderWorker       senderWorker;
        ReceptionistWorker receptionistWorker;


        senderWorker = new SenderWorker();

        // Try to connect
        clientSocket = senderWorker.connect(serverAddress, port);

        // If connection was not possible...
        if(clientSocket == null)
            return false;


        // try to create a receptionistWorker.
        try {
            receptionistWorker = new ReceptionistWorker(clientSocket);
        }catch (NullPointerException e){
            return false;
        }


        receptionistWorker.setMineSweeperClient(this);

        this.senderWorker       = senderWorker;
        this.receptionistWorker = receptionistWorker;




        // Wait that the thread sends welcome message.
        synchronized (this.receptionistWorker){

            // start the receptionnist thread.
            this.receptionistWorker.start();

            try {
                LOG.log(Level.INFO, "Waiting for server welcome...");
                this.receptionistWorker.wait(); // ici !
            }catch (InterruptedException e){
                LOG.log(Level.SEVERE, e.getMessage(), e);
                return false;
            }
        }

        return  true;
    }

    //@Override
    public void disconnect() {

        // Check that the senderWorker is not null
        if(senderWorker == null)
            return;

        // Send the disconnect command

        if (senderWorker.disconnect() == -1) {
            System.out.println("disonnect error !");
            return;
        }

        // Waiting for the server response and the receptionnist worker to stop.
        try {
            receptionistWorker.join();
        } catch (InterruptedException e) {
            LOG.log(Level.SEVERE, e.getMessage(), e);
        }



        // cleaning up resources.
        senderWorker.cleanup();


        senderWorker       = null;
        receptionistWorker = null;
        lobby              = null;
        mineSweeperGame    = null;
    }


    //@Override
    public boolean isConnected(){
        return false;
    }


    public void joinLobby(String lobbyName, String playerName){
        senderWorker.joinLobby(lobbyName, playerName);
    }


    public void createLobby(String lobbyName, String playerName){
        senderWorker.createLobby(lobbyName, playerName);
    }

    public void openLobby(){
        senderWorker.openLobby();
    }

    public void closeLobby(){
        senderWorker.closeLobby();
    }

    public void quitLobby(){
        senderWorker.quitLobby();
    }

    public void expelLobby(String playerName){
        senderWorker.expelLobby(playerName);
    }

    public void setPlayerAmount(int playerAmount){
        senderWorker.setPlayerAmount(playerAmount);
    }

    public void setMineProportion(int proportion){
        senderWorker.setMineProportion(proportion);
    }

    public void setScoreMode(String scoreMode){
        senderWorker.setScoreMode(scoreMode);
    }

    public void enableBonusMalus(){
        senderWorker.enableBonusMalus();
    }

    public void disableBonusMalus(){
        senderWorker.disableBonusMalus();
    }

    public void startGame(){
        senderWorker.startGame();
    }

    public void sweep(int x, int y){
        senderWorker.sweep(x, y);
    }
}
