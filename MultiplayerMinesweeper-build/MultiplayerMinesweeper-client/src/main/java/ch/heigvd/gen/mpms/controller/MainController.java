package ch.heigvd.gen.mpms.controller;

public class MainController {
    private MainWindowController        mainWindowController;
    private LobbyWindowController       lobbyWindowController;


    public MainController(){
        mainWindowController   = new MainWindowController();
        lobbyWindowController  = new LobbyWindowController();
    }

    public MainWindowController getMainWindowController() {
        return mainWindowController;
    }

    public LobbyWindowController getLobbyWindowController() {
        return lobbyWindowController;
    }
}
