package ch.heigvd.gen.mpms.model.net.client;

import ch.heigvd.gen.mpms.controller.*;

import java.net.Socket;
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

    /**
     *  @brief Constructor of the class.
     */
    public MineSweeperClient(){
        this.senderWorker            = null;
        this.receptionistWorker      = null;
        this.mainController          = null;
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


        // start the receptionnist thread.
        this.receptionistWorker.start();

        // Wait that the thread sends welcome message.
        synchronized (this.receptionistWorker){
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
        if(senderWorker.disconnect() == -1)
            return;

        // Waiting for the server response
        synchronized (receptionistWorker){
            try {
                receptionistWorker.wait();
            }catch (InterruptedException e){
                LOG.log(Level.SEVERE, e.getMessage(), e);
            }
        }

        // interruption of the receptionnist worker.
        receptionistWorker.interrupt();

        // cleaning up resources.
        senderWorker.cleanup();

        senderWorker       = null;
        receptionistWorker = null;

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

    public void setPlayerAmount(int playerAmount){
        senderWorker.setPlayerAmount(playerAmount);
    }
}
