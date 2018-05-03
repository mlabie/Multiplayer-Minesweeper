package ch.heigvd.gen.mpms.net.Protocol;

/**
 * @brief: Set of command, status, and answer that are used in the multiplayer
 *         minesweeper protocol.
 *
 * @author Corentin Basler, Antonio Cusanelli, Marc Labie, Simon Jobin
 */
public class MinesweeperProtocol {

    public final static String VERSION   = "1.0";
    public final static int DEFAULT_PORT = 1001;
    public final static String EOL       = "\r\n";

    // - - - - - - - - - - -          Status          - - - - - - - - - - - //
    public final static String STATUS_220 = "220";
    public final static String STATUS_250 = "250";
    public final static String STATUS_350 = "350";
    public final static String STATUS_450 = "450";
    public final static String STATUS_550 = "550";
    public final static String STATUS_650 = "650";
    public final static String STATUS_750 = "750";


    // - - - - - - - - - - -          Command         - - - - - - - - - - - //
    public final static String CMD_CREATE_LOBBY         = "CREATE LOBBY";
    public final static String CMD_OPEN_LOBBY           = "OPEN LOOBY";
    public final static String CMD_CLOSE_LOBBY          = "CLOSE LOBBY";
    public final static String CMD_JOIN_LOBBY           = "JOIN LOOBY";
    public final static String CMD_QUIT_LOBBY           = "QUIT LOBBY";
    public final static String CMD_EXPEL_LOBBY          = "EXPEL LOBBY";
    public final static String CMD_SET_SCORE_MODE       = "SET SCORE MODE";
    public final static String CMD_SET_MINE_AMOUNT      = "SET MINE AMOUNT";
    public final static String CMD_SET_SIZE             = "SET SIZE";
    public final static String CMD_SET_PLAYER_AMOUNT    = "SET PLAYER AMOUNT";
    public final static String CMD_ENABLE_BONUS_MALUS   = "ENABLE BONUS-MALUS";
    public final static String CMD_DISABLE_BONUS_MALUS  = "DISABLE BONUS-MALUS";
    public final static String CMD_START_GAME           = "START GAME";
    public final static String CMD_SWEEP                = "SWEEP";
    public final static String CMD_QUIT_GAME            = "QUIT GAME";
    public final static String CMD_DISCONNECT           = "DISCONNECT";

    // - - - - - - - - - - -          Answer         - - - - - - - - - - - //
    public final static String REPLY_OK                         = "OK";
    public final static String REPLY_ACTION_DENIED              = "ACTION DENIED";
    public final static String REPLY_LOBBY_CREATE               = "LOBBY CREATE";
    public final static String REPLY_LOBBY_NAME_NOT_AVAIABLE    = "LOBBY NAME NOT AVAILABLE";
    public final static String REPLY_NO_LOBBY_CREATED           = "NO LOBBY CREATED";
    public final static String REPLY_LOBBY_OPENED               = "LOBBY OPENED";
    public final static String REPLY_LOBBY_CLOSED               = "LOBBY CLOSED";
    public final static String REPLY_LOBBY_FULL                 = "LOBBY FULL";
    public final static String REPLY_LOBBY_NOT_FOUND            = "LOBBY NOT FOUND";
    public final static String REPLY_PLAYER_NAME_NOT_AVAIBALE   = "PLAYER NAME NOT AVAIABLE";
    public final static String REPLY_LOBBY_JOINED_BY            = "LOBBY JOINED BY";
    public final static String REPLY_NO_LOBBY_JOINED            = "NO LOBBY JOINED";
    public final static String REPLY_LOBBY_LEFT_BY              = "LOBBY LEFT BY";
    public final static String REPLY_PLAYER_NOT_FOUND           = "PLAYER NOT FOUND";
    public final static String REPLY_YOU_HAVE_BEEN_EXPELED      = "YOU HAVE BEEN EXPELED";
    public final static String REPLY_MODE_NOT_FOUND             = "MODE NOT FOUND";
    public final static String REPLY_SCORE_MODE_IS              = "SCORE MODE IS";
    public final static String REPLY_MINE_AMOUNT_NOT_ALLOWED    = "MINE AMOUNT NOT ALLOWED";
    public final static String REPLY_MINE_AMOUNT_IS             = "MINE AMOUNT IS";
    public final static String REPLY_SIZE_NOT_ALLOWED           = "SIZE IS NOT ALLOWED";
    public final static String REPLY_SIZE_IS                    = "SIZE IS";
    public final static String REPLY_PLAYER_AMOUNT_NOT_ALLOWED  = "PLAYER AMOUNT NOT ALLOWED";
    public final static String REPLY_PLAYER_AMOUNT_IS           = "PLAYER AMOUNT IS";
    public final static String REPLY_BONUS_MALUS_ENABLED        = "BONUS-MALUS ENABLED";
    public final static String REPLY_BONUS_MALUS_DISABLED       = "BONUS-MALUS DISABLED";
    public final static String REPLY_GAME_STARTED               = "GAME STARTED";
    public final static String REPLY_SQUARE_ALREADY_SWEPT       = "SQUARE ALREADY SWEPT";
    public final static String REPLY_YOU_HAVE_ALREADY_LOST      = "YOU HAVE ALREADY LOST";
    public final static String REPLY_SQUARE_NOT_FOUND           = "SQUARE NOT FOUND";
    public final static String REPLY_SQUARE_SWEPT               = "SQUARE SWEPT";
    public final static String REPLY_PLAYER_SCORE               = "PLAYER SCORE";
    public final static String REPLY_NOT_IN_A_GAME              = "NOT IN A GAME";
    public final static String REPLY_PLAYER_LEFT                = "PLAYER LEFT";
    public final static String REPLY_GAME_FINISHED              = "GAME FINISHED";
    public final static String REPLY_UNKNOWN_COMMAND            = "UNKNOWN COMMAND";

}
