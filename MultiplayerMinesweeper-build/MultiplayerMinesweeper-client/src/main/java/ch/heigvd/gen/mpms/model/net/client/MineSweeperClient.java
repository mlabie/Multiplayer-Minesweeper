package ch.heigvd.gen.mpms.model.net.client;

import ch.heigvd.gen.mpms.controller.*;
import sun.applet.Main;

import java.net.Socket;

public class MineSweeperClient {

    private SenderWorker       senderWorker;
    private ReceptionistWorker receptionistWorker;
    Thread  tmp;

    //private MainController     mainController;
    private MainWindowController mainWindowController;

    /**
     *
     */
    public MineSweeperClient(){
        this.senderWorker       = null;
        this.receptionistWorker = null;
        //mainController          = new MainController();
        this.mainWindowController    = null;
    }

    /**
     *
     * @param mainWindowController
     */
    public void setMainWindowController(MainWindowController mainWindowController) {
        this.mainWindowController = mainWindowController;
    }

    public MainWindowController getMainWindowController() {
        return mainWindowController;
    }

    /**
     *
     * @return
     */
    /*public MainController getMainController() {
        return mainController;
    }/*


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

        tmp = new Thread(receptionistWorker);

        // start the receptionnist thread.
        tmp.start();

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
