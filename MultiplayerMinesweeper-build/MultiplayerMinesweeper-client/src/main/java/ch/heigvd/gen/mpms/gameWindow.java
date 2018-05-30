package controller;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.event.EventType;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.text.Text;
import javafx.scene.paint.Paint;
import model.Game;

import java.awt.*;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.net.URL;
import java.util.ResourceBundle;


public class gameWindow implements Initializable {

    @FXML
    private AnchorPane gamePane;

    private Game game;

    private Button[][] buttons;

    private static String[] colorButtons = {"blue", "green", "orange", "grey", "red", "black"};

    private Image mineImage, flagImage;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        /*
        b.setLayoutX(40);
        b.setLayoutY(40);
        b.setStyle("-fx-pref-height: 40px");
        b.setStyle("-fx-pref-width: 40px");
        gamePane.getChildren().add(b);
        */

        game = new Game();

        buttons = new Button[10][10];

        try {
            mineImage = new Image(new FileInputStream("mine.png"));
            flagImage = new Image(new FileInputStream("flag.png"));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        GridPane gridPane = new GridPane();
        for(int i = 0; i < 10; ++i) {
            for(int j = 0; j < 10; ++j) {

                //style des boutons
                Button b = new Button();
                b.setLayoutX(40 + 40 * i);
                b.setLayoutY(40 + 40 * j);
                b.setStyle("-fx-pref-height: 40px");
                b.setStyle("-fx-pref-width: 40px");

                buttons[i][j] = b;

                int x = i;
                int y = j;

                b.addEventFilter(MouseEvent.MOUSE_CLICKED, event -> {
                    if(event.getButton() == MouseButton.SECONDARY) {
                        BackgroundImage bImage = new BackgroundImage(flagImage, BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER, new BackgroundSize(b.getWidth(), b.getHeight(), true, true, true, false));
                        Background backGround = new Background(bImage);
                        b.setBackground(backGround);
                    }
                });

                //ajout de l'action sur le jeu
                b.setOnAction((ActionEvent event) -> {
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
                });

                gridPane.add(b, i, j, 1, 1);
            }
        }

        //gridPane au centre
        gridPane.setLayoutX(100);
        gridPane.setLayoutY(50);
        gamePane.getChildren().add(gridPane);

    }

    public void refreshGame() {
        for(int i = 0; i < 10; ++i) {
            for(int j = 0; j < 10; ++j) {
                if(game.discovered[i][j] == true) {
                    buttons[i][j].setDisable(true);
                    if(game.board[i][j] != 0) {
                        buttons[i][j].setText(String.valueOf(game.board[i][j]));
                        buttons[i][j].setTextFill(Paint.valueOf(colorButtons[game.board[i][j] - 1]));
                    }
                }
            }
        }
    }
}
