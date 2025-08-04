package it.polimi.ingsw.common;

import java.util.Objects;

/**
 * Class containing useful constants
 */

public final class Constants {

    private Constants() {
    }
    public static final String PROJECT_GRADE = "30L";
    public static final double PI = 3.14159;
    public static final int MAX_CHAR = 10;
    public static final double PLANCK_CONSTANT = 6.62606896e-34;
    public static final String TITLE = "My Shelfie";
    public static final int WIDTHPANEL = 450;
    public static final int HEIGHTPANEL = 300;
    public static final String BOARD = "/Pictures/boards/livingroom.png";
    public static final String TABLE = "/FXML/Table.fxml";
    public static final String RANK = "/FXML/Ranking.fxml";
    public static final String WAITING = "/FXML/Waiting.fxml";
    public static final String NICKNAME = "/FXML/Nickname.fxml";
    public static final String SETTINGS = "/FXML/Settings.fxml";
    public static final String LOGIN = "/FXML/Login.fxml";
    public static final String CONNECTION = "/FXML/Connection.fxml";
    public static final String ICON = "/Pictures/Publisher_material/Icon_50x50px.png";
    public static final String LIBRARY_SKELETRON = "/Pictures/boards/bookshelf_orth.png";
    public static final String BACKGROUND = "/Pictures/buttons/background.jpg";
    public static final String CSS = "/css/Style.css";
    public static final String BLOCKSTYLE = "board-tiles";
    public static final String STYLE_SHEET = Objects.requireNonNull(Constants.class.getResource("/css/Style.css")).toExternalForm();
    public static final String HOSTNAME = "127.0.0.1";
    public static final int SOCKETPORT = 1234;
    public static final int RMIPORT = 1099;
    public static final int MAXLOBBYNAMELENGTH = 10;

}