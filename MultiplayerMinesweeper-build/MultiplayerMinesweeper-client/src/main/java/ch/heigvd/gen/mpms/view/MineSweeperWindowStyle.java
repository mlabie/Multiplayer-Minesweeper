package ch.heigvd.gen.mpms.view;

import java.util.HashMap;
import java.util.Map;

import javafx.geometry.Insets;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;


public class MineSweeperWindowStyle {

    /**********        MineField Border        **********/

    public final static Border MINEFIELD_BORDER = new Border(new BorderStroke(Color.DIMGRAY, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT));


    /**********        Button Background       **********/

    private final static CornerRadii BUTTON_CORNER = new CornerRadii(2);

    public final static Background BUTTON_BACKGROUND       = new Background(new BackgroundFill(Color.DIMGRAY, BUTTON_CORNER, Insets.EMPTY));
    public final static Background BUTTON_HOVER_BACKGROUND = new Background(new BackgroundFill(Color.DARKGRAY, BUTTON_CORNER, Insets.EMPTY));



    /**********        Player Background       **********/

    private static Background b1 = new Background(new BackgroundFill(Color.SKYBLUE, BUTTON_CORNER, Insets.EMPTY));
    private static Background b2 = new Background(new BackgroundFill(Color.LIGHTGREEN, BUTTON_CORNER, Insets.EMPTY));
    private static Background b3 = new Background(new BackgroundFill(Color.LIGHTSALMON, BUTTON_CORNER, Insets.EMPTY));
    private static Background b4 = new Background(new BackgroundFill(Color.KHAKI, BUTTON_CORNER, Insets.EMPTY));




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


    /***************         Texts        ***************/

    public final static String INFO_DEFAULT         = "";

    public final static String INFO_WINNER          = "Winner is ";



    /***************         Sizes        ***************/

    public final static int MINEFIELD_ICON_SIZE     = 14;

}
