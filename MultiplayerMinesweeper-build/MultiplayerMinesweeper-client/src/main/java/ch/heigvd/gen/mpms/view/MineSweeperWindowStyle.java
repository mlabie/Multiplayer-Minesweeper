package ch.heigvd.gen.mpms.view;

import java.util.HashMap;
import java.util.Map;

import javafx.geometry.Insets;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.paint.Color;


public class MineSweeperWindowStyle {

    private static Background b1 = new Background(new BackgroundFill(Color.SKYBLUE, CornerRadii.EMPTY, Insets.EMPTY));
    private static Background b2 = new Background(new BackgroundFill(Color.LIGHTGREEN, CornerRadii.EMPTY, Insets.EMPTY));
    private static Background b3 = new Background(new BackgroundFill(Color.LIGHTSALMON, CornerRadii.EMPTY, Insets.EMPTY));
    private static Background b4 = new Background(new BackgroundFill(Color.KHAKI, CornerRadii.EMPTY, Insets.EMPTY));

    /***************        Colours       ***************/

    public final static Map<Integer, Color> VALUE_COLOUR = new HashMap<Integer, Color>();
    static {
        VALUE_COLOUR.put(0, Color.WHITE);
        VALUE_COLOUR.put(1, Color.BLUE);
        VALUE_COLOUR.put(2, Color.GREEN);
        VALUE_COLOUR.put(3, Color.RED);
        VALUE_COLOUR.put(4, Color.DARKBLUE);
        VALUE_COLOUR.put(5, Color.DARKGREEN);
        VALUE_COLOUR.put(6, Color.DARKRED);
        VALUE_COLOUR.put(7, Color.DARKGRAY);
        VALUE_COLOUR.put(8, Color.LIGHTGRAY);
    }

    public final static Map<Integer, Background> PLAYER_COLOUR = new HashMap<Integer, Background>();
    static {
        PLAYER_COLOUR.put(1, b1);
        PLAYER_COLOUR.put(2, b2);
        PLAYER_COLOUR.put(3, b3);
        PLAYER_COLOUR.put(4, b4);
    }


}
