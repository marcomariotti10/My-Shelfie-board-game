package it.polimi.ingsw.client.CLI;

import it.polimi.ingsw.client.Controller;
import it.polimi.ingsw.client.MessageHandler;
import it.polimi.ingsw.exceptions.*;
import it.polimi.ingsw.messages.toClient.*;

import it.polimi.ingsw.messages.toServer.*;
import it.polimi.ingsw.server.ClientHandlerRMIInterface;
import it.polimi.ingsw.server.model.Block;

import java.io.*;
import java.util.*;

import static it.polimi.ingsw.common.Constants.MAXLOBBYNAMELENGTH;

/**
 *The CLIcontroller class implements the Controller interface and is responsible for handling the user interaction
 *and input in a command-line interface (CLI) environment.
 * @author Gabriele Marra, Francesco Foresti, Andrea Gollo, Marco Mariotti
 */
public class CLIcontroller implements Controller, Serializable {
    private MessageHandler messageHandler;
    private final ObjectOutputStream os = null;

    public CLIcontroller() {
    }

    /**
     *Constructs a CLIcontroller object with the specified serverClientHandler.
     *@param serverClientHandler the serverClientHandler for RMI communication
     */
    public CLIcontroller(ClientHandlerRMIInterface serverClientHandler) {//per la rmi
        this.messageHandler = new MessageHandler(serverClientHandler);
    }

    /**
     *Handles the AskBlockMessage received from the server. Prompts the user to enter the color of the block they wish to insert in the library.
     *@param message the AskBlockMessage to be handled
     */
    @Override
    public void handleAskBlockMessage(AskBlockMessage message) {
        final ArrayList<Block> colors = message.getColors();
        try {
            System.out.println("Digit the colour of the block you wish to insert");
            Integer blockIndex = Integer.parseInt(getInput());
            if (colors.contains(Block.values()[blockIndex])) {
                messageHandler.sendMessageToServer(new SetBlockMessage(Block.values()[blockIndex]));
            } else throw new IllegalArgumentException();
        } catch (IllegalArgumentException | ArrayIndexOutOfBoundsException e) {
            System.err.println("Input error, please enter a number contained in the list");
            handleAskBlockMessage(message);
        }
    }

    /**
     *Handles the ClientErrorMessage received from the server. Prints the error message.
     *@param message the ClientErrorMessage to be handled
     */
    @Override
    public void handleClientErrorMessage(ClientErrorMessage message) {
        System.err.println("Error : " + message.getMessage());
    }

    @Override
    public void handleClientExitMessage(ClientExitMessage message) {  //when server kick out player
        System.out.println("Your connection is closed");
    }

    /**
     *Handles the ColumnInsertionMessage received from the server. Prompts the user to enter the number of the column they wish to insert the blocks in.
     *@param message the ColumnInsertionMessage to be handled
     */
    @Override
    public void handleColumnInsertionMessage(ColumnInsertionMessage message) {
        String column;
        try {
            System.out.println("Digit the number of the column you wish to insert the blocks in");
            column = getInput();
            if (!column.equals("0") && !column.equals("1") && !column.equals("2") && !column.equals("3") && !column.equals("4"))
                throw new NotExistingColumnException();
            messageHandler.sendMessageToServer(new SetColumnMessage(Integer.parseInt(column), message.getSize()));
        } catch (NotExistingColumnException e) {
            System.err.println("Digit a number between 0 and 4");
            handleColumnInsertionMessage(message);
        }
    }

    /**
     *Handles the DrawBlockRequest received from the server. Prompts the user to enter the coordinates of the tiles they want to collect.
     *@param message the DrawBlockRequest to be handled
     */
    @Override
    public void handleDrawBlockRequest(DrawBlockMessage message) {
        boolean correctInput = false;
        while (!correctInput){
            System.out.println("Enter the coordinates of the tiles you want to collect: ");
            System.out.println("(for example: '1,3 1,4 1,5' o '9,1 9,2')");
            String[] arrayCoupleCoordinates = getInput().split(" ");
            ArrayList<Integer> listCoordinates = new ArrayList<>();
            Set<ArrayList<Integer>> positions = new HashSet<>();
            try{
                for (String coupleCoordinates : arrayCoupleCoordinates) {
                    listCoordinates.addAll(parseXYCoord(coupleCoordinates));
                }
                for (int i = 0; i < (listCoordinates.size() - 1); i = i + 2) {
                    positions.add(new ArrayList<>(Arrays.asList(listCoordinates.get(i), listCoordinates.get(i + 1))));
                }
                messageHandler.sendMessageToServer(new DrawBlocksMessage(positions));
                correctInput = true;
            } catch (Exception e){
                System.err.println(e.getMessage());
            }
            //TODO: controller che gestisce l'input da tastiera e lo rielabora qui e non nel server
        }
    }

    /**
     *Parses the X and Y coordinates from the input string using the method parseMaster(String master) .
     *@param s the input string containing the coordinates
     *@return the list of parsed coordinates
     */
    private ArrayList<Integer> parseXYCoord(String s) {
        String master = s;
        if (s.contains(",")) {
            master = master.replace("(", "").replace(")", "");
            master = master.replace(",", " ");
        } else if (s.contains(".")) {
            master = master.replace(".", " ");
        } else throw new IllegalArgumentException("separation of the coordinate is incorrect!");
        return parseMaster(master);
    }

    /**
     *Parses the master string into X and Y coordinates.
     *@param master the master string containing the coordinates
     *@return the list of parsed coordinates
     */
    private ArrayList<Integer> parseMaster(String master) {
        if (!master.matches("^[0-8] [0-8]$"))
            throw new IllegalArgumentException("Inputted Number is incorrect!");
        String[] splitMaster = master.split(" ");
        ArrayList<Integer> coordinates = new ArrayList<>();
        coordinates.add(Integer.parseInt(splitMaster[0]));
        coordinates.add(Integer.parseInt(splitMaster[1]));
        return coordinates;

    }

    @Override
    public void handleEndGameMessage(EndGameMessage message) {  //notifica l'inizio dell'ultimo giro perchè qualcuno ha completato la libreria
        System.out.println(message.getString());
    }

    /**
     *Handles the EnterNicknameMessage by prompting the user to insert their name.
     *@param message the EnterNicknameMessage received
     */
    @Override
    public void handleEnterNicknameMessage(EnterNicknameMessage message) {
        System.out.println("Insert your name");
        boolean canParse = false;
        String name = null;
        while (!canParse) {
            name = getInput();
            if (name.length() > 10)
                System.err.println("The name is too long, please choose a name with 10 or less characters");
            else if (name.isEmpty() || name.isBlank())
                System.err.println("The name is empty, please select at least one character");
                else
                    canParse = true;
        }
        messageHandler.sendMessageToServer(new SetNicknameMessage(name));
        }

    /**
     *Handles the GameSettingsMessage by prompting the user to enter the number of players and common goals for the match.
     *@param message the GameSettingsMessage received
     */
    @Override
    public void handleGameSettingsMessage(GameSettingsMessage message) {
        boolean canParse;
        int numberOfPlayers = 0;
        int numberOfCommonGoals = 0;

        while (numberOfPlayers < 2) {
            canParse = false;
            while (!canParse) {
                System.out.println("Insert the number of players for the match: " + message.getLobbyName());
                try {
                    int temp = Integer.parseInt(getInput());
                    if (temp >= 2 && temp <= 4) {
                        numberOfPlayers = temp;
                    } else {
                        System.err.println(temp + " it's not acceptable, put a value between 2 and 4");
                    }
                    canParse = true;
                } catch (NumberFormatException e) {
                    System.err.println("Your input is not valid");
                }
            }
        }

        while (numberOfCommonGoals < 1) {
            canParse = false;
            while (!canParse) {
                System.out.println("Insert the number of common goals for the match: " + message.getLobbyName());
                try {
                    int temp = Integer.parseInt(getInput());
                    if (temp >= 1 && temp <= 2) {
                        numberOfCommonGoals = temp;
                    } else {
                        System.err.println(temp + " it's not acceptable, put a value between 1 and 2");
                    }
                    canParse = true;
                } catch (NumberFormatException e) {
                    System.err.println("Your input is not valid");
                }
            }
        }

        messageHandler.sendMessageToServer(new SetLobbySettingMessage(message.getLobbyName(), numberOfPlayers, numberOfCommonGoals));
    }

    /**
     *Handles the login message received from the server.
     *Prompts the user to choose whether to create a new lobby or join an existing one.
     *If the user chooses to create a new lobby, it sends a createLobby message to the server.
     *If the user chooses to join an existing lobby, it sends an accessLobby message to the server.
     *@param message The login message received from the server.
     */
    @Override
    public void handleLoginMessage(LoginMessage message) {  //TODO: CONTROLLER PER TUTTI I TIPI DI INPUT, ANCHE SE NON E PRESENTE ALCUNA PARTITA
        boolean validChoice = false;
        boolean isCreatingNewLobby = false;
        while (!validChoice) {
            System.out.println("Do you want to create new lobby? (Y/N)");
            String input = getInput();
            if (input == null || input.isEmpty() || input.isBlank()) {
                System.err.println("Choice cannot be null");
            } else if (input.equals("Y") || input.equals("y")) {
                validChoice = true;
                isCreatingNewLobby = true;
            } else if (input.equals("N") || input.equals("n")) {
                validChoice = true;
            } else {
                System.err.println("Not valid choice");
            }
        }

        String lobbyName = null;
        while (lobbyName == null) {
            System.out.println("Enter lobby name");
            String input = getInput();
            if(input == null || input.isEmpty() || input.isBlank()){
                System.err.println("Lobby name not valid, cannot be empty");
            } else if (input.length() <= MAXLOBBYNAMELENGTH) {
                lobbyName = input;
            } else {
                System.err.println("Lobby name not valid, max length is " + MAXLOBBYNAMELENGTH);
            }
        }

        if (isCreatingNewLobby) {
            messageHandler.sendMessageToServer(new CreateLobbyMessage(lobbyName));
        } else {
            messageHandler.sendMessageToServer(new AccessLobbyMessage(lobbyName));
        }
    }

    @Override
    public void handlePingMessage(PingMessage message) { }

    /**
     *Handles the StampEndGameMessage and displays the end game information, including points and player nicknames.
     *@param message the StampEndGameMessage received
     */
    @Override
    public void handleStampEndGameMessage(StampEndGameMessage message) {
        showEndGame(message.getPoints(), message.getNickNames());
    }

    /**
     *Handles the StampEndTurnMessage and displays the updated game board after the end of a turn.
     *@param message the StampEndTurnMessage received
     */
    @Override
    public void handleStampEndTurnMessage(StampEndTurnMessage message) {
        showMatrix(message.getLibrary(), 1);
    }  //TODO: serve ancora? riceve subito dopo l'intera board aggiornata, segnalerei solo la conclusione del turno

    /**
     *Handles the stamp middle turn message by displaying the updated library and the selected blocks.
     *@param message The stamp middle turn message.
     */
    @Override
    public void handleStampMiddleTurnMessage(StampMiddleTurnMessage message) {
        showMatrix(message.getLibrary(), 1);
        showAvailableBlocks(message.getAvailableBlocks());
    }

    /**
     *Handles the StampGameStartMessage and displays all the game information using the method show().
     *@param message the StampGameStartMessage received
     */
    @Override
    public void handleStampStartTurnMessage(StampStartTurnMessage message) {
        show(message.getPlayerlibrary(), message.getPlayerPoints(), message.getGameboard(), message.getSecret(), message.getOtherplayerslibrary(), message.getOtherplayerspoints(), message.getOtherplayersNickName(), message.getCommongoals());
    }  //TODO: aggiungi la stampa dei punti al momento ottenibili raggiungendo il common goal (esiste già il parametro), inoltre è possibile usare anche il nome del giocatore ore

    public void handleStartGameMessage(StartGameMessage message) {
        System.out.println("START GAME!!!");
    }

    @Override
    public void handleWaitingMessage(WaitingMessage message) {
        System.out.println(message.getMessage());
    }

    /**
     *Shows the information of other players including their libraries and points.
     *@param otherplayerslibrary The libraries of other players.
     *@param otherplayerspoints The points of other players.
     *@param otherplayersNickName The nicknames of other players.
     */
    private void showOther(ArrayList<Block[][]> otherplayerslibrary, int[] otherplayerspoints, String[] otherplayersNickName) {
        System.out.print("    "); //compensa l'inizio e mette il nome, poi mette uno spazio fisso col nome successivo
        for (String s : otherplayersNickName) {
            System.out.print("PLAYER: " + s);
            for (int j = 0; j < 25 - s.length(); j++) {
                System.out.print(" ");
            }
        }
        System.out.print("\n");
        //unisce le varie matrici
        ArrayList<Block[][]> matrices = new ArrayList<>(otherplayerslibrary);
        Block[][] board = mergeMatrices(matrices);
        showMatrix(board, otherplayerslibrary.size());
        System.out.print("    "); //compensa l'inizio e mette i punti, poi mette uno spazio fisso col nome successivo
        for (int otherplayerspoint : otherplayerspoints) {
            System.out.printf("POINTS : " + otherplayerspoint);
            if (otherplayerspoint < 10) { //questo if per capire se hai superato le due cifre oppure no e dover mettere uno spazio in più
                for (int j = 0; j < 23; j++) {
                    System.out.print(" ");
                }
            } else {
                for (int j = 0; j < 22; j++) {
                    System.out.print(" ");
                }
            }
        }
    }

    /**
     *Shows the game board.
     *@param gameboard The game board to be shown.
     */
    private void showboard(Block[][] gameboard) {
        System.out.println("\t\t   BOARD");
        showMatrix(gameboard, 0);
    }

    /**
     *Shows the player's library and secret goal.
     *@param playerlibrary The library of the player.
     *@param secret The secret goal of the player.
     *@param playerpoints The points of the player.
     */
    private void showplayer(Block[][] playerlibrary, int[] secret, int playerpoints) {
        System.out.print("   YOUR LIBRARY          \t\t   YOUR SECRET GOAL");
        System.out.print("\n");
        Block[][] secretmatrix = new Block[6][5]; //creo la matrice che ottiene il massimo punteggio nel relativo secret goal
        for (int i = 0; i < 6; i++) {
            for (int j = 0; j < 5; j++) {
                secretmatrix[i][j] = Block.NULL;
            }
        }
        int blockindex = 0;
        for (int i = 0; i < 6; i++) {
            (secretmatrix[secret[i * 2]][secret[(i * 2) + 1]]) = (Block.values()[blockindex]);
            blockindex++;
        }
        ArrayList<Block[][]> matrices = new ArrayList<>(); //unisco le due matrici
        matrices.add(playerlibrary);
        matrices.add(secretmatrix);
        Block[][] mergematrices = mergeMatrices(matrices);
        showMatrix(mergematrices, 2);
        System.out.print("   \t\t\t     YOUR POINTS : " + playerpoints);
    }

    /**
     *Shows the common goals.
     *@param commongoals The common goals to be shown.
     */
    private void showcommongoals(int[] commongoals) {
        for (int i = 1; i < commongoals.length + 1; i++) {
            System.out.println("COMMON GOAL N°" + i + " :");
            switch (commongoals[i - 1]) {   //per compensare essendo che sono partito da 1 per motivi grafici
                case 0 -> System.out.println(CommonDescrition.COMMON_A.getDesc());
                case 1 -> System.out.println(CommonDescrition.COMMON_B.getDesc());
                case 2 -> System.out.println(CommonDescrition.COMMON_C.getDesc());
                case 3 -> System.out.println(CommonDescrition.COMMON_D.getDesc());
                case 4 -> System.out.println(CommonDescrition.COMMON_E.getDesc());
                case 5 -> System.out.println(CommonDescrition.COMMON_F.getDesc());
                case 6 -> System.out.println(CommonDescrition.COMMON_G.getDesc());
                case 7 -> System.out.println(CommonDescrition.COMMON_H.getDesc());
                case 8 -> System.out.println(CommonDescrition.COMMON_I.getDesc());
                case 9 -> System.out.println(CommonDescrition.COMMON_J.getDesc());
                case 10 -> System.out.println(CommonDescrition.COMMON_K.getDesc());
                case 11 -> System.out.println(CommonDescrition.COMMON_L.getDesc());
            }
            System.out.print("\n");
        }
    }

    /**
     *Shows the matrix representation of a block grid.
     *@param matrix The block matrix to be shown.
     *@param numLibrary The number of libraries.
     */
    public void showMatrix(Block[][] matrix, int numLibrary) { //numLibrary per dire quante librerie vicine ci sono
        int row;
        int column;
        if (numLibrary == 0) {
            row = 9;
            column = 9;
        } else {
            row = 6;
            column = 5 * numLibrary;
        }
        System.out.print("   "); //per compensare l'inizio di ogni riga
        int repeat = 1;
        if (numLibrary <= 1) { //valido per una libreria o per la board
            for (int i = 0; i < column; i++) {
                System.out.print(" " + i);
            }
        } else {
            for (int i = 0; i < 5; i++) { // valido per più librerie
                System.out.print(" " + i);
                if (i == 4 && repeat < numLibrary) {
                    System.out.print("                       ");
                    i = -1;
                    repeat++;
                }
            }
        }
        System.out.print("\n");
        for (int rowCount = 0; rowCount < row; rowCount++) {
            System.out.print(rowCount + ": |");
            repeat = 1;
            for (int columnCount = 0; columnCount < column; columnCount++) {
                switch (matrix[rowCount][columnCount].getIndex()) {
                    case (0) ->
                            System.out.print(Colors.ANSI_PURPLE.getcolor() + "■" + Colors.ANSI_RESET.getcolor() + "|");
                    case (1) ->
                            System.out.print(Colors.ANSI_YELLOW.getcolor() + "■" + Colors.ANSI_RESET.getcolor() + "|");
                    case (2) ->
                            System.out.print(Colors.ANSI_BLUE.getcolor() + "■" + Colors.ANSI_RESET.getcolor() + "|"); // BLUE E CYAN sono molto simili
                    case (3) ->
                            System.out.print(Colors.ANSI_CYAN.getcolor() + "■" + Colors.ANSI_RESET.getcolor() + "|");
                    case (4) ->
                            System.out.print(Colors.ANSI_WHITE.getcolor() + "■" + Colors.ANSI_RESET.getcolor() + "|");
                    case (5) ->
                            System.out.print(Colors.ANSI_GREEN.getcolor() + "■" + Colors.ANSI_RESET.getcolor() + "|");
                    default ->
                            System.out.print(Colors.ANSI_BLACK.getcolor() + "■" + Colors.ANSI_RESET.getcolor() + "|");
                }
                if (numLibrary > 1 && repeat < numLibrary && (columnCount == 4 || columnCount == 9)) { //nel caso di due o tre librerie (tre è il caso massimo)
                    System.out.print("                      |");
                    repeat++;
                }
            }
            System.out.print("\n");
        }
    }

    /**
     *Merges the given matrices into a single matrix.
     *@param matrices the list of matrices to merge.
     *@return the merged matrix.
     */
    private Block[][] mergeMatrices(ArrayList<Block[][]> matrices) {
        int rows = 6;
        int cols = 5;
        int n = matrices.size();
        Block[][] result = new Block[rows][n * cols];

        for (int i = 0; i < n; i++) {
            Block[][] matrix = matrices.get(i);
            for (int j = 0; j < rows; j++) {
                System.arraycopy(matrix[j], 0, result[j], i * cols, cols);
            }
        }

        return result;
    }

    /**
     *Displays the game state including player libraries, points, game board, secret goals, other players' information, and common goals.
     *@param playerlibrary The block library of the current player.
     *@param playerpoints The points of the current player.
     *@param gameboard The game board.
     *@param secret The secret goals of the current player.
     *@param otherplayerslibrary The block libraries of other players.
     *@param otherplayerspoints The points of other players.
     *@param otherplayersNickName The nicknames of other players.
     *@param commongoals The common goals of the game.
     */
    private void show(Block[][] playerlibrary, int playerpoints, Block[][] gameboard, int[] secret, ArrayList<Block[][]> otherplayerslibrary, int[] otherplayerspoints, String[] otherplayersNickName, int[] commongoals) {
        System.out.print("\n");
        showcommongoals(commongoals);
        System.out.print("\n");
        showOther(otherplayerslibrary, otherplayerspoints, otherplayersNickName);
        System.out.println("\n");
        showboard(gameboard);
        System.out.print("\n");
        showplayer(playerlibrary, secret, playerpoints);
        System.out.print("\n");
    }

    /**
     *Displays the available blocks that can be drawn in the library.
     *@param drawedBlocks The list of available blocks.
     */
    public void showAvailableBlocks(ArrayList<Block> drawedBlocks) {
        if (drawedBlocks.isEmpty()) {
            System.out.print("No more blocks\n");
        } else {
            for (Block b : drawedBlocks) {
                switch (b.getIndex()) {
                    case (0) -> System.out.print("0)Pink ");
                    case (1) -> System.out.print("1)Orange ");
                    case (2) -> System.out.print("2)Blue ");
                    case (3) -> System.out.print("3)Light_blue ");
                    case (4) -> System.out.print("4)White ");
                    case (5) -> System.out.print("5)Green ");
                }
            }
            System.out.print("\n");
        }
    }

    /**
     *Displays the end game results including points and nicknames of all players.
     *@param points The points of each player.
     *@param nickNames The nicknames of each player.
     */
    private void showEndGame(int[][] points, String[] nickNames) {
        System.out.println("                                          THE GAME IS ENDED!!!");
        System.out.println("NICKNAMES           |  SECRET GOAL  |  COMMON GOAL  |    ADJACENT   |     FINAL     |     TOTAL     |\n");
        String name = null;
        for (int i = 0; i < nickNames.length; i++) {
            System.out.print(nickNames[i]);
            for (int k = 0; k < 20 - nickNames[i].length(); k++) {
                System.out.print(" ");
            }
            System.out.print("|");
            for (int j = 0; j < 5; j++) {
                System.out.print("       " + points[i][j]);
                if (points[i][j] < 10) System.out.print("       |");
                else System.out.print("      |");
            }
            System.out.println();
            int max = 0;
            for(int k = 0 ; k < nickNames.length ; k++){
                if(points[k][4] >= max){
                    max = points[k][4];
                    name = nickNames[k];
                }
            }
        }
        System.out.println("THE WINNER IS " + name + " !!!");
    }

    /**
     *Reads and retrieves user input from the command line.
     *@return The user input as a string.
     */
    public static String getInput() {
        try {
            return new BufferedReader(new InputStreamReader(System.in)).readLine();
        } catch (IOException e) {
            System.err.println(e.getMessage());
            return null;
        }
    }

    public void setMessageHandler(MessageHandler messageHandler) {
        this.messageHandler = messageHandler;
    }
}
