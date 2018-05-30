package ch.heigvd.gen.mpms.model.net.Protocol;


/**
 * @brief: Set of command, status, and answer that are used in the multiplayer
 *         minesweeper protocol.
 *
 * @author Corentin Basler, Antonio Cusanelli, Marc Labie, Simon Jobin
 */
public class MinesweeperProtocol {

    public final static String VERSION   = "1.0";
    public final static int DEFAULT_PORT = 1001;

    public final static int MIN_PORT_RANGE          = 0;
    public final static int MAX_PORT_RANGE          = 65535;

    public final static String EOL                     = "\r\n";
    public final static char   CARRIAGE_RETURN         = '\r';
    public final static String DELIMITER               = " ";
    public final static String REPLY_PARAM_DELIMITER   = " : ";
    public final static String MULTIPLE_DELIMITER      = "\\b" + DELIMITER + "+";


    // - - - - - - - - - - -          Status          - - - - - - - - - - - //
    public final static int STATUS_220_I = 220;
    public final static int STATUS_250_I = 250;
    public final static int STATUS_350_I = 350;
    public final static int STATUS_450_I = 450;
    public final static int STATUS_550_I = 550;
    public final static int STATUS_650_I = 650;
    public final static int STATUS_750_I = 750;

    public final static String STATUS_220 = "220";
    public final static String STATUS_250 = "250";
    public final static String STATUS_350 = "350";
    public final static String STATUS_450 = "450";
    public final static String STATUS_550 = "550";
    public final static String STATUS_650 = "650";
    public final static String STATUS_750 = "750";


    // - - - - - - - - - - -          Command         - - - - - - - - - - - //
    public final static String CMD_CREATE_LOBBY         = "CREATE LOBBY";
    public final static String CMD_OPEN_LOBBY           = "OPEN LOBBY";
    public final static String CMD_CLOSE_LOBBY          = "CLOSE LOBBY";
    public final static String CMD_JOIN_LOBBY           = "JOIN LOBBY";
    public final static String CMD_QUIT_LOBBY           = "QUIT LOBBY";
    public final static String CMD_EXPEL_LOBBY          = "EXPEL LOBBY";
    public final static String CMD_SET_SCORE_MODE       = "SET SCORE MODE";
    public final static String CMD_SET_MINE_PROPORTION  = "SET MINE PROPORTION";
    public final static String CMD_SET_SIZE             = "SET SIZE";
    public final static String CMD_SET_PLAYER_AMOUNT    = "SET PLAYER AMOUNT";
    public final static String CMD_ENABLE_BONUS_MALUS   = "ENABLE BONUS-MALUS";
    public final static String CMD_DISABLE_BONUS_MALUS  = "DISABLE BONUS-MALUS";
    public final static String CMD_START_GAME           = "START GAME";
    public final static String CMD_SWEEP                = "SWEEP";
    public final static String CMD_QUIT_GAME            = "QUIT GAME";
    public final static String CMD_DISCONNECT           = "DISCONNECT";

    public final static String[] SUPPORTED_COMMANDS = new String[]{
            CMD_CREATE_LOBBY,
            CMD_OPEN_LOBBY,
            CMD_CLOSE_LOBBY,
            CMD_JOIN_LOBBY,
            CMD_QUIT_LOBBY,
            CMD_EXPEL_LOBBY,
            CMD_SET_SCORE_MODE,
            CMD_SET_MINE_PROPORTION,
            CMD_SET_SIZE,
            CMD_SET_PLAYER_AMOUNT,
            CMD_ENABLE_BONUS_MALUS,
            CMD_DISABLE_BONUS_MALUS,
            CMD_START_GAME,
            CMD_SWEEP,
            CMD_QUIT_GAME,
            CMD_DISCONNECT
    };

    // - - - - - - - - - - -         Parameter        - - - - - - - - - - - //
    public final static int NBR_PARAM_CREATE_LOBBY         =  2;
    public final static int NBR_PARAM_OPEN_LOBBY           =  0;
    public final static int NBR_PARAM_CLOSE_LOBBY          =  0;
    public final static int NBR_PARAM_JOIN_LOBBY           =  2;
    public final static int NBR_PARAM_QUIT_LOBBY           =  0;
    public final static int NBR_PARAM_EXPEL_LOBBY          =  1;
    public final static int NBR_PARAM_SET_SCORE_MODE       =  1;
    public final static int NBR_PARAM_SET_MINE_PROPORTION  =  1;
    public final static int NBR_PARAM_SET_SIZE             =  2;
    public final static int NBR_PARAM_SET_PLAYER_AMOUNT    =  1;
    public final static int NBR_PARAM_ENABLE_BONUS_MALUS   =  0;
    public final static int NBR_PARAM_DISABLE_BONUS_MALUS  =  0;
    public final static int NBR_PARAM_START_GAME           =  0;
    public final static int NBR_PARAM_SWEEP                =  2;
    public final static int NBR_PARAM_QUIT_GAME            =  0;
    public final static int NBR_PARAM_DISCONNECT           =  0;


    // - - - - - - - - - - -          Answer         - - - - - - - - - - - //

    public static final String WELCOME = "Welcome to the Multiplayer MineSweeper game !";
    public static final String GOODBYE = "Thank's for playing ! see you soon.";

    public final static String REPLY_OK                          = "OK";
    public final static String REPLY_ACTION_DENIED               = "ACTION DENIED";
    public final static String REPLY_LOBBY_CREATED               = "LOBBY CREATED";
    public final static String REPLY_LOBBY_JOINED                = "LOBBY JOINED";
    public final static String REPLY_LOBBY_NAME_NOT_AVAIABLE     = "LOBBY NAME NOT AVAILABLE";
    public final static String REPLY_NO_LOBBY_CREATED            = "NO LOBBY CREATED";
    public final static String REPLY_LOBBY_OPENED                = "LOBBY OPENED";
    public final static String REPLY_LOBBY_CLOSED                = "LOBBY CLOSED";
    public final static String REPLY_LOBBY_FULL                  = "LOBBY FULL";
    public final static String REPLY_ALREADY_IN_A_LOBBY          = "ALREADY IN A LOBBY";
    public final static String REPLY_LOBBY_NOT_FOUND             = "LOBBY NOT FOUND";
    public final static String REPLY_PLAYER_NAME_NOT_AVAIBALE    = "PLAYER NAME NOT AVAIABLE";
    public final static String REPLY_LOBBY_JOINED_BY             = "LOBBY JOINED BY";
    public final static String REPLY_NO_LOBBY_JOINED             = "NO LOBBY JOINED";
    public final static String REPLY_LOBBY_LEFT_BY               = "LOBBY LEFT BY";
    public final static String REPLY_YOU_HAVE_BEEN_EXPELLED      = "YOU HAVE BEEN EXPELLED";
    public final static String REPLY_PLAYER_NOT_FOUND            = "PLAYER NOT FOUND";
    public final static String REPLY_MODE_NOT_FOUND              = "MODE NOT FOUND";
    public final static String REPLY_SCORE_MODE_IS               = "SCORE MODE IS";
    public final static String REPLY_MINE_PROPORTION_NOT_ALLOWED = "MINE PROPORTION NOT ALLOWED";
    public final static String REPLY_MINE_PROPORTION_IS          = "MINE PROPORTION IS";
    public final static String REPLY_SIZE_NOT_ALLOWED            = "SIZE IS NOT ALLOWED";
    public final static String REPLY_SIZE_IS                     = "SIZE IS";
    public final static String REPLY_PLAYER_AMOUNT_NOT_ALLOWED   = "PLAYER AMOUNT NOT ALLOWED";
    public final static String REPLY_PLAYER_AMOUNT_IS            = "PLAYER AMOUNT IS";
    public final static String REPLY_BONUS_MALUS_ENABLED         = "BONUS-MALUS ENABLED";
    public final static String REPLY_BONUS_MALUS_DISABLED        = "BONUS-MALUS DISABLED";
    public final static String REPLY_GAME_STARTED                = "GAME STARTED";
    public final static String REPLY_SQUARE_ALREADY_SWEPT        = "SQUARE ALREADY SWEPT";
    public final static String REPLY_YOU_HAVE_ALREADY_LOST       = "YOU HAVE ALREADY LOST";
    public final static String REPLY_SQUARE_NOT_FOUND            = "SQUARE NOT FOUND";
    public final static String REPLY_SQUARE_SWEPT                = "SQUARE SWEPT";
    public final static String REPLY_PLAYER_SCORE                = "PLAYER SCORE";
    public final static String REPLY_NOT_IN_A_GAME               = "NOT IN A GAME";
    public final static String REPLY_PLAYER_LEFT                 = "PLAYER LEFT";
    public final static String REPLY_GAME_FINISHED               = "GAME FINISHED";
    public final static String REPLY_UNKNOWN_COMMAND             = "UNKNOWN COMMAND";
    public final static String REPLY_TOO_MANY_ARGUMENTS          = "TOO MANY ARGUMENTS";
    public final static String REPLY_NOT_ENOUGH_ARGUMENTS        = "NOT ENOUGH ARGUMENTS";

}
