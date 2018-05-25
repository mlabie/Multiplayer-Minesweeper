package ch.heigvd.gen.mpms.controller;

import ch.heigvd.gen.mpms.model.net.client.MineSweeperClient;
import javafx.scene.Scene;

public class MainController {

    private MineSweeperClient           mineSweeperClient;

    private WindowController            windowController;
    private MainWindowController        mainWindowController;
    private LobbyWindowController       lobbyWindowController;




    public MainController(MineSweeperClient mineSweeperClient, Scene scene){
        this.mineSweeperClient      = mineSweeperClient;
        this.mainWindowController   = new MainWindowController();
        this.lobbyWindowController  = new LobbyWindowController();
        this.windowController       = new WindowController(scene);

        this.mainWindowController.setMineSweeperClient(this.mineSweeperClient);
        this.mineSweeperClient.setMainController(this);
    }


    public WindowController getWindowController() {
        return windowController;
    }

    public MainWindowController getMainWindowController() {
        return mainWindowController;
    }

    public LobbyWindowController getLobbyWindowController() {
        return lobbyWindowController;
    }
}
