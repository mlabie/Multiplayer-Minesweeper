package ch.heigvd.gen.mpms;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;


import ch.heigvd.gen.mpms.model.net.Protocol.MinesweeperProtocol;

public class MinesweeperClient extends Application{

    @Override
    public void start(Stage primaryStage) throws Exception{

        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/mainWindow.fxml"));
        Parent root = loader.load();

        // Scene
        Scene scene = new Scene(new VBox());
        ((VBox) scene.getRoot()).getChildren().addAll(root);

        // Set window title
        primaryStage.setTitle("Multiplayer Minesweeper");
        primaryStage.setResizable(false);

        // Show
        primaryStage.setScene(scene);
        primaryStage.show();
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
