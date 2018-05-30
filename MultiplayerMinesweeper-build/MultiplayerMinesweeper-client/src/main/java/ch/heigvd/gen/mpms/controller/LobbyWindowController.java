package ch.heigvd.gen.mpms.controller;

import ch.heigvd.gen.mpms.model.GameComponent.Configuration;

import ch.heigvd.gen.mpms.view.LobbyWindowStyle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;


/**
 * @brief Controller for the Lobby Window.
 *
 * @author Corentin Basler, Antonio Cusanelli, Marc Labie, Simon Jobin
 */
public class LobbyWindowController {


    private static final Object lobbyWindowLock   = new Object();

    /**
     * @brief link with the MainController.
     */
    private MainController mainController;


    @FXML
    private Label lobbyNameLabel;

    @FXML
    private Label infoLabel;

    @FXML
    private ToggleButton openLobbyToggleButton;

    @FXML
    private ChoiceBox<String> customConfigSelect;

    @FXML
    private ChoiceBox<String> playerAmountSelect;

    @FXML
    private ChoiceBox<String> fieldSizeSelect;

    @FXML
    private Slider mineProportionSlider;

    @FXML
    private ChoiceBox<String> scoreModeSelect;


    @FXML
    private CheckBox bonusMalusCheckbox;

    @FXML
    private Button startGameButton;


    public void setMainController(MainController mainController) {
        this.mainController = mainController;
    }

    /**
     * @brief Initialize function.
     */
    public void initialize() {

        ObservableList<String> customConfigChoices;
        ObservableList<String> playerAmountChoices;
        ObservableList<String> fieldSizeChoices;
        ObservableList<String> scoreModeChoices;

        customConfigChoices = FXCollections.observableArrayList();
        playerAmountChoices = FXCollections.observableArrayList();
        fieldSizeChoices    = FXCollections.observableArrayList();
        scoreModeChoices    = FXCollections.observableArrayList();


        for(int iter_slot = Configuration.MIN_SLOT; iter_slot <= Configuration.MAX_SLOT; iter_slot++)
            playerAmountChoices.add(String.valueOf(iter_slot));

        fieldSizeChoices.addAll("Small", "Medium", "Big");

        // adding the Score mode choices to the items table.
        for(Configuration.ScoreMode scoreMode : Configuration.ScoreMode.class.getEnumConstants())
            scoreModeChoices.add(scoreMode.toString());


        // Adding the Choices to the lobby window.
        customConfigSelect.setItems(customConfigChoices);
        playerAmountSelect.setItems(playerAmountChoices);
        fieldSizeSelect.setItems(fieldSizeChoices);
        scoreModeSelect.setItems(scoreModeChoices);
    }


    /**
     * @brief Set the lobby window style to "Admin Mode", which means that the player
     *        has access to all the controllers of the window.
     */
    public void setAdminLobby(){
        openLobbyToggleButton.setDisable(false);
        customConfigSelect.setDisable(false);
        playerAmountSelect.setDisable(false);
        fieldSizeSelect.setDisable(false);
        mineProportionSlider.setDisable(false);
        scoreModeSelect.setDisable(false);
        bonusMalusCheckbox.setDisable(false);
        startGameButton.setDisable(false);
    }


    /**
     * @brief Set the lobby window style to "Player Mode", which means that the player
     *        doesn't have to any controllers of the window.
     */
    public void setPlayerLobby(){
        openLobbyToggleButton.setDisable(true);
        customConfigSelect.setDisable(true);
        playerAmountSelect.setDisable(true);
        fieldSizeSelect.setDisable(true);
        mineProportionSlider.setDisable(true);
        scoreModeSelect.setDisable(true);
        bonusMalusCheckbox.setDisable(true);
        startGameButton.setDisable(true);
    }

    /**
     * @brief Set the the lobby name label of the Lobby window.
     *
     * @param lobbyName  : The name of the lobby to set.
     */
    public void setLobbyNameLabel(String lobbyName) {
        lobbyNameLabel.setText(lobbyName);
    }


    /**
     * @brief Set the text of the Information Label of the Lobby window.
     *
     * @param info  : The text to set.
     */
    public void setInfoLabel(String info) {
        infoLabel.setText(info);
    }


    /**
     * @brief sets the openLobby button style to opened.
     */
    public void setOpenedLobby(){
        openLobbyToggleButton.setSelected(true);
        openLobbyToggleButton.setText(LobbyWindowStyle.OPEN_LOBBY_TOGGLE_OPENED);
        openLobbyToggleButton.setStyle("-fx-background-color: " + LobbyWindowStyle.TOGGLE_BUTTON_OPENED_BACKGROUND_COLOR);
    }


    /**
     * @brief sets the openLobby button style to closed.
     */
    public void setClosedLobby(){
        openLobbyToggleButton.setSelected(false);
        openLobbyToggleButton.setText(LobbyWindowStyle.OPEN_LOBBY_TOGGLE_CLOSED);
        openLobbyToggleButton.setStyle("-fx-background-color: " + LobbyWindowStyle.TOGGLE_BUTTON_CLOSED_BACKGROUND_COLOR);
    }


    /**
     * @brief Set the actual amount of player in the playerAmount Choice Box of the Lobby Window.
     *
     * @param amount    : The amount of player. Must not be smaller than Configuration.MIN_SLOT
     *                    and greater than Configuration.MAX_SLOT
     */
    public void setPlayerAmount(int amount){

        // sets the value only if the amount is the Configuration boundaries.
        if(amount < Configuration.MIN_SLOT || amount > Configuration.MAX_SLOT)
            return;

        playerAmountSelect.setValue(String.valueOf(amount));

    }


    /**
     * @brief Set the actual score mode in the scoreMode Choice Box of the Lobby Window.
     *
     * @param scoreMode : The score mode. Must be one of the declared Score Mode.
     */
    public void setScoreMode(String scoreMode){

        // Sets the values only if the score mode exists.
        for(Configuration.ScoreMode sm : Configuration.ScoreMode.class.getEnumConstants())
            if(sm.toString().equals(scoreMode))
                scoreModeSelect.setValue(scoreMode);

    }


    /**
     * @brief Function called when the "openLobbyToggleButton" is toggled.
     *        If its new state is toggled, opens the lobby to new players.
     *        If its new state isn't toggled, closes the lobby to new players.
     *
     * @param actionEvent
     */
    public void openLobbyToggled(ActionEvent actionEvent) {
        synchronized (lobbyWindowLock){
            if(openLobbyToggleButton.isSelected()){
                mainController.getMineSweeperClient().openLobby();
            }else {
                mainController.getMineSweeperClient().closeLobby();
            }
        }
    }

}
