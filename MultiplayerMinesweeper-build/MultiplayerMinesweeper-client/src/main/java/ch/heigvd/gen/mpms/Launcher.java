package ch.heigvd.gen.mpms;

import ch.heigvd.gen.mpms.controller.*;

import ch.heigvd.gen.mpms.file.ConfigurationFileJson;
import ch.heigvd.gen.mpms.model.GameComponent.Configuration;
import ch.heigvd.gen.mpms.model.GameComponent.Player;
import ch.heigvd.gen.mpms.model.GameComponent.Square;
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

import java.util.ArrayList;
import java.util.Vector;
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

        /*ArrayList<Player> players = new ArrayList<>();
        Player player1 = new Player("Test1");
        Player player2 = new Player("Test2");

        players.add(player1);
        players.add(player2);

        mainController.getMineSweeperWindowController().setPlayer(players);

        player1.setScore(10);
        mainController.getMineSweeperWindowController().setScore(player1);*/

        /*

        Square sq1 = new Square(0,0);
        Square sq2 = new Square(0,1);
        Square sq3 = new Square(5,5);

        Vector<Square> mines = new Vector<>();
        mines.add(sq1);
        mines.add(sq2);
        mines.add(sq3);

        mainController.getMineSweeperWindowController().showMines(mines);*/

        /*mainController.getMineSweeperWindowController().setField(16,16);
        mainController.getWindowController().activate(WindowController.MINESWEEPER_WINDOW);*/




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
