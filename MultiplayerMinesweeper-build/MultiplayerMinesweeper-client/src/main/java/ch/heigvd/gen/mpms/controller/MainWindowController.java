package ch.heigvd.gen.mpms.controller;


import ch.heigvd.gen.mpms.model.net.Protocol.MinesweeperProtocol;
import ch.heigvd.gen.mpms.model.net.client.MineSweeperClient;
import ch.heigvd.gen.mpms.model.net.client.ReceptionistWorker;
import ch.heigvd.gen.mpms.model.net.client.SenderWorker;
import ch.heigvd.gen.mpms.view.MainWindowStyle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

import java.net.Socket;


public class mainWindowController {

    final static String FIELD_ERROR_BACKGROUND_COLOR        = "#FFB0B0";
    final static String FIELD_DEFAULT_BACKGROUND_COLOR      = "#FFFFFF";


    @FXML
    private TextField playerNameField;

    @FXML
    private TextField lobbyNameField;

    @FXML
    private TextField serverAddressField;

    @FXML
    private TextField serverPortField;

    @FXML
    private Label infoLabel;

    @FXML
    private Button joinLobbyButton;

    @FXML
    private Button createLobbyButton;



    public void initialize() {
        infoLabel.setText(MainWindowStyle.INFO_DEFAULT);
    }


    public void setInfoLabel(String info) {
        infoLabel.setText(info);
    }



    public void joinLobbyButtonClicked(ActionEvent actionEvent) {

        String playerName;
        String lobbyName;
        String serverAddress;
        String serverPort;
        int    port;

        Socket clientSocket;

        MineSweeperClient  mineSweeperClient;
        SenderWorker       senderWorker;
        ReceptionistWorker receptionistWorker;


        playerName    = playerNameField.getText();
        lobbyName     = lobbyNameField.getText();
        serverAddress = serverAddressField.getText();
        serverPort    = serverPortField.getText();

        // reset de info label
        infoLabel.setText(MainWindowStyle.INFO_DEFAULT);


        // Check if the field are filld
        if(!fieldAreFilld(playerName, lobbyName, serverAddress, serverPort)){
            infoLabel.setText(MainWindowStyle.INFO_EMPTY_FIELD);
            return;
        }

        // Check if the port is a positive integer.
        port = castPort(serverPort);

        if(port < 0){
            infoLabel.setText(MainWindowStyle.INFO_PORT_NOT_POSITIVE_INTEGER);
            return;
        }


        senderWorker = new SenderWorker();

        // Try to connect
        clientSocket = senderWorker.connect(serverAddress, Integer.parseInt(serverPort));

        // If connection was not possible...
        if(clientSocket == null){
            infoLabel.setText(MainWindowStyle.INFO_IMPOSSIBLE_CONNECTION);
            return;
        }

        // try to create a receptionistWorker.
        try {
            receptionistWorker = new ReceptionistWorker(clientSocket);
        }catch (NullPointerException e){
            infoLabel.setText(MainWindowStyle.INFO_IMPOSSIBLE_CONNECTION);
            return;
        }


        //create a minewSweeperClient.
        mineSweeperClient = new MineSweeperClient(senderWorker, receptionistWorker);

        mineSweeperClient.getSenderWorker().joinLobby(lobbyName, playerName);

        receptionistWorker.setMineSweeperClient(mineSweeperClient);
        //mineSweeperClient.getSenderWorker().disconnect();
    }

    public void createLobbyButtonClicked(ActionEvent actionEvent) {

    }

    /**
     * @brief Check if all the fields of the mainWindow are filld.
     *
     * @param playerName        : The playerName field content
     * @param lobbyName         : The lobbyName field content
     * @param serverAddress     : The serverAddress field content
     * @param serverPort        : The serverPort field content
     *
     * @return true if they are all filld, false if one or more is missing.
     */
    private boolean fieldAreFilld(String playerName, String lobbyName, String serverAddress, String serverPort){

        // Check if the Player Name field is filld.
        if(playerName.equals("")){
            setFieldErrorBackground(playerNameField);
            return false;
        }else {
            setFieldDefaultBackground(playerNameField);
        }

        // Check if the Lobby Name field is filld.
        if(lobbyName.equals("")){
            setFieldErrorBackground(lobbyNameField);
            return false;
        }else {
            setFieldDefaultBackground(lobbyNameField);
        }


        // Check if the Server Address field is filld.
        if(serverAddress.equals("")){
            setFieldErrorBackground(serverAddressField);
            return false;
        }else {
            setFieldDefaultBackground(serverAddressField);
        }


        // Check if the server Port field is filld.
        if(serverPort.equals("")){
            setFieldErrorBackground(serverPortField);
            return false;
        }else {
            setFieldDefaultBackground(serverPortField);
        }

        return true;
    }


    /**
     * @brief Cast the given String port into an Integer. It must be a positive Integer
     *        not bigger than the max port range.
     *
     * @param serverPort    : The server port in a String format.
     *
     * @return  -1 if the port is not a positive Integer or is bigger
     *          than the max port range, the port value else.
     */
    private int castPort(String serverPort){
        int port;

        // Check if the port is an integer.
        try {
            port = Integer.parseInt(serverPort);
        }catch (NumberFormatException e){
            return -1;
        }

        // Check if the port is a Positive integer.
        if(port < MinesweeperProtocol.MIN_PORT_RANGE)
            return -1;

        // Check if the port is a bigger than the max port range.
        if(port > MinesweeperProtocol.MAX_PORT_RANGE)
            return -1;

        return port;
    }


    /**
     * Sets the style of a field to its Error style.
     *
     * @param field : the field that needs to be set
     */
    private void setFieldErrorBackground(TextField field){
        if(field != null)
            field.setStyle("-fx-control-inner-background: " + FIELD_ERROR_BACKGROUND_COLOR);
    }


    /**
     * Sets the style of a field to its Default style.
     *
     * @param field : the field that needs to be set
     */
    private void setFieldDefaultBackground(TextField field){
        if(field != null)
            field.setStyle("-fx-control-inner-background: " + FIELD_DEFAULT_BACKGROUND_COLOR);
    }


}
