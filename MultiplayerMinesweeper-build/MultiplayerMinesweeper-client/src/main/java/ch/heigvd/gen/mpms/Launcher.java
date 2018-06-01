package ch.heigvd.gen.mpms;

import ch.heigvd.gen.mpms.controller.*;

import ch.heigvd.gen.mpms.model.net.client.MineSweeperClient;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.util.logging.Level;

public class Launcher extends Application{

    private Scene               scene;
    private MineSweeperClient   mineSweeperClient;
    private MainController      mainController;


    @Override
    public void start(Stage primaryStage) throws Exception{


        FXMLLoader loader;
        Parent     parent;


        // Initialisation.
        mineSweeperClient = new MineSweeperClient();
        mainController    = new MainController(mineSweeperClient, primaryStage);


        // Loading Main Window
        loader  = new FXMLLoader(getClass().getResource( "/window/mainWindow.fxml"));
        parent  = loader.load();


        mainController.setMainWindowController((MainWindowController)loader.getController());
        mainController.getWindowController().addWindow(WindowController.MAIN_WINDOW, parent);

        mainController.getMainWindowController().setMainController(this.mainController);


        // Loading Lobby Window
        loader  = new FXMLLoader(getClass().getResource( "/window/lobbyWindow.fxml"));
        parent  = loader.load();

        mainController.setLobbyWindowController((LobbyWindowController)loader.getController());
        mainController.getWindowController().addWindow(WindowController.LOBBY_WINDOW, parent);

        mainController.getLobbyWindowController().setMainController(this.mainController);

        // Loading MineSweeper Window
        loader  = new FXMLLoader(getClass().getResource( "/window/mineSweeperWindow.fxml"));
        parent  = loader.load();

        mainController.setMineSweeperWindowController((MineSweeperWindowController) loader.getController());
        mainController.getWindowController().addWindow(WindowController.MINESWEEPER_WINDOW, parent);

        mainController.getMineSweeperWindowController().setMainController(this.mainController);


        // Setting first Window as Main Window.
        mainController.getWindowController().activate(WindowController.MAIN_WINDOW);




        primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent event){
                mineSweeperClient.disconnect();
            }
        });

    }

    public static void main(String args[]){
        launch(args);
    }
}
