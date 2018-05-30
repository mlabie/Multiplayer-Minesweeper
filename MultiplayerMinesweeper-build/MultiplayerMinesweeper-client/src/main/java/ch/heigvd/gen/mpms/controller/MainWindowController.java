package ch.heigvd.gen.mpms.controller;


import ch.heigvd.gen.mpms.model.net.Protocol.MinesweeperProtocol;
import ch.heigvd.gen.mpms.view.MainWindowStyle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;


/**
 * @brief Controller for the Login Window.
 *
 * @author Corentin Basler, Antonio Cusanelli, Marc Labie, Simon Jobin
 */
public class MainWindowController {

    /**
     * @brief link with the MainController.
     */
    private MainController mainController;

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


    /**
     * Sets the MainController.
     *
     * @param mainController    : The MainController.
     */
    public void setMainController(MainController mainController) {
        this.mainController = mainController;
    }

    /**
     * @brief Initialize function.
     */
    public void initialize() {
        infoLabel.setText(MainWindowStyle.INFO_DEFAULT);
    }


    /**
     * @brief Set the text of the Information Label of the Login window.
     *
     * @param info  : The text to set.
     */
    public void setInfoLabel(String info) {
        infoLabel.setText(info);
    }


    /**
     * @brief Function called when the "Join Lobby" button is pressed.
     *        Creates a MineSweeper Client, and tries to connect to the
     *        server. If it successes, sends the "JOIN LOBBY" commands
     *        to the server.
     *        The fields must be completed before calling this function.
     *
     * @param actionEvent
     */
    public void joinLobbyButtonClicked(ActionEvent actionEvent) {

        String playerName;
        String lobbyName;
        String serverAddress;
        String serverPort;
        int    port;


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

        // try to connect
        if(!mainController.getMineSweeperClient().connect(serverAddress,port)){
            infoLabel.setText(MainWindowStyle.INFO_IMPOSSIBLE_CONNECTION);
            return;
        }

        mainController.getMineSweeperClient().joinLobby(lobbyName, playerName);

    }


    /**
     * @brief Function called when the "Create Lobby" button is pressed.
     *        Creates a MineSweeper Client, and tries to connect to the
     *        server. If it successes, sends the "CREATE LOBBY" commands
     *        to the server.
     *        The fields must be completed before calling this function.
     *
     * @param actionEvent
     */
    public void createLobbyButtonClicked(ActionEvent actionEvent) {

        String playerName;
        String lobbyName;
        String serverAddress;
        String serverPort;
        int    port;


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


        // try to connect
        if(!mainController.getMineSweeperClient().connect(serverAddress,port)){
            infoLabel.setText(MainWindowStyle.INFO_IMPOSSIBLE_CONNECTION);
            return;
        }

        mainController.getMineSweeperClient().createLobby(lobbyName, playerName);
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
            field.setStyle("-fx-control-inner-background: " + MainWindowStyle.FIELD_ERROR_BACKGROUND_COLOR);
    }


    /**
     * Sets the style of a field to its Default style.
     *
     * @param field : the field that needs to be set
     */
    private void setFieldDefaultBackground(TextField field){
        if(field != null)
            field.setStyle("-fx-control-inner-background: " + MainWindowStyle.FIELD_DEFAULT_BACKGROUND_COLOR);
    }


}
