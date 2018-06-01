package ch.heigvd.gen.mpms.controller;

//import ch.heigvd.gen.mpms.Square;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
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

public class MineSweeperWindowController implements Initializable {


    private MainController mainController;

    @FXML
    private AnchorPane gamePane;

    private Button[][] buttons;

    private static String[] colorButtons = {"blue", "green", "orange", "grey", "red", "black"};

    private Image mineImage, flagImage;

    public void setMainController(MainController mainController) {
        this.mainController = mainController;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {


        int width = 20;
        int height = 20;

        buttons = new Button[width][height];

        /*
        try {
            mineImage = new Image(new FileInputStream("mine.png"));
            flagImage = new Image(new FileInputStream("flag.png"));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        */

        GridPane gridPane = new GridPane();

        for(int i = 0; i < width; ++i) {
            for(int j = 0; j < height; ++j) {

                //style des boutons
                Button b = new Button();
                b.setPrefSize(40, 40);
                buttons[i][j] = b;

                int x = i;
                int y = j;

                /*TODO
                bouton droit -> ajout ou suppression du drapreau
                 */

                //ajout de l'action sur le jeu
                b.setOnAction(event -> {
                    /*
                    if(game.board[x][y] == 9) {
                        //ImageView iv = new ImageView(mineImage);
                        BackgroundImage bImage = new BackgroundImage(mineImage, BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER, new BackgroundSize(b.getWidth(), b.getHeight(), true, true, true, false));
                        Background backGround = new Background(bImage);
                        b.setBackground(backGround);
                        //iv.setPickOnBounds(true);
                        //b.setGraphic(iv);
                    } else {
                        game.sweep(x, y);
                        refreshGame();
                    }
                    */
                });

                gridPane.add(b, i, j, 1, 1);
            }
        }


        //gridPane au centre
        gridPane.setLayoutX(50);
        gridPane.setLayoutY(50);
        gamePane.getChildren().add(gridPane);

    }

    /*public void refreshGame(Vector<Square> tabOfSquareSwept) {
        for(int i = 0; i < tabOfSquareSwept.size(); ++i) {

        }
    }*/
}
