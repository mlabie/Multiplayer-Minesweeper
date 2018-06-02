package ch.heigvd.gen.mpms.controller;

//import ch.heigvd.gen.mpms.Square;
import ch.heigvd.gen.mpms.model.GameComponent.Square;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.SplitPane;
import javafx.scene.image.Image;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.*;

import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.Vector;

public class MineSweeperWindowController {


    private MainController mainController;

    //@FXML
    //private AnchorPane gamePane;
    @FXML
    private AnchorPane mainPane;

    private GridPane gridPane = new GridPane();

    private Button[][] buttons;

    private static int buttonSize = 30;

    private static String[] colorButtons = {"blue", "green", "orange", "grey", "red", "black"};

    private Image mineImage, flagImage;

    public void setMainController(MainController mainController) {
        this.mainController = mainController;
    }

    public void setField(int width, int height) {

        // First remove current graphics
        //gridPane.getChildren().remove(buttons);

        // Set field size
        //mainPane.setMinSize(300 + buttonSize * width, 100 + buttonSize * height);

        // Add buttons
        buttons = new Button[width][height];

        for(int i = 0; i < width; ++i) {
            for(int j = 0; j < height; ++j) {

                Button b = new Button();
                b.setPrefSize(buttonSize, buttonSize);
                buttons[i][j] = b;

                int x = i;
                int y = j;

                b.setOnAction(event -> {

                });

                gridPane.add(b, i, j, 1, 1);

            }
        }

        mainPane.getChildren().add(gridPane);

    }


    public void initialize() {

        /*
        try {
            mineImage = new Image(new FileInputStream("mine.png"));
            flagImage = new Image(new FileInputStream("flag.png"));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        */
    }

    public void refreshGame(Vector<Square> tabOfSquareSwept) {
        for(int i = 0; i < tabOfSquareSwept.size(); ++i) {

        }
    }
}
