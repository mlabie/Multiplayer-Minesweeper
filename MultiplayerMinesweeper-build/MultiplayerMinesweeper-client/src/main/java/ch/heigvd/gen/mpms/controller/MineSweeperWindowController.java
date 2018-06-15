package ch.heigvd.gen.mpms.controller;


import ch.heigvd.gen.mpms.model.GameComponent.Player;
import ch.heigvd.gen.mpms.model.GameComponent.Square;
import ch.heigvd.gen.mpms.view.MineSweeperWindowStyle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.*;

import java.util.ArrayList;
import java.util.Vector;

public class MineSweeperWindowController {

    private static final Object MineSweeperWindowLock   = new Object();

    private final static int MENU_HEIGHT       = 30;
    private final static int MANAGER_PANE_WITH = 300;
    private final static int MINE_FIELD_MARGIN = 20;

    private final static int SCORE_TABLE_MARGIN_TOP     = 40;
    private final static int SCORE_TABLE_INSIDE_PADDING = 10;


    private final static int PLAYER_NAME_LABEL_WIDTH  = 110;
    private final static int PLAYER_STATE_LABEL_WIDTH = 50;
    private final static int PLAYER_SCORE_LABEL_WIDTH = 80;
    private final static int SCORE_TABLE_ROW_HEIGTH   = 40;


    private MainController mainController;

    /**********           MineSweeperWindow Component           **********/
    @FXML
    private AnchorPane mineSweeperAnchorPane;

    @FXML
    private ColumnConstraints mineSweeperPaneColumn;

    @FXML
    private RowConstraints mineSweeperPaneRow;

    @FXML
    private ColumnConstraints mineFieldPaneColumn;

    @FXML
    private ColumnConstraints managerPaneColumn;

    @FXML
    private Pane mineFieldPane;

    @FXML
    private Pane scoreTablePane;

    @FXML
    private Button quitGameButton;

    @FXML
    private Label infoLabel;





    private GridPane mineFieldSquares = new GridPane();

    private GridPane scoresTable = new GridPane();


    private Button[][]  buttons;
    private boolean[][] haseFlag;

    private static int buttonSize = 32;

    private static String[] colorButtons = {"blue", "green", "orange", "grey", "red", "black"};

    private Image     mineImage, flagImage;
    private ImageView mineImageView, flagImageView;

    public void setMainController(MainController mainController) {
        this.mainController = mainController;
    }


    public void setInfoLabel(String info) {
        infoLabel.setText(info);
    }

    public void setPlayer(ArrayList<Player> players){

        // Clear the last children content.
        scoreTablePane.getChildren().remove(scoresTable);
        scoresTable.getChildren().clear();

        String playerName;

        Label playerNameLabel;
        Label playerStateLabel;
        Label playerScoreLabel;

        for(int i = 0; i < players.size(); i++){

            playerName  = players.get(i).getPlayerName();
            playerNameLabel  = new Label();
            playerStateLabel = new Label();
            playerScoreLabel = new Label();

            playerNameLabel.setId(playerName + "Name");
            playerStateLabel.setId(playerName + "State");
            playerScoreLabel.setId(playerName + "Score");

            playerNameLabel.setPrefSize(PLAYER_NAME_LABEL_WIDTH, SCORE_TABLE_ROW_HEIGTH);
            playerStateLabel.setPrefSize(PLAYER_STATE_LABEL_WIDTH, SCORE_TABLE_ROW_HEIGTH);
            playerScoreLabel.setPrefSize(PLAYER_SCORE_LABEL_WIDTH, SCORE_TABLE_ROW_HEIGTH);

            playerNameLabel.setPadding(new Insets(SCORE_TABLE_INSIDE_PADDING));
            playerStateLabel.setPadding(new Insets(SCORE_TABLE_INSIDE_PADDING));
            playerScoreLabel.setPadding(new Insets(SCORE_TABLE_INSIDE_PADDING));

            playerNameLabel.setAlignment(Pos.CENTER_LEFT);
            playerStateLabel.setAlignment(Pos.CENTER);
            playerScoreLabel.setAlignment(Pos.CENTER_RIGHT);


            playerNameLabel.setText(players.get(i).getPlayerName());
            playerScoreLabel.setText("0");


            playerNameLabel.setBackground(MineSweeperWindowStyle.PLAYER_COLOUR.get(players.get(i).getNumber()));
            playerStateLabel.setBackground(MineSweeperWindowStyle.PLAYER_COLOUR.get(players.get(i).getNumber()));
            playerScoreLabel.setBackground(MineSweeperWindowStyle.PLAYER_COLOUR.get(players.get(i).getNumber()));

            scoresTable.add(playerNameLabel, 0, i, 1, 1);
            scoresTable.add(playerStateLabel, 1, i, 1, 1);
            scoresTable.add(playerScoreLabel, 2, i, 1, 1);
        }

        scoreTablePane.getChildren().add(scoresTable);

    }

    public void setScore(Player player){

        Label score = (Label)scoresTable.lookup("#" + player.getPlayerName() + "Score");

        if(score != null){
            score.setText(String.valueOf(player.getScore()));
        }
    }

    public void setDead(Player player){

        Label state = (Label)scoresTable.lookup("#" + player.getPlayerName() + "State");

        if(state != null){
            state.setGraphic(new ImageView(mineImage));
        }
    }

    public void setField(int width, int height) {

        int newAnchorWidth;
        int newAnchorHeight;

        int newMineSweeperPaneWidth;
        int newMineSweeperPaneHeight;

        int newMineFieldPaneColumnWidth;
        int newManagerPaneColumnWidth;

        int newMineFieldPaneWidth;
        int newMineFieldPaneHeight;

        // First remove current graphics
        mineFieldPane.getChildren().remove(mineFieldSquares);
        mineFieldSquares.getChildren().clear();

        newMineFieldPaneWidth  = buttonSize * width;
        newMineFieldPaneHeight = buttonSize * height;

        newMineFieldPaneColumnWidth = newMineFieldPaneWidth + MINE_FIELD_MARGIN * 2;
            newManagerPaneColumnWidth   = MANAGER_PANE_WITH;

            newMineSweeperPaneWidth  = newMineFieldPaneColumnWidth + newManagerPaneColumnWidth;
            newMineSweeperPaneHeight = newMineFieldPaneHeight + MINE_FIELD_MARGIN * 2;

        newAnchorWidth  = newMineSweeperPaneWidth;
        newAnchorHeight = newMineSweeperPaneHeight + MENU_HEIGHT;

        // Set Window new size
        mineSweeperAnchorPane.setPrefSize(newAnchorWidth, newAnchorHeight);

        mineSweeperPaneColumn.setPrefWidth(newMineSweeperPaneWidth);
        mineSweeperPaneRow.setPrefHeight(newMineSweeperPaneHeight);

        mineFieldPaneColumn.setPrefWidth(newMineFieldPaneColumnWidth);
        managerPaneColumn.setPrefWidth(newManagerPaneColumnWidth);

        mineFieldPane.setPrefSize(newMineFieldPaneWidth, newMineFieldPaneHeight);


        //mineFieldPane.setMinSize(buttonSize * width, buttonSize * height);

        // Add buttons
        buttons  = new Button[width][height];
        haseFlag = new boolean[width][height];

        for(int i = 0; i < width; ++i) {
            for(int j = 0; j < height; ++j) {

                Button b = new Button();
                b.setPrefSize(buttonSize, buttonSize);
                buttons[i][j] = b;

                int x = i;
                int y = j;

                b.setBackground(MineSweeperWindowStyle.BUTTON_BACKGROUND);

                b.setOnMouseEntered(e -> b.setBackground(MineSweeperWindowStyle.BUTTON_HOVER_BACKGROUND));
                b.setOnMouseExited(e -> b.setBackground(MineSweeperWindowStyle.BUTTON_BACKGROUND));

                // On right button clicked, set a flag on the square.
                b.setOnMouseClicked(event ->{
                    if(event.getButton() == MouseButton.SECONDARY){
                        if(haseFlag[x][y]){
                            haseFlag[x][y] = false;
                            b.setGraphic(null);
                        }else {
                            haseFlag[x][y] = true;
                            flagImageView = new ImageView(flagImage);
                            flagImageView.setFitWidth(MineSweeperWindowStyle.MINEFIELD_ICON_SIZE);
                            flagImageView.setFitHeight(MineSweeperWindowStyle.MINEFIELD_ICON_SIZE);
                            b.setGraphic(flagImageView);
                        }
                    }
                });

                b.setOnAction(event -> {
                    if(!haseFlag[x][y]){
                        mainController.getMineSweeperClient().sweep(x,y);
                    }
                });

                mineFieldSquares.add(b, i, j, 1, 1);

            }
        }

        mineFieldPane.getChildren().add(mineFieldSquares);

    }


    public void initialize() {
        mineSweeperAnchorPane.setPrefWidth(MANAGER_PANE_WITH);
        mineSweeperAnchorPane.setPrefHeight(MENU_HEIGHT);
        mineFieldPane.setBorder(MineSweeperWindowStyle.MINEFIELD_BORDER);
        this.setInfoLabel(MineSweeperWindowStyle.INFO_DEFAULT);

        mineImage = new Image(getClass().getResourceAsStream( "/figures/mine.png"));
        flagImage = new Image(getClass().getResourceAsStream( "/figures/flag.png"));

    }

    public void refreshGame(Vector<Square> tabOfSquareSwept) {

        int    x;
        int    y;
        int    value;


        for (Square square : tabOfSquareSwept){

            x      = square.getX();
            y      = square.getY();
            value  = square.getValue();

            buttons[x][y].setOnMouseEntered(null);
            buttons[x][y].setOnMouseExited(null);

            buttons[x][y].setDisable(true);
            buttons[x][y].setOpacity(1.0);

            // if the player mistakenly put a flag on this square, we remove the flag.
            haseFlag[x][y] = false;
            buttons[x][y].setGraphic(null);

            if(value != 0){
                buttons[x][y].setText(String.valueOf(value));
                buttons[x][y].setTextFill(MineSweeperWindowStyle.VALUE_COLOUR.get(value));
            }
            buttons[x][y].setBackground(MineSweeperWindowStyle.PLAYER_COLOUR.get(mainController.getMineSweeperClient().getMineSweeperGame().getPlayer(square.getPlayerName()).getNumber()));

        }
    }

    public void showMines(Vector<Square> mines){
        int    x;
        int    y;


        for (Square square : mines){

            x             = square.getX();
            y             = square.getY();

            mineImageView = new ImageView(mineImage);

            buttons[x][y].setOnMouseEntered(null);
            buttons[x][y].setOnMouseExited(null);

            buttons[x][y].setDisable(true);
            buttons[x][y].setOpacity(1.0);


            mineImageView.setFitWidth(MineSweeperWindowStyle.MINEFIELD_ICON_SIZE);
            mineImageView.setFitHeight(MineSweeperWindowStyle.MINEFIELD_ICON_SIZE);

            buttons[x][y].setGraphic(mineImageView);
        }
    }


    public void quitGameButtonClicked(ActionEvent actionEvent) {
        synchronized (MineSweeperWindowLock){
            mainController.getMineSweeperClient().quitGame();
        }
    }

}
