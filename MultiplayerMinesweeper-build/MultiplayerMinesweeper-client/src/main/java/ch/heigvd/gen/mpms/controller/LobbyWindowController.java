package ch.heigvd.gen.mpms.controller;

import ch.heigvd.gen.mpms.model.GameComponent.Configuration;

import ch.heigvd.gen.mpms.model.net.Protocol.MinesweeperProtocol;
import ch.heigvd.gen.mpms.view.LobbyWindowStyle;
import javafx.beans.value.ChangeListener;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.io.IOException;


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


    private boolean isAdmin;


    @FXML
    private Label lobbyNameLabel;

    @FXML
    private ListView<String> playersListView;

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
    private Button quitLobbyButton;

    @FXML
    private Button expelLobbyButton;


    @FXML
    private Button startGameButton;

    @FXML
    private Menu fileMenu;


    @FXML
    private MenuItem saveConfigMenuItem;



    private ObservableList<String> customConfigChoices;


    public void setMainController(MainController mainController) {
        this.mainController = mainController;
    }

    /**
     * @brief Initialize function.
     */
    public void initialize() {

        ObservableList<String> playerAmountChoices;
        ObservableList<String> fieldSizeChoices;
        ObservableList<String> scoreModeChoices;

        mineProportionSlider.setMin((double)Configuration.PROPORTION_MIN);
        mineProportionSlider.setMax((double)Configuration.PROPORTION_MAX);

        customConfigChoices = FXCollections.observableArrayList();
        playerAmountChoices = FXCollections.observableArrayList();
        fieldSizeChoices    = FXCollections.observableArrayList();
        scoreModeChoices    = FXCollections.observableArrayList();



        for(int iter_slot = Configuration.MIN_SLOT; iter_slot <= Configuration.MAX_SLOT; iter_slot++)
            playerAmountChoices.add(String.valueOf(iter_slot));

        fieldSizeChoices.addAll("16" + MinesweeperProtocol.FIELD_SIZE_DELIMITER + "16",
                "24" + MinesweeperProtocol.FIELD_SIZE_DELIMITER + "16" ,
                "24" + MinesweeperProtocol.FIELD_SIZE_DELIMITER + "24",
                "32" + MinesweeperProtocol.FIELD_SIZE_DELIMITER + "24",
                "48" + MinesweeperProtocol.FIELD_SIZE_DELIMITER + "28");

        // adding the Score mode choices to the items table.
        for(Configuration.ScoreMode scoreMode : Configuration.ScoreMode.class.getEnumConstants())
            scoreModeChoices.add(scoreMode.toString());


        // Adding the Choices to the lobby window.
        playerAmountSelect.setItems(playerAmountChoices);
        fieldSizeSelect.setItems(fieldSizeChoices);
        scoreModeSelect.setItems(scoreModeChoices);



        playerAmountSelect.getSelectionModel().selectedIndexProperty().addListener(
                (ChangeListener<Number>) (observableValue, oldSelected, newSelected) ->
                        this.playerAmountSelected(playerAmountChoices.get(newSelected.intValue()))
        );


        scoreModeSelect.getSelectionModel().selectedIndexProperty().addListener(
                (ChangeListener<Number>) (observableValue, oldSelected, newSelected) ->
                        this.scoreModeSelected(scoreModeChoices.get(newSelected.intValue()))
        );

        fieldSizeSelect.getSelectionModel().selectedIndexProperty().addListener(
                (ChangeListener<Number>) (observableValue, oldSelected, newSelected) ->
                        this.fieldSizeSelected(fieldSizeChoices.get(newSelected.intValue()))
        );


        mineProportionSlider.valueProperty().addListener(
                (ChangeListener<Number>) (observableValue, oldVal, newVal) ->
                        //mineProportionSlider.setValue(newVal.intValue())
                        this.mineProportionSlided(newVal.intValue())
        );


        /*mineProportionSlider.setOnDragDone(dragEvent -> {
            this.mineProportionSlided((int)mineProportionSlider.getValue());
        });*/

        isAdmin = false;

        setInfoLabel(LobbyWindowStyle.INFO_DEFAULT);

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
        expelLobbyButton.setDisable(false);
        startGameButton.setDisable(false);

        for(MenuItem item : fileMenu.getItems())
            if(item.equals(saveConfigMenuItem))
                item.setVisible(true);


        customConfigChoices.addAll(mainController.getMineSweeperClient().getConfigurationFileJson().listOfConfigs());

        customConfigSelect.setItems(customConfigChoices);

        customConfigSelect.getSelectionModel().selectedIndexProperty().addListener(
                (ChangeListener<Number>) (observableValue, oldSelected, newSelected) ->
                        this.customConfigSelected(customConfigChoices.get(newSelected.intValue()))
                //System.out.println(playerAmountChoices.get(newSelected.intValue()))
        );

        isAdmin = true;
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
        expelLobbyButton.setDisable(true);
        startGameButton.setDisable(true);

        for(MenuItem item : fileMenu.getItems())
            if(item.equals(saveConfigMenuItem))
                item.setVisible(false);

        isAdmin = false;
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
     * Adds a player to the player listView
     *
     * @param playerName    : The name of the player
     */
    public void addPlayer(String playerName){
        playersListView.getItems().add(playerName);
    }

    /**
     * Removes a player from the listView
     *
     * @param playerName    : The name of the player
     */
    public void removePlayer(String playerName){
        playersListView.getItems().removeAll(playerName);
    }


    /**
     *
     * @param actionEvent
     */
    public void expelLobbyButtonClicked(ActionEvent actionEvent) {
        synchronized (lobbyWindowLock){
            if(isAdmin){
                ObservableList<String> players;
                players = playersListView.getSelectionModel().getSelectedItems();
                for(String player : players){
                    mainController.getMineSweeperClient().expelLobby(player);
                }
            }
        }
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
     * @brief Set the actual mine proportion of the field in the Mine Proportion slider.
     *
     * @param proportion : The mine proportion. Must not be smaller than Configuration.PROPORTION_MIN
     *                     and greater than Configuration.PROPORTION_MAX
     */
    public void setMineProportion(int proportion){
        synchronized (lobbyWindowLock){
            if(!isAdmin){
                if(proportion < Configuration.PROPORTION_MIN || proportion > Configuration.PROPORTION_MAX)
                    return;

                mineProportionSlider.setValue(proportion);
            }
        }
    }


    /**
     * @brief Set the actual score mode in the scoreMode Choice Box of the Lobby Window.
     *
     * @param scoreMode : The score mode. Must be one of the declared Score Mode.
     */
    public void setScoreMode(Configuration.ScoreMode scoreMode){
        scoreModeSelect.setValue(scoreMode.toString());
    }

    /**
     * @brief Set the actual score mode in the scoreMode Choice Box of the Lobby Window.
     *
     * @param fieldSize : The score mode. Must be one of the declared Score Mode.
     */
    public void setFieldSize(String fieldSize){
        synchronized (lobbyWindowLock){
            fieldSizeSelect.setValue(fieldSize);
        }
    }


    /**
     * @brief sets the Bonus/Malus checkbox as checked
     */
    public void enableBonusMalusCheckbox(){
        bonusMalusCheckbox.setSelected(true);
    }


    /**
     * @brief sets the Bonus/Malus checkbox as unChecked.
     */
    public void disableBonusMalusCheckbox(){
        bonusMalusCheckbox.setSelected(false);
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
            if(isAdmin){
                if(openLobbyToggleButton.isSelected()){
                    mainController.getMineSweeperClient().openLobby();
                }else {
                    mainController.getMineSweeperClient().closeLobby();
                }
            }
        }
    }

    /**
     * @brief
     *
     * @param customConfig
     */
    private void customConfigSelected(String customConfig) {

        Configuration configuration;

        synchronized (lobbyWindowLock){
            if(isAdmin){
                configuration = mainController.getMineSweeperClient().getConfigurationFileJson().selectConfig(customConfig);
                if(configuration == null)
                    return;

                mainController.getMineSweeperClient().setPlayerAmount(configuration.getNbrSlot());
                mainController.getMineSweeperClient().setFieldSize(configuration.getWidth(),configuration.getHeight());
                mainController.getMineSweeperClient().setMineProportion(configuration.getMineProportion());
                mainController.getMineSweeperClient().setScoreMode(configuration.getScore().toString());
                if(configuration.isBonus()){
                    mainController.getMineSweeperClient().enableBonusMalus();
                }else{
                    mainController.getMineSweeperClient().disableBonusMalus();
                }
            }
        }
    }

    /**
     * @brief Function called when a choice in the "playerAmountSelect" choice box is selected. .
     *        Sends a command to the server to change the player Amount.
     *
     * @param amount
     */
    private void playerAmountSelected(String amount) {
        synchronized (lobbyWindowLock){
            if(isAdmin){
                try {
                    mainController.getMineSweeperClient().setPlayerAmount(Integer.parseInt(amount));
                }catch (NumberFormatException e){
                    return;
                }
            }
        }
    }


    /**
     * @brief Function called when a choice in the "scoreModeSelect" choice box is selected. .
     *        Sends a command to the server to change the Score Mode.
     *
     * @param scoreMode
     */
    private void scoreModeSelected(String scoreMode) {
        synchronized (lobbyWindowLock){
            if(isAdmin){
                mainController.getMineSweeperClient().setScoreMode(scoreMode);
            }
        }
    }


    /**
     * @brief Function called when a choice in the "fieldSizeSelect" choice box is selected. .
     *        Sends a command to the server to change the Score Mode.
     *
     * @param fieldSize
     */
    private void fieldSizeSelected(String fieldSize) {
        String[] size;
        int width;
        int height;

        synchronized (lobbyWindowLock){
            if(isAdmin){
                size = fieldSize.split(MinesweeperProtocol.FIELD_SIZE_DELIMITER);
                if(size.length != 2){
                    return;
                }

                try {
                    width  = Integer.parseInt(size[0]);
                    height = Integer.parseInt(size[1]);
                }catch (NumberFormatException e){
                    return;
                }
                mainController.getMineSweeperClient().setFieldSize(width, height);
            }
        }
    }

    /**
     * @brief Function called when the Bonus/Malus checkbox is checked.
     *        Sends a command to the server to enable/disable the bonus and malus.
     *
     * @param actionEvent
     */
    public void bonusMalusChecked(ActionEvent actionEvent) {
        synchronized (lobbyWindowLock){
            if(isAdmin){
                if(bonusMalusCheckbox.isSelected()){
                    mainController.getMineSweeperClient().enableBonusMalus();
                }else {
                    mainController.getMineSweeperClient().disableBonusMalus();
                }
            }
        }
    }

    /**
     * @brief Function called when the Bonus/Malus checkbox is checked.
     *        Sends a command to the server to enable/disable the bonus and malus.
     *
     * @param proportion
     */
    private void mineProportionSlided(int proportion) {
        synchronized (lobbyWindowLock){
            if(isAdmin){
                if(mainController.getMineSweeperClient().getLobby().getConfig().getMineProportion() != proportion){
                    mineProportionSlider.setValue(proportion);
                    mainController.getMineSweeperClient().setMineProportion(proportion);
                }
            }
        }
    }


    /**
     * @brief Function called when the "quitLobbyButton" is clicked.
     *        Makes the player leave the lobby by sending a command to the server.
     *
     * @param actionEvent
     */
    public void quitLobbyButtonClicked(ActionEvent actionEvent) {
        synchronized (lobbyWindowLock){
            mainController.getMineSweeperClient().quitLobby();
        }
    }

    /**
     * @brief Function called when the "startGameButton" is clicked.
     *        Sends a command to the server to starts a new game.
     *        The player needs to be the admin for the client to send
     *        the command.
     *
     * @param actionEvent
     */
    public void startGameButtonClicked(ActionEvent actionEvent) {
        synchronized (lobbyWindowLock){
            if(isAdmin){
                mainController.getMineSweeperClient().startGame();
            }
        }
    }


    /**
     * @brief When the admin click on the Save Config item on the File menu,
     *        launch a popup window in which the user will be asked to give
     *        a configuration name, and saves the actual lobby configuration
     *        in the Json config file.
     *
     * @param actionEvent
     */
    public void saveConfigItemClicked(ActionEvent actionEvent){
        synchronized (lobbyWindowLock){
            if(isAdmin){

                FXMLLoader loader;
                Parent     parent;

                Stage popupStage;
                Scene popupScene;

                String configurationName;

                Configuration configuration;

                //ConfigurationPopUpController configurationPopUpController;

                try {


                    popupScene = new Scene(new VBox());
                    popupStage = new Stage();

                    loader    = new FXMLLoader(getClass().getResource( "/window/configNamePopup.fxml"));
                    parent    = loader.load();

                    popupStage.setTitle("Save Configuration");

                    popupScene.setRoot(parent);


                    popupStage.setScene(popupScene);

                    popupStage.initModality(Modality.APPLICATION_MODAL);
                    popupStage.showAndWait();

                    if(ConfigurationPopUpController.save_clicked){
                        configurationName = ConfigurationPopUpController.configurationName;
                        System.out.println(configurationName);

                        ConfigurationPopUpController.save_clicked = false;

                        configuration = mainController.getMineSweeperClient().getLobby().getConfig();

                        configuration.setName(configurationName);


                        mainController.getMineSweeperClient().getConfigurationFileJson().insertConfigurationIntoFile(configuration);
                    }


                    popupStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
                        @Override
                        public void handle(WindowEvent event){
                            ConfigurationPopUpController.save_clicked = false;
                            popupStage.close();
                        }
                    });

                } catch (IOException e) {
                    e.printStackTrace();
                }


            }
        }
    }


    /**
     * Clears the lobby window.
     */
    public void cleanUp() {
        synchronized (lobbyWindowLock){
            playersListView.getItems().clear();
        }
    }

}
