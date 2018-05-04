package controller;

import com.jfoenix.controls.JFXTabPane;
import de.jensd.fx.glyphs.GlyphIcon;
import de.jensd.fx.glyphs.GlyphsBuilder;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.SnapshotParameters;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.*;
import javafx.stage.FileChooser;
import javafx.stage.Window;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.util.Collection;
import java.util.ResourceBundle;

public class mainWindow implements Initializable {

    @FXML
    private AnchorPane mainPane;
    @FXML
    private HBox toolsBox;
    @FXML
    private Button joinLobby;
    @FXML
    private Button createLobby;

    // Buttons (in toolbar)
    private Button saveButton;
    private Button infoButton;

    private Image image;
    ImageView imageView;


    public void initialize(URL url, ResourceBundle rb){

        toolsBox.setStyle("-fx-background-color: #34495e");
        toolsBox.setSpacing(5);
        toolsBox.setPadding(new Insets(5, 5, 5, 5));

        addSaveButton();
        addInfoButton();
        addCreateLobbyButton();
        addJoinLobbyButton();

        try {

            // Add image
            image = new Image(new FileInputStream("mine.png"));
            imageView = new ImageView(image);
            imageView.setFitWidth(50);
            imageView.setFitHeight(50);
            imageView.setX(270);
            imageView.setY(60);
            mainPane.getChildren().add(imageView);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        // Add event on Enter button to get text fields
        mainPane.addEventFilter(KeyEvent.KEY_PRESSED, event -> {

        });
    }

    public void addSaveButton() {
        final GlyphIcon saveIcon = GlyphsBuilder.create(FontAwesomeIconView.class)
                .glyph(FontAwesomeIcon.SAVE)
                .size("20px")
                .build();
        saveIcon.setStyle("-fx-padding: 2");

        saveButton = new Button();
        saveButton.setGraphic(saveIcon);
        toolsBox.getChildren().add(saveButton);

        saveButton.setOnAction(event -> {
            //to do
        });
    }

    public void addInfoButton() {
        final GlyphIcon infoIcon = GlyphsBuilder.create(FontAwesomeIconView.class)
                .glyph(FontAwesomeIcon.INFO)
                .size("20px")
                .build();
        infoIcon.setStyle("-fx-padding: 2");

        infoButton = new Button();
        infoButton.setGraphic(infoIcon);
        toolsBox.getChildren().add(infoButton);

        infoButton.setOnAction(event -> {
            //to do
        });
    }

    public void addJoinLobbyButton() {

        joinLobby.setOnAction(event -> {
            //to do
            try {
                System.out.println("loading lobbyWindow");


                mainPane.getChildren().setAll((Node) FXMLLoader.load(getClass().getResource("/view/lobbyWindow.fxml")));

            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    public void addCreateLobbyButton() {
        createLobby.setOnAction(event -> {
            //to do
        });
    }
}
