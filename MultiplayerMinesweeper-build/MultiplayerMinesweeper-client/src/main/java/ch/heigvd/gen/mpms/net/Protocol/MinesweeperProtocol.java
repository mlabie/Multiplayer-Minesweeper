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

    // - - - - - - - - - - -          Answer         - - - - - - - - - - - //

    //TODO:


}