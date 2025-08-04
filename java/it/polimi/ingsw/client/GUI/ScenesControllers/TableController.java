package it.polimi.ingsw.client.GUI.ScenesControllers;

   import it.polimi.ingsw.client.CLI.CommonDescrition;
   import it.polimi.ingsw.client.GUI.GUI;
import it.polimi.ingsw.common.SecretGoals;
import it.polimi.ingsw.server.model.Block;

import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.VPos;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.scene.paint.ImagePattern;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Modality;

import java.util.*;
import java.util.concurrent.CountDownLatch;

import static it.polimi.ingsw.common.Constants.BACKGROUND;
import static it.polimi.ingsw.common.Constants.BLOCKSTYLE;

/**
 *Controller used by the GUI to interact with the user during the match.
 *@author Andrea Gollo
 */
public class TableController {
    private Set<ArrayList<Integer>> blocksToDrawCoord = new HashSet<>();
    private ArrayList<String> blocksToDrawUrl = new ArrayList<>();
    private ArrayList<String> blocksToDrawUrlCopy = new ArrayList<>();
    private ArrayList<Block> blocksToDraw = new ArrayList<>();
    private ArrayList<ImageView> achievedImageList = new ArrayList<>();
    private int playerThink = -1;  //gestisce le fasi per i pulsanti di interazione
    private int columnChosen = -1;
    private ImageView selectedImageView;  //sostituto di blockChosenUrl
    private Stage primaryStage;
    private ArrayList<Pane> otherPanels;
    private ArrayList<GridPane> otherGridPanels;
    private ArrayList<Label> otherLabelPanels;
    private Integer lockGuiController;
    private String winnerName;
    private GUI gui;


    @FXML
    private Label playerLabel1;

    @FXML
    private Label playerLabel2;

    @FXML
    private Label playerLabel3;

    @FXML
    private Label playerLabel4;

    @FXML
    private GridPane rankingGrid;

    private ArrayList<Label> listRankingPlayer;


    @FXML
    private Pane CGPanel;
    @FXML
    private Pane SGPanel;
    @FXML
    private HBox achivementsHbox;
    @FXML
    private Button arrow1;
    @FXML
    private Button arrow2;
    @FXML
    private Button arrow3;
    @FXML
    private Button arrow4;
    @FXML
    private Button arrow5;
    @FXML
    private HBox arrowHBox;
    @FXML
    private GridPane boardGrid;
    @FXML
    private ImageView boardImage;
    @FXML
    private AnchorPane boardPanel;
    @FXML
    private Pane boardPanelnto;
    @FXML
    private HBox buttonHbox;
    @FXML
    private Button confirmButton;
    @FXML
    private Button deleteButton;
    @FXML
    private ImageView endTokenImageBoard;
    @FXML
    private Pane firstToken;
    @FXML
    private ImageView imageConfirm;
    @FXML
    private ImageView imageDelete;
    @FXML
    private ImageView imageLibraryPlayer;
    @FXML
    private ImageView imageOther1;
    @FXML
    private ImageView imageOther2;
    @FXML
    private ImageView imageOther3;
    @FXML
    private Pane infoPlayerPanel;
    @FXML
    private Label labelNickname;
    @FXML
    private Label labelPoints;
    @FXML
    private GridPane libraryPlayerGrid;
    @FXML
    private Pane libraryPlayerPanel;
    @FXML
    private Pane other1Panel;
    @FXML
    private GridPane other1grid;
    @FXML
    private Label other1name;
    @FXML
    private Pane other2Panel;
    @FXML
    private GridPane other2grid;
    @FXML
    private Label other2name;
    @FXML
    private Pane other3Panel;
    @FXML
    private GridPane other3grid;
    @FXML
    private Label other3name;
    @FXML
    private Pane otherLibraryPanel;
    @FXML
    private AnchorPane otherPanel;
    @FXML
    private AnchorPane playerPanel;
    @FXML
    private AnchorPane tablePanel;

    /**
     * Board section of the table
     */
    @FXML
    public void initialize() {
        otherPanels = new ArrayList<>();
        otherLabelPanels = new ArrayList<>();
        otherGridPanels = new ArrayList<>();
        listRankingPlayer = new ArrayList<>();
        listRankingPlayer.addAll(Arrays.asList(playerLabel1, playerLabel2, playerLabel3, playerLabel4));
        otherPanels.addAll(Arrays.asList(other1Panel, other2Panel, other3Panel));
        otherLabelPanels.addAll(Arrays.asList(other1name, other2name, other3name));
        otherGridPanels.addAll(Arrays.asList(other1grid, other2grid, other3grid));

    }


    /**
     * Sets up the table scene at the start of every turn with the provided data.
     * This method is called to initialize various panels and grids based on the given parameters.
     *
     * @param name                The name of the player.
     * @param playerLibrary       The player's library as a 2D array of blocks.
     * @param playerPoints        The points scored by the player.
     * @param gameBoard           The game board as a 2D array of blocks.
     * @param secret              The secret information array.
     * @param achievedCG          The array of achieved common goals.
     * @param otherPlayersLibrary The libraries of other players as a list of 2D block arrays.
     * @param otherPlayersPoints  The points scored by other players.
     * @param otherPlayersNickName The nicknames of other players.
     * @param commonGoals         The common goals array.
     * @param achievedNum          The array of achieved goals.
     * @param upgradeBoard        A flag indicating whether the game board should be upgraded.
     *
     * @throws InterruptedException If the thread is interrupted while waiting.
     */
    public void setTable(String name, Block[][] playerLibrary, int playerPoints, Block[][] gameBoard,
                         int[] secret, int[] achievedCG, ArrayList<Block[][]> otherPlayersLibrary, int[] otherPlayersPoints,
                         String[] otherPlayersNickName, int[] commonGoals, int[] achievedNum, boolean upgradeBoard) throws InterruptedException {
        CountDownLatch countDownLatch = new CountDownLatch(1);
        if (blocksToDrawCoord.isEmpty()) {
            otherPanels = new ArrayList<>();
            otherLabelPanels = new ArrayList<>();
            otherGridPanels = new ArrayList<>();

            otherPanels.addAll(Arrays.asList(other1Panel, other2Panel, other3Panel));
            otherLabelPanels.addAll(Arrays.asList(other1name, other2name, other3name));
            otherGridPanels.addAll(Arrays.asList(other1grid, other2grid, other3grid));
            for (int i = 0; i < 3; i++) {  //sperando che gli elementi siano in ordine
                if (i + 1 > otherPlayersNickName.length) {
                    otherPanels.get(3 - i).setDisable(true);
                    otherPanels.get(3 - i).setVisible(false);
                    otherPanels.remove(3 - i);
                    otherGridPanels.remove(3 - i);
                    otherLabelPanels.remove(3 - i);
                }
            }
        }
        if(upgradeBoard) {
            Platform.runLater(() -> boardGrid.getChildren().clear());
        }
        Platform.runLater(() -> {
            System.out.println("sono setTable");
            setBoardPanel(gameBoard);
            setPlayerPanel(name, playerLibrary, playerPoints, secret, achievedCG, otherPlayersNickName.length + 1);
            setOtherPanel(otherPlayersLibrary, otherPlayersPoints, otherPlayersNickName, commonGoals, otherPlayersNickName.length + 1, achievedNum);
            interactButton(false);
            countDownLatch.countDown();
        });
        //startTurnInfo();
        countDownLatch.await();
    }

    /**
     * Sets up the board panel in the UI calling the fillBoardGrid Method with the provided game board data.
     *
     * @param gameBoard The game board represented as a 2D array of blocks.
     */
    @FXML
    public void setBoardPanel(Block[][] gameBoard) {
        System.out.println("sono setBoardPanel");
        for (Node b1 : boardGrid.getChildren()) {
            if (b1 instanceof Button) b1.setVisible(false);
        }
        fillBoardGrid(gameBoard);
        for (Node b1 : boardGrid.getChildren()) {
            if (b1 instanceof Button) b1.setDisable(true);
        }
    }

    @FXML
    public void endGamePoint(String winner) {
        firstToken.setVisible(false);
        winnerName = winner;
    }

    /**
     * Adds a block to the draw list when the corresponding button is clicked.
     *
     * @param event The action event triggered by clicking the button.
     */
    @FXML
    public void addToDraw(ActionEvent event) {
        System.out.println("sono addToDraw");
        ArrayList<Integer> coordinates = new ArrayList<>();
        Button button = (Button) event.getSource();
        coordinates.add(GridPane.getRowIndex(button));
        coordinates.add(GridPane.getColumnIndex(button));
        if (blocksToDrawCoord.contains(coordinates)) {
            button.getGraphic().setStyle("-fx-effect: null;");
            blocksToDrawUrl.remove(((ImageView) button.getGraphic()).getImage().getUrl());
            blocksToDrawCoord.remove(coordinates);
        } else if (blocksToDrawCoord.size() < 3) {
            glowImageView((ImageView) button.getGraphic());
            String url = ((ImageView) button.getGraphic()).getImage().getUrl();
            blocksToDraw.add(fromUrlToBlock(url));
            blocksToDrawUrl.add(url);
            blocksToDrawCoord.add(coordinates);
        }
    }

    /**
     * Converts a block URL to its corresponding Block enum value.
     *
     * @param url The URL of the block image.
     * @return The Block enum value corresponding to the URL, or null if no matching block is found.
     */
    private Block fromUrlToBlock(String url){
        for (Block block: Block.values()) {
            if(url.contains(block.getObject())) return block;
        }
        return null;
    }

    /**
     * Initiates the draw phase, enabling the submit button and making the board buttons usable.
     * Notifies the GUI controller to start the player's thinking process and put in wait until the press of Confirm Button.
     */
    public void drawTime() {  //accendi e rendi utilizzabile il pulsante d'invio caselle
        System.out.println("sono drawTime");
        if (blocksToDrawCoord.isEmpty()) {
            drawInfo();
        }
        synchronized (lockGuiController) {

            playerThink = 1;
            lockGuiController.notifyAll();
        }
        abortAction(new ActionEvent());
        for (Node b1 : boardGrid.getChildren()) {
            b1.setDisable(false);
            b1.getStyleClass().add(BLOCKSTYLE);
        }

        interactButton(true);
    }

    public Set<ArrayList<Integer>> getBlocksToDrawCoord() {
        return blocksToDrawCoord;
    }

    /**
     * Fills the board grid with buttons representing the blocks on the game board.
     *
     * @param gameBoard the 2D array of blocks representing the game board
     */
    public void fillBoardGrid(Block[][] gameBoard) {
        System.out.println("sono fillBoardGrid");
        //buttonBoardGrid = new Button[gameBoard.length][gameBoard[0].length];
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                boolean f = false;  //indica la presenza o meno del pulsante nel grid
                for (int k = 0; k < boardGrid.getChildren().size(); k++) {
                    Button button = (Button) boardGrid.getChildren().get(k);
                    if(gameBoard[i][j].getIndex() != -1 && GridPane.getRowIndex(button).equals(i) && GridPane.getColumnIndex(button).equals(j)) {
                        f = true;
                        button.setVisible(true);
                        break;
                    }
                }
                if (gameBoard[i][j].getIndex() != -1 && !f) {
                    Button b1 = createBoardBlock(gameBoard[i][j]);
                    b1.setVisible(true);
                    b1.setDisable(false);
                    b1.getStyleClass().add(BLOCKSTYLE);
                    b1.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
                    boardGrid.add(b1, j, i);  // columns and rows are switched
                    GridPane.setHalignment(b1, HPos.CENTER);
                    GridPane.setValignment(b1, VPos.CENTER);
                }
            }
        }
        boardGrid.setVisible(true);

    }

    /**
     * Creates a button representing a block on the game board.
     *
     * @param block the block to be represented by the button
     * @return the created button
     */
    private Button createBoardBlock(Block block) {
        Button b1 = new Button();
        b1.setPrefSize(5, 5);
        //b1.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        b1.setAlignment(Pos.CENTER);
        //b1.setMinSize(Region.USE_PREF_SIZE,Region.USE_PREF_SIZE);
        b1.setGraphic(imageViewFromBlock(block));
        b1.setOnAction(this::addToDraw);
        b1.setVisible(true);
        b1.setDisable(false);
        b1.getStyleClass().add(BLOCKSTYLE);
        b1.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        return b1;
    }

    /**
     * Creates an ImageView representing a random image of the same color as the block.
     *
     * @param block the block to be represented by the ImageView
     * @return the created ImageView
     */
    private ImageView imageViewFromBlock(Block block) {
        String tile;
        String num = String.valueOf((new Random().nextInt(3) + 1));
        switch (block) {
            case PINK -> tile = "Piante1." + num + ".png";
            case BLUE -> tile = "Cornici1." + num + ".png";
            case GREEN -> tile = "Gatti1." + num + ".png";
            case WHITE -> tile = "Libri1." + num + ".png";
            case ORANGE -> tile = "Giochi1." + num + ".png";
            case LIGHT_BLUE -> tile = "Trofei1." + num + ".png";
            default -> tile = "";
        }
        ImageView imageView = new ImageView(GUI.class.getResource("/Pictures/item_tiles/" + tile).toString());
        imageView.setFitHeight(50);
        imageView.setFitWidth(50);
        return imageView;
    }

    /**
     * Player section of the table
     */
    public void setPlayerPanel(String name, Block[][] playerlibrary, int playerPoints, int[] secret, int[] achivedCG, int numOfPlayers) {
        setLibraryPlayerGrid(playerlibrary);
        setSGPanel(secret);
        setInfoPlayerPanel(name, playerPoints, achivedCG, numOfPlayers);
    }

    //riempire la libreria
    public void setLibraryPlayerGrid(Block[][] playerlibrary) {
        Platform.runLater(() -> fillLibraryPlayerGrid(libraryPlayerGrid, playerlibrary));
    }

    /**
     * Fills the library grid of the player  with the blocks from the library matrix.
     *
     * @param libraryGrid    the GridPane representing the library grid
     * @param libraryMatrix  the matrix containing the blocks for the library
     */
    private void fillLibraryPlayerGrid(GridPane libraryGrid, Block[][] libraryMatrix) {  //usato sia per gli others sia per il player
        System.out.println("sono fillLibraryGrid");
        for (int i = 0; i < libraryMatrix.length; i++) {
            for (int j = 0; j < libraryMatrix[0].length; j++) {
                boolean f = false;
                for (Node b1 : libraryGrid.getChildren()) {
                    if(libraryMatrix[i][j].getIndex() != -1 && GridPane.getRowIndex(b1).equals(i) && GridPane.getColumnIndex(b1).equals(j)) {
                        f = true;
                        break;
                    }
                }
                if (!(libraryMatrix[i][j].equals(Block.NULL)) && !f) {
                    String typeTile;
                    switch (libraryMatrix[i][j]) {
                        case PINK -> typeTile = "Piante1";
                        case BLUE -> typeTile = "Cornici1";
                        case GREEN -> typeTile = "Gatti1";
                        case WHITE -> typeTile = "Libri1";
                        case ORANGE -> typeTile = "Giochi1";
                        case LIGHT_BLUE -> typeTile = "Trofei1";
                        default -> typeTile = "";
                    }
                    if(!blocksToDrawUrl.isEmpty()){
                        for (String tile: blocksToDrawUrl) {
                            if(tile.contains(typeTile)){
                                ImageView imageView = new ImageView(new Image(tile));
                                imageView.setFitWidth(40);
                                imageView.setFitHeight(40);
                                libraryGrid.add(imageView, j, i);  //le immagini riempiono da sole tutto lo spazio disponibile
                                GridPane.setHalignment(imageView, HPos.CENTER);
                                GridPane.setValignment(imageView, VPos.CENTER);
                            }
                        }
                    }
                    else{
                        ImageView tile = imageViewFromBlock(libraryMatrix[i][j]);
                        tile.setFitWidth(40);
                        tile.setFitHeight(40);
                        libraryGrid.add(tile, j, i);  //le immagini riempiono da sole tutto lo spazio disponibile
                        GridPane.setHalignment(tile, HPos.CENTER);
                        GridPane.setValignment(tile, VPos.CENTER);
                    }
                }
            }
        }
    }

    /**
     * Retrieves the selected block from the chosen image.
     *
     * @return the selected block
     */
    public Block getBlockChosenAsBlock() {
        System.out.println("restituisco la seguente immagine da aggiungere: " + selectedImageView.getImage().getUrl());
        Block block = fromUrlToBlock(selectedImageView.getImage().getUrl());
        if (block != null){
            blocksToDrawUrlCopy.remove(selectedImageView.getImage().getUrl());
            return block;
        }
        else{
            gui.errorPopup("null block", "error in loading the block, it's problably late, but pray");
        }
        return null;
    }

    /**
     * Handles the column selection phase.
     * Clears the selected blocks from the board and enables the column selection arrows.
     * Notifies the controller about the player's action.
     */    public void columnTime() {
        System.out.println("sono columnTime");
        //if (libraryPlayerGrid.getChildren().isEmpty()) colInfo();
        columnChosen = -1;
        //to delate blocks from the board
        for (ArrayList<Integer> coordinates : blocksToDrawCoord) {
            ObservableList<Node> children = boardGrid.getChildren();
            for (Node child : children) {
                if (child instanceof Button button && (Objects.equals(GridPane.getRowIndex(button),
                        coordinates.get(0)) && Objects.equals(GridPane.getColumnIndex(button), coordinates.get(1)))) {
                    button.setDisable(true);
                    button.setVisible(false);

                }
            }
        }

        for (Node b1 : arrowHBox.getChildren()) {
            b1.setDisable(false);
            b1.setStyle("");
        }
        interactButton(true);

        synchronized (lockGuiController) {

            playerThink = 2;
            lockGuiController.notifyAll();
        }
    }

    /**
     * Handles the column selection event triggered by clicking on an arrow button.
     * Updates the visual style of the selected arrow button.
     * Sets the chosen column index for inserting the selected blocks.
     *
     * @param event The action event triggered by clicking on an arrow button.
     */
    public void columnChose(ActionEvent event) {
        System.out.println("sono columnChose");
        //System.out.println(arrowHBox.getChildren());
        DropShadow whiteDropShadow = new DropShadow();
        whiteDropShadow.setColor(javafx.scene.paint.Color.WHITE);
        DropShadow yellowDropShadow = new DropShadow();
        yellowDropShadow.setColor(javafx.scene.paint.Color.YELLOW);
        Node selectedButton = (Node) event.getSource();
        for (Node node : arrowHBox.getChildren()) {
            if (!node.equals(selectedButton)) {
                node.setStyle("");
            } else {
                node.setStyle("-fx-effect: dropshadow(gaussian, yellow, 20, 0, 0, 0);");
            }
        }
        columnChosen = arrowHBox.getChildren().indexOf(event.getSource());
    }

    public int getColumnChosen() {
        return columnChosen;
    }

    /**
     * Sets the information in the player panel.
     *
     * @param name         The player's name.
     * @param playerPoints The player's points.
     * @param achievedCG   An array representing the achieved common goals.
     */
    public void setInfoPlayerPanel(String name, int playerPoints, int[] achievedCG, int numOfPlayers) {
        System.out.println("sono setInfoPlayerPanel");
        labelNickname.setText(name);
        labelNickname.setLayoutX(10);
        labelNickname.setMaxWidth(190);

        labelPoints.setText("Points: " + playerPoints);
        labelPoints.setLayoutX(10);
        setAchievements(achievedCG, name, numOfPlayers);
        for (Node node : arrowHBox.getChildren()) {
            node.setDisable(true);
        }
    }

    /**
     * Sets the secret goal panel with the specified secret goals.
     *
     * @param secret An array representing the secret goals.
     */
    private void setSGPanel(int[] secret) {
        System.out.println("sono setSGPanel");
        ImageView SGImage = new ImageView(new Image(GUI.class.getResource("/Pictures/personal_goal_cards/Personal_Goals" + (SecretGoals.getIndex(secret)) + ".png").toString()));
        SGImage.setPreserveRatio(true);
        SGImage.setFitWidth(100);
        SGImage.setFitHeight(150);
        SGPanel.getChildren().add(SGImage);
    }

    /**
     * Sets the achievements panel with the specified achieved color goals and current player name.
     *
     * @param achievedCG        An array representing the achieved color goals.
     * @param currentPlayerName The name of the current player.
     */
    private void setAchievements(int[] achievedCG, String currentPlayerName, int numOfPlayers) {
        achievedImageList.clear();
        achivementsHbox.getChildren().clear();
        achievedImageList.addAll(Arrays.asList(new ImageView(), new ImageView()));
        for (int i = 0; i < achievedCG.length; i++) {
            if (achievedCG[i] != -1) {
                int val = (8 - achievedCG[i]*2);
                if(numOfPlayers == 2 && achievedCG[i] == 1){
                    val = 4;
                }
                String score = "scoring_" + val;
                System.out.println(score);
                System.out.println(GUI.class.getResource("/Pictures/scoring_tokens/" + score + ".jpg").toString());
                achievedImageList.get(i).setImage(new Image(GUI.class.getResource("/Pictures/scoring_tokens/" + score + ".jpg").toString()));
                achievedImageList.get(i).setFitWidth(45);
                achievedImageList.get(i).setFitHeight(45);
                achivementsHbox.getChildren().add(achievedImageList.get(i));
            }
        }
        if (currentPlayerName.equals(winnerName)) {
            ImageView imageView = new ImageView(new Image("/Pictures/scoring_tokens/end_game.jpg"));
            imageView.setFitWidth(45);
            imageView.setFitHeight(45);
            achivementsHbox.getChildren().add(imageView);
        }
    }

    /**
     * otherPlayers section of the table
     */
    public void setOtherPanel(ArrayList<Block[][]> otherPlayersLibrary, int[] otherPlayersPoints,
                              String[] otherPlayersNickName, int[] commonGoals, int numOfPlayers, int[] achievedNum) {
        setOtherPlayers(otherPlayersLibrary, otherPlayersPoints, otherPlayersNickName);
        setCGPanel(commonGoals, numOfPlayers, achievedNum);
    }

    /**
     * Sets the library and points for other players.
     *
     * @param otherPlayersLibrary   The library of other players.
     * @param otherPlayersPoints    The points of other players.
     * @param otherPlayersNickName  The nicknames of other players.
     */
    private void setOtherPlayers(ArrayList<Block[][]> otherPlayersLibrary, int[] otherPlayersPoints,
                                 String[] otherPlayersNickName) {
        System.out.println("sono setOtherPlayers");
        for (int i = 0; i < otherGridPanels.size(); i++) {
            int finalI = i;
            Platform.runLater(() -> fillLibraryOtherGrid(otherGridPanels.get(finalI), otherPlayersLibrary.get(finalI)));
            otherLabelPanels.get(i).setText("   " + otherPlayersNickName[i] + "    Points: " + otherPlayersPoints[i]);
            otherLabelPanels.get(i).setStyle("-fx-background-color: rgba(255, 255, 255, 0.6);" +
                    " -fx-background-radius: 4px;  -fx-border-radius: 4px;");
            otherLabelPanels.get(i).setFont(Font.font("Comic Sans MS", 10));
        }
    }

    /**
     * Fills the library grid with the given library matrix.
     *
     * @param libraryGrid    The grid pane representing the library grid.
     * @param libraryMatrix  The library matrix.
     */
    private void fillLibraryOtherGrid(GridPane libraryGrid, Block[][] libraryMatrix) {  //usato sia per gli others sia per il player
        System.out.println("sono fillLibraryGrid");
        for (int i = 0; i < libraryMatrix.length; i++) {
            for (int j = 0; j < libraryMatrix[0].length; j++) {
                boolean f = false;
                for (Node b1 : libraryGrid.getChildren()) {
                    if(libraryMatrix[i][j].getIndex() != -1 && GridPane.getRowIndex(b1).equals(i) && GridPane.getColumnIndex(b1).equals(j)) {
                        f = true;
                        break;
                    }
                }
                if (!(libraryMatrix[i][j].equals(Block.NULL)) && !f) {
                    ImageView tile = imageViewFromBlock(libraryMatrix[i][j]);
                    tile.setFitWidth(23);
                    tile.setFitHeight(23);
                    libraryGrid.add(tile, j, i);  //le immagini riempiono da sole tutto lo spazio disponibile
                    GridPane.setHalignment(tile, HPos.CENTER);
                    GridPane.setValignment(tile, VPos.CENTER);
                }
            }
        }
    }

    /**
     * Sets up the common goals panel.
     *
     * @param commonGoals   The array of common goals.
     * @param numOfPlayers  The number of players.
     * @param achievedNum   The array of achieved common goals.
     */
    private void setCGPanel(int[] commonGoals, int numOfPlayers, int[] achievedNum) {
        System.out.println("sono setCGPanel");
        ArrayList<Pane> CGList = new ArrayList<>();
        for (int i = 0; i < commonGoals.length; i++) {
            CGList.add(setCommonGoal(i, commonGoals[i], numOfPlayers, achievedNum[i]));
        }
        CGPanel.getChildren().addAll(CGList);
    }

    /**
     * Creates and returns a pane for a common goal.
     *
     * @param i            The index of the common goal.
     * @param CGID         The ID of the common goal.
     * @param numOfPlayers The number of players.
     * @param achievedNum  The number of players who have achieved the common goal.
     * @return The pane representing the common goal.
     */
    private Pane setCommonGoal(int i, int CGID, int numOfPlayers, int achievedNum) {
        System.out.println("sono setCommonGoal");
        Pane commonGoal = new Pane();
        commonGoal.setPrefSize(150, 100);
        commonGoal.setMinSize(Region.USE_PREF_SIZE, Region.USE_PREF_SIZE);
        commonGoal.setLayoutX(150 * i);
        //immagine CG
        ImageView CGImage = new ImageView(new Image(GUI.class.getResource("/Pictures/common_goal_cards/" + (CGID + 1) + ".jpg").toString()));
        CGImage.setPreserveRatio(true);
        CGImage.setFitWidth(150);
        CGImage.setFitHeight(100);
        commonGoal.getChildren().add(CGImage);
        //score tokens
        System.out.println(numOfPlayers + "raggiunti: " + achievedNum);
        for (int j = (numOfPlayers - achievedNum) -1; j >= 0; j--) {
            int val = (8-2*j)-achievedNum*2;
            if(numOfPlayers == 2 && achievedNum == 1) val = 4;
            ImageView token = new ImageView(new Image(GUI.class.getResource("/Pictures/scoring_tokens/scoring_" + val + ".jpg").toString()));
            token.setPreserveRatio(true);
            token.setFitWidth(45);  //parametri per il css
            token.setFitHeight(45);
            token.setLayoutX(85);
            token.setLayoutY(22);
            token.setRotate(-8);
            commonGoal.getChildren().add(token);
        }
        return commonGoal;
    }

    /**
     * General tools
     */
    public int getPlayerThink() {
        return playerThink;
    }

    /**
     * Handles the action event when the confirm button is clicked.
     *
     * @param actionEvent The action event generated by the confirm button.
     */
    @FXML
    public void confirmAction(ActionEvent actionEvent) {
        System.out.println("sono confirmAction");
        if (playerThink == 1 && blocksToDrawCoord.size() < 4 && !blocksToDrawCoord.isEmpty()) {
            disablePane(boardGrid);
            blocksToDrawUrlCopy = new ArrayList<>(blocksToDrawUrl);
            interactButton(false);
        } else if (playerThink == 2 && columnChosen != -1) {
            disablePane(arrowHBox);
            interactButton(false);
        }
        synchronized (lockGuiController) {
            playerThink = -1;
            lockGuiController.notifyAll();
        }
        System.out.println("confirmAction finito");
    }

    /**
     * Enables or disables the buttons' interaction.
     *
     * @param on Boolean value indicating whether the interaction should be enabled (true) or disabled (false).
     */
    private void interactButton(Boolean on) {
        if (Boolean.TRUE.equals(on)) {
            confirmButton.setDisable(false);
            confirmButton.setVisible(true);
            deleteButton.setDisable(false);
            deleteButton.setVisible(true);
        } else {
            confirmButton.setDisable(true);
            confirmButton.setVisible(false);
            deleteButton.setDisable(true);
            deleteButton.setVisible(false);
        }
    }

    private void disablePane(Pane pane) {
        for (Node node : pane.getChildren()) {
            node.setDisable(true);
            node.setStyle("-fx-effect: null;");
            node.getStyleClass().add(BLOCKSTYLE);
        }
    }

    /**
     * Performs the abort action to cancel the current operation.
     *
     * @param actionEvent The event triggered by the abort action.
     */
    @FXML
    public void abortAction(ActionEvent actionEvent) {
        System.out.println("sono annulAction");
        if (playerThink == 1) {
            for (ArrayList<Integer> coordinates : blocksToDrawCoord) { //
                ObservableList<Node> children = boardGrid.getChildren();
                for (Node child : children) {
                    if (child instanceof Button button && (Objects.equals(GridPane.getRowIndex(button),
                            coordinates.get(0)) && Objects.equals(GridPane.getColumnIndex(button), coordinates.get(1)))) {
                        button.getGraphic().setStyle("-fx-effect: null;");
                    }
                }
            }
            blocksToDrawUrl.clear();
            blocksToDrawCoord.clear();
        } else if (playerThink == 2) {
            columnChosen = -1;
            for (Node node : arrowHBox.getChildren()) {
                node.setStyle("");
            }
        }
    }

    public void askBlockTime(ArrayList<Block> colors) {
        selectedImageView = null;
        synchronized (lockGuiController) {

            playerThink = 3;
            lockGuiController.notifyAll();
        }
        Platform.runLater(() -> openImageSelectionPopup(colors));
    }

    public void setPrimaryStage(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

    /**
     * Opens the image selection popup to choose tiles for a specific column.
     *
     * @param colors The list of available block colors.
     */
    public void openImageSelectionPopup(ArrayList<Block> colors) {
        System.out.println("sono openImageSelectionPopup");
        Stage popupStage = new Stage();
        popupStage.setTitle("Choose Tile");
        popupStage.initModality(Modality.APPLICATION_MODAL);
        popupStage.initOwner(primaryStage);

        VBox vBox = new VBox();
        vBox.setPrefSize(350, 190);
        vBox.setPadding(new Insets(10));
        vBox.setSpacing(10);

        Image image = new Image(BACKGROUND);
        BackgroundSize backgroundSize = new BackgroundSize(350, 250, true, true, true, false);
        BackgroundImage backgroundImage = new BackgroundImage(image, BackgroundRepeat.REPEAT,
                BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER, backgroundSize);
        Background background = new Background(backgroundImage);
        vBox.setBackground(background);

        //hobx of tiles
        HBox imageBox = new HBox(75);
        imageBox.setMaxWidth(350);
        imageBox.setAlignment(Pos.CENTER);
        imageBox.setSpacing(20);
        if (!(blocksToDrawUrlCopy.isEmpty())) {
            System.out.println(blocksToDrawUrlCopy);
            for (String blockUrl : blocksToDrawUrlCopy) {
                addImageView(blockUrl, imageBox);
            }
        }else {
            for (Block block : colors) {
                addImageView(imageViewFromBlock(block).getImage().getUrl(), imageBox);
            }
        }

        //confirmButtonPopup
        Button confirmButtonPopup = new Button("Confirm");
        confirmButtonPopup.setOnAction(event -> {
            try {
            if (selectedImageView.getImage() != null) {

                for(int i = 0; i < blocksToDrawUrl.size(); i++){
                    selectedImageView.getImage();
                }
                synchronized (lockGuiController) {

                    playerThink = -1;
                    lockGuiController.notifyAll();
                }
                popupStage.close();
            }
            }catch (Exception ignored){ }
        });

        confirmButtonPopup.setDefaultButton(true);

        //request
        Label label = new Label("Choose the tiles to add to the " + (columnChosen + 1) + " column");
        label.setPadding(new Insets(10, 0, 0, 5)); // top, right, bottom, left
        label.setFont(Font.font("Comic Sans MS", 12));
        label.setStyle("-fx-font-weight: bold;");

        vBox.getChildren().addAll(label, imageBox, confirmButtonPopup);
        Scene popupScene = new Scene(vBox, 350, 190);
        popupStage.setScene(popupScene);

        //set background
        popupStage.getScene().setFill(new ImagePattern(new Image(GUI.class.getResource(BACKGROUND).toString())));

        popupStage.setOnCloseRequest(Event::consume);

        popupStage.showAndWait();
    }

    /**
     * Adds an image view with the specified URL to the given pane.
     *
     * @param url  The URL of the image.
     * @param pane The pane to add the image view to.
     */
    private void addImageView(String url, Pane pane) {
        System.out.println("sono addImageView");
        Image image = new Image(url);
        ImageView imageBlock = new ImageView(image);
        imageBlock.setFitHeight(75);
        imageBlock.setFitWidth(75);
        imageBlock.setOnMouseClicked(event -> {
            for (Node node : pane.getChildren()) {
                if (node instanceof ImageView) node.setStyle("-fx-effect: null;");
            }
            selectedImageView = imageBlock;
            glowImageView(imageBlock);
        });
        pane.getChildren().add(imageBlock);
    }

    // Illumina l'immagine selezionata
    private void glowImageView(ImageView imageView) {
        imageView.setStyle("-fx-effect: dropshadow(gaussian, yellow, 10, 0, 0, 0);");
    }

    /**
     * Opens a popup window displaying the game ranking.
     *
     * @param nicknames The array of player nicknames.
     * @param points    The 2D array of player points.
     */
    public void openGameOverRankingPopup(String[] nicknames, int[][] points) {
        System.out.println("sono openGameOverRankingPopup");
        Stage popupStage = new Stage();
        popupStage.setTitle("Game Ranking");
        popupStage.initModality(Modality.APPLICATION_MODAL);
        popupStage.initOwner(primaryStage);
        //set background

        VBox vBox = new VBox();
        vBox.setPrefSize(500, 300);
        vBox.setPadding(new Insets(10));
        vBox.setSpacing(10);

        Image image = new Image(BACKGROUND);
        BackgroundSize backgroundSize = new BackgroundSize(500, 300, true, true, true, false);
        BackgroundImage backgroundImage = new BackgroundImage(image, BackgroundRepeat.REPEAT,
                BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER, backgroundSize);
        Background background = new Background(backgroundImage);
        vBox.setBackground(background);


        //gridPanel with name, position, points
        GridPane gridPanePoints = setGridPanePoints(nicknames.length +1, points[0].length + 2);
        gridPanePoints.setGridLinesVisible(true);
        String winnerName = new String();
        gridPanePoints.addRow(0, new Label("Name:"), new Label("Personal Goal Points:"),
                    new Label("Common Goal:"), new Label("Near Blocks:"), new Label("Final Point:"), new Label("Total:"));
        int winnerPoints = points[0][points[0].length-1];
        for (int i = 0; i < points.length; i++) {
            if(winnerPoints<=points[i][points[0].length-1]) winnerPoints = points[i][points[0].length-1];
        }
        for (int i = 0; i < nicknames.length; i++) {
            // Create labels for player name, points, and rank
            Label playerNameLabel = new Label(nicknames[i]);
            playerNameLabel.setStyle("-fx-font-family: Arial; -fx-font-size: 14px; -fx-font-weight: bold; -fx-text-fill: #FF0000;");
            if(winnerPoints == points[i][points[0].length-1]){
                playerNameLabel.setStyle("-fx-text-fill: #008000;");
                winnerName = nicknames[i];
            }
            gridPanePoints.add(playerNameLabel, 0, i+1);
            for (int j = 0; j < points[0].length; j++) {
                Label pointsValueLabel = new Label(String.valueOf(points[i][j]));
                pointsValueLabel.setStyle("-fx-font-family: Arial; -fx-font-size: 14px; -fx-font-weight: bold; -fx-text-fill: #FF0000;");
                if(winnerPoints == points[i][points[0].length-1]){
                    pointsValueLabel.setStyle("-fx-text-fill: #008000;");
                }
                gridPanePoints.add(pointsValueLabel, j + 1, i);
            }
        }
        //confirmButtonPopup
        Button confirmButtonPopup = new Button("Confirm");
        confirmButtonPopup.setOnAction(event -> {
            synchronized (lockGuiController) {
                playerThink = -1;
                lockGuiController.notifyAll();
            }
            popupStage.close();
        });
        //request
        Label label = new Label("Ranking");
        label.getStyleClass().add("-fx-font-family: Arial; -fx-font-size: 14px; -fx-font-weight: bold; -fx-text-fill: #FF0000;");
        Label label1 = new Label(winnerName);
        vBox.getChildren().addAll(label, gridPanePoints, label1, confirmButtonPopup);
        Scene popupScene = new Scene(vBox, 500, 300);
        popupStage.setScene(popupScene);
        popupStage.showAndWait();
    }

    public void rankingScene(String[] nicknames, int[][] points) {

        for (int i = 0; i < nicknames.length; i++) {
            listRankingPlayer.get(i).setText(nicknames[i]);
        }
        for (int i = 0; i < nicknames.length; i++) {
            for (int j = 0; j < points.length; j++) {
                Label label = new Label("" + points[i][j]);
                label.setStyle("-fx-font-family: Arial; -fx-font-size: 14px; -fx-font-weight: bold; ");
                rankingGrid.add(label, j+1, i+1);
            }
        }
    }




    /**
     * Sets up a GridPane for displaying points at the end of the game.
     *
     * @param nRows The number of rows in the GridPane.
     * @param nCols The number of columns in the GridPane.
     * @return The configured GridPane.
     */
    private GridPane setGridPanePoints(int nRows, int nCols) {
        System.out.println("sono setGridPanePoints");
        GridPane gridPane = new GridPane();
        //gridPanePoints.setPadding(new Insets(10));
        gridPane.setHgap(10);
        gridPane.setVgap(10);
        for (int i = 0; i < nRows; i++) {
            RowConstraints rowConstraints = new RowConstraints();
            rowConstraints.setPrefHeight(30);
            gridPane.getRowConstraints().add(rowConstraints);
        }
        ColumnConstraints colPosition = new ColumnConstraints();
        colPosition.setPercentWidth(5);
        gridPane.getColumnConstraints().add(colPosition);
        ColumnConstraints colName = new ColumnConstraints();
        colName.setPercentWidth(30);
        gridPane.getColumnConstraints().add(colName);
        for (int i = 0; i < nCols - 3; i++) {
            ColumnConstraints col = new ColumnConstraints();
            col.setPercentWidth(((double) 50 / nCols));
            gridPane.getColumnConstraints().add(col);
        }
        ColumnConstraints colTotal = new ColumnConstraints();
        colTotal.setPercentWidth(15);
        gridPane.getColumnConstraints().add(colTotal);
        return gridPane;
    }

    public void startTurnInfo() {
        gui.notificationPopup("Start turn", "It's your turn!");
    }

    public void colInfo() {
        gui.notificationPopup("Choose the column", "Select the arrow above the column");
    }

    public void drawInfo() {
        gui.notificationPopup("Turn Phase", "Select up to three Tiles and press confirm");
    }

    public void endTurn(){
        gui.notificationPopup("Turn Phase", "Your turn is Ended!");
    }
    
    public void endGamePopup(String[] nicknames, int[][] points){
        int max = 0;
        String name = null;
        for(int k = 0 ; k < nicknames.length ; k++){
            if(points[k][4] >= max){
                max = points[k][4];
                name = nicknames[k];
            }
        }
        gui.infoPopup("End Game", "The Winner Player is: " + name + " with " + max + " Points");
    }
    public ArrayList<Block> getBlocksToDraw() {
        return blocksToDraw;
    }
    public GridPane getBoardGrid(){
        return boardGrid;
    }

    public void setGUI(GUI gui) {
        this.gui = gui;
    }

    public void setLockGuiController(Integer lockGuiController) {
        this.lockGuiController = lockGuiController;
    }

}