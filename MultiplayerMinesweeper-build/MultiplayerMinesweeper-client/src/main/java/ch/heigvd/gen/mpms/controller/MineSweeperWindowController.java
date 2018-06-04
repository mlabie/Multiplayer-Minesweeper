package ch.heigvd.gen.mpms.controller;


import ch.heigvd.gen.mpms.model.GameComponent.Player;
import ch.heigvd.gen.mpms.model.GameComponent.Square;
import ch.heigvd.gen.mpms.view.MineSweeperWindowStyle;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ToggleButton;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;

import java.util.Vector;

public class MineSweeperWindowController {


    private MainController mainController;

    //@FXML
    //private AnchorPane gamePane;
    @FXML
    //private AnchorPane mainPane;

    private GridPane gridPane = new GridPane();

    @FXML
    private Pane mineFieldPane;

    @FXML
    private Button quitGameButton;


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
                    mainController.getMineSweeperClient().sweep(x,y);
                });

                gridPane.add(b, i, j, 1, 1);

            }
        }

        mineFieldPane.getChildren().add(gridPane);

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

        int    x;
        int    y;
        int    value;


        for (Square square : tabOfSquareSwept){

            x      = square.getX();
            y      = square.getY();
            value  = square.getValue();

            buttons[x][y].setDisable(true);
            buttons[x][y].setOpacity(1.0);

            if(value != 0){
                buttons[x][y].setText(String.valueOf(value));
                buttons[x][y].setTextFill(MineSweeperWindowStyle.VALUE_COLOUR.get(value));
            }

            buttons[x][y].setBackground(MineSweeperWindowStyle.PLAYER_COLOUR.get(mainController.getMineSweeperClient().getMineSweeperGame().getPlayer(square.getPlayerName()).getNumber()));
        }

    }
}
