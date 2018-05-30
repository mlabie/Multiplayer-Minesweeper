package ch.heigvd.gen.mpms.controller;

import ch.heigvd.gen.mpms.model.net.client.MineSweeperClient;
import javafx.stage.Stage;


/**
 * @brief The MainCtrontroller class contains all the different controller of the Multiplayer Minesweeper windows.
 *        It's used to make a link between the MineSweeperClient and the different Windows.
 *
 * @author Corentin Basler, Antonio Cusanelli, Marc Labie, Simon Jobin
 */
public class MainController {

    private MineSweeperClient           mineSweeperClient;
    private WindowController            windowController;
    private MainWindowController        mainWindowController;
    private LobbyWindowController       lobbyWindowController;



    public MainController(MineSweeperClient mineSweeperClient, Stage primarystage){
        this.mineSweeperClient      = mineSweeperClient;
        this.mainWindowController   = null;
        this.lobbyWindowController  = null;
        this.windowController       = new WindowController(primarystage);
        this.mineSweeperClient.setMainController(this);
    }

    public MineSweeperClient getMineSweeperClient() {
        return mineSweeperClient;
    }

    public WindowController getWindowController() {
        return windowController;
    }

    public void setMainWindowController(MainWindowController mainWindowController) {
        this.mainWindowController = mainWindowController;
    }

    public void setLobbyWindowController(LobbyWindowController lobbyWindowController) {
        this.lobbyWindowController = lobbyWindowController;
    }

    public MainWindowController getMainWindowController() {
        return mainWindowController;
    }

    public LobbyWindowController getLobbyWindowController() {
        return lobbyWindowController;
    }
}
