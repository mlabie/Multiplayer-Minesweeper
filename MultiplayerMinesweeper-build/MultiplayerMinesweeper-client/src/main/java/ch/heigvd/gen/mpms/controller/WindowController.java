package ch.heigvd.gen.mpms.controller;

import javafx.scene.Scene;
import javafx.scene.layout.Pane;

import java.util.HashMap;


/**
 * @source https://stackoverflow.com/questions/37200845/how-to-switch-scenes-in-javafx
 */
public class WindowController {

    public final static String MAIN_WINDOW        = "mainWindow";
    public final static String LOBBY_WINDOW       = "lobbyWindow";

    private HashMap<String, Pane> windowMap;
    Scene window;

    public WindowController(Scene window){
        this.window    = window;
        this.windowMap = new HashMap<>();
    }

    public void addWindow(String name, Pane pane){
        windowMap.put(name, pane);
    }

    public void remoreWindow(String name){
        windowMap.remove(name);
    }

    public void activate(String name){
        window.setRoot(windowMap.get(name));
    }
}
