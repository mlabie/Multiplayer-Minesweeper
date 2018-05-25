package ch.heigvd.gen.mpms.model.net.client;

import ch.heigvd.gen.mpms.controller.*;

import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MineSweeperClient {

    final static Logger LOG = Logger.getLogger(ReceptionistWorker.class.getName());

    private SenderWorker       senderWorker;
    private ReceptionistWorker receptionistWorker;
    Thread  receptionnistThread;

    private MainController       mainController;
    //private MainWindowController mainWindowController;

    /**
     *
     */
    public MineSweeperClient(){
        this.senderWorker            = null;
        this.receptionistWorker      = null;
        this.mainController          = null;
        //this.mainWindowController    = null;
    }


    /*public void setMainWindowController(MainWindowController mainWindowController) {
        this.mainWindowController = mainWindowController;
    }

    public MainWindowController getMainWindowController() {
        return mainWindowController;
    }*/


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

        receptionnistThread = new Thread(receptionistWorker);

        // start the receptionnist thread.
        receptionnistThread.start();

        // Wait that the thread sends welcome message.
        /*try {
            receptionistWorker.wait();
        }catch (InterruptedException e){
            LOG.log(Level.SEVERE, e.getMessage(), e);
            return false;
        }*/


        return  true;
    }

    //@Override
    public void disconnect() {
        senderWorker.disconnect();
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


    public void cleanup(){
        senderWorker.cleanup();
    }

}
