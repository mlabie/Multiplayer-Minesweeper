package ch.heigvd.gen.mpms;

import ch.heigvd.gen.mpms.controller.MainController;
import ch.heigvd.gen.mpms.controller.MainWindowController;
import ch.heigvd.gen.mpms.controller.WindowController;

import ch.heigvd.gen.mpms.model.net.client.MineSweeperClient;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

public class Launcher extends Application{

    private Scene               scene;
    private MineSweeperClient   mineSweeperClient;
    private MainController      mainController;

    @Override
    public void start(Stage primaryStage) throws Exception{


        // Initialisation.
        scene             = new Scene(new VBox());
        mineSweeperClient = new MineSweeperClient();
        mainController    = new MainController(mineSweeperClient, scene);


        // Ajout des différents fenêtre.
        mainController.getWindowController().addWindow(WindowController.MAIN_WINDOW,
                (Pane)FXMLLoader.load(getClass().getResource( "/window/mainWindow.fxml")));
        mainController.getWindowController().addWindow(WindowController.LOBBY_WINDOW,
                (Pane)FXMLLoader.load(getClass().getResource( "/window/lobbyWindow.fxml")));


        mainController.getWindowController().activate(WindowController.MAIN_WINDOW);

        // Set window title
        primaryStage.setTitle("Multiplayer Minesweeper");
        primaryStage.setResizable(false);

        // Show
        primaryStage.setScene(scene);
        primaryStage.show();


        primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent event){

            }
        });

    }

    public static void main(String args[]){
        launch(args);

        // TEST

        /*String answer1 = MinesweeperProtocol.STATUS_450 +  MinesweeperProtocol.DELIMITER + MinesweeperProtocol.REPLY_ACTION_DENIED;
        String answer2 = MinesweeperProtocol.STATUS_350 + MinesweeperProtocol.DELIMITER + MinesweeperProtocol.REPLY_SIZE_IS + MinesweeperProtocol.REPLY_PARAM_DELIMITER +MinesweeperProtocol.REPLY_PARAM_DELIMITER +  "20X20";


        String status;
        String message;
        String parameters;

        String[] splitedAnswer;

        status  = answer1.substring(0, answer1.indexOf(MinesweeperProtocol.DELIMITER));
        message = answer1.replaceFirst(status + MinesweeperProtocol.DELIMITER, "");

        splitedAnswer = message.split(MinesweeperProtocol.REPLY_PARAM_DELIMITER);

        System.out.println(splitedAnswer.length);
        System.out.println(status);

        for(String s : splitedAnswer)
            System.out.println(s);

        System.out.println();


        status  = answer2.substring(0, answer2.indexOf(MinesweeperProtocol.DELIMITER));
        message = answer2.replaceFirst(status + MinesweeperProtocol.DELIMITER, "");

        splitedAnswer = message.split(MinesweeperProtocol.REPLY_PARAM_DELIMITER);

        System.out.println(splitedAnswer.length);
        System.out.println(status);


        for(String s : splitedAnswer)
            System.out.println(s);

        System.out.println();*/


    }
}
