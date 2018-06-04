package ch.heigvd.gen.mpms.controller;

import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.HashMap;


/**
 * @brief This class manage the window shown to the user.
 *
 * @source inspired from :
 *
 *        https://stackoverflow.com/questions/37200845/how-to-switch-scenes-in-javafx
 *
 *
 * @author Corentin Basler, Antonio Cusanelli, Marc Labie, Simon Jobin
 */
public class WindowController {

    public final static String MAIN_WINDOW           = "mainWindow";
    public final static String LOBBY_WINDOW          = "lobbyWindow";
    public final static String MINESWEEPER_WINDOW    = "mineSweeperWindow";

    private HashMap<String, Scene> windowMap;
    private Stage  primarystage;
    private String actualWindow;

    public WindowController(Stage primarystage){
        this.primarystage = primarystage;
        this.windowMap    = new HashMap<>();

        this.primarystage.setTitle("Multiplayer Minesweeper");
        this.primarystage.setResizable(false);
    }


    /**
     * @brief Get the actual showing windwow name.
     *
     * @return The actual window name
     */
    public String getActualWindow(){
        return actualWindow;
    }


    /**
     * @brief Used to add a new window to the controller.
     *
     * @param name      : The name of the new window
     * @param parent    : The parent reference of the window.
     */
    public void addWindow(String name, Parent parent){
        Scene scene = new Scene(new VBox());
        scene.setRoot(parent);
        windowMap.put(name, scene);
    }

    /**
     * @brief Remove a window of the controller.
     *
     * @param name      : The name of the window to remove.
     */
    public void remoreWindow(String name){
        windowMap.remove(name);
    }

    /**
     * @brief Displays the desired window on the screen.
     *
     * @param name  : The name of the window to show.
     */
    public void activate(String name){
        primarystage.hide();
        primarystage.setScene(windowMap.get(name));
        primarystage.show();
        actualWindow = name;
    }
}
