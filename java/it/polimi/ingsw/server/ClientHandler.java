package it.polimi.ingsw.server;

import it.polimi.ingsw.exceptions.*;
import it.polimi.ingsw.messages.toClient.*;
import it.polimi.ingsw.messages.toServer.*;
import it.polimi.ingsw.server.controllers.LobbyController;
import it.polimi.ingsw.server.model.Block;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.*;
/**
 *handle the single connection of the client with the server.
 *@author Gabriele Marra, Marco Mariotti
 */

public abstract class ClientHandler extends UnicastRemoteObject {
    private Server server;
    private String nickname;
    private String lobbyName;

    public ClientHandler(Server server) throws RemoteException {
        this.server = server;
    }

    public LobbyController getLobbyController() {
        return server.getLobbyController(lobbyName);
    }

    abstract public void sendMessage(ClientMessage message);

    public Server getServer() {
        return server;
    }

    /**
     * Sets the nickname for the player.
     *
     * @param nickname The nickname to be set.
     * @throws AlreadyExistNickException    If the nickname already exists in the lobby.
     * @throws NotMissingPlayerException    If the lobby is already full and the player is not missing.
     */
    public void setNickname(String nickname) throws AlreadyExistNickException, NotMissingPlayerException {
        if (getLobbyController().getNicknameList().size() < getLobbyController().getNumberOfPlayers()) { //match not altredy started
            for (ClientHandler handler : getLobbyController().getClientHandlers()) {
                if (!handler.equals(this)) {
                    if (handler.getNickname().equals(nickname)) throw new AlreadyExistNickException();
                }
            }
            getLobbyController().addNickname(nickname);
            getLobbyController().addClientHandler(this);


        } else if ((getLobbyController().getClientHandlers().contains(null))) { //match already started, player missing
            int pos = getLobbyController().getNicknameList().indexOf(nickname);
            if (pos == -1) { //there aren't players with that name
                System.out.println("Not missing player exception throwed");
                throw new NotMissingPlayerException();
            } else if (getLobbyController().getClientHandlers().get(pos) == null) { //not active player in the match
                getLobbyController().getClientHandlers().set(pos, this);
            } else {
                System.out.println("Not missing player exception throwed");
                throw new NotMissingPlayerException();
            }
        }
        this.nickname = nickname;
        System.out.println("questi sono i client handlers: " + getLobbyController().getClientHandlers());
    }

    public String getNickname() {
        return this.nickname;
    }

    public void setServer(Server server) throws RemoteException {
        this.server = server;
    }

    /**
     * Handles the incoming server message.
     *
     * @param message The server message to be handled.
     */
    public void handleMessage(ServerMessage message) {
        switch (message.getClass().getSimpleName()) {
            case "AccessLobbyMessage" -> {
                handleAccess((AccessLobbyMessage) message);
            }
            case "CreateLobbyMessage" -> {
                handleCreate((CreateLobbyMessage) message);
            }
            case "DrawBlocksMessage" -> {
                handleDraw((DrawBlocksMessage) message);
            }
            case "RMIConnectedClientMessage" -> {
                handleRMIConnected();
            }
            case "ServerExitMessage" -> {
                handleExit();
            }
            case "SetBlockMessage" -> {
                handleSetBlock((SetBlockMessage) message);
            }
            case "SetColumnMessage" -> {
                handleSetColumn((SetColumnMessage) message);
            }
            case "SetLobbySettingMessage" -> {
                handleSetLobby((SetLobbySettingMessage) message);
            }
            case "SetNicknameMessage" -> {
                handleSetNickname((SetNicknameMessage) message);
            }
            default -> {
            }
        }
    }


    /**
     * Handles the AccessLobbyMessage to allow a client to access a lobby.
     *
     * @param message The AccessLobbyMessage containing the lobby name.
     */
    public void handleAccess(AccessLobbyMessage message) {
        final LobbyController lobbyController = server.getLobbyController(message.getLobbyName());
        if (lobbyController == null) { //There is not lobby controller with such name
            sendMessage(new ClientErrorMessage("There isn't a lobby with that name"));
            sendMessage(new LoginMessage());
        } else {
            try {
                boolean clientHandlersContainsNull = lobbyController.getClientHandlers().contains(null);
                boolean clientHandlersFilled = (lobbyController.getClientHandlers().size() == lobbyController.getNumberOfPlayers());
                //all connected
                if (!clientHandlersContainsNull && clientHandlersFilled) {
                    throw new ExceedNumberOfPlayersException();
                } else {
                    this.lobbyName = message.getLobbyName();
                    sendMessage(new EnterNicknameMessage());
                }
            } catch (ExceedNumberOfPlayersException e) {
                sendMessage(new ClientErrorMessage(e.getMessage()));
            }
        }
    }

    /**
     * Handles the CreateLobbyMessage to create a new lobby.
     *
     * @param message The CreateLobbyMessage containing the lobby name.
     */
    public void handleCreate(CreateLobbyMessage message) {
        try {
            server.checkLobbyName(message.getLobbyName());
            sendMessage(new GameSettingsMessage(message.getLobbyName()));
        } catch (LobbyControllerAlreadyExist e) {
            sendMessage(new ClientErrorMessage(e.getMessage()));
            sendMessage(new LoginMessage());
        }

    }

    /**
     * Handles the DrawBlocksMessage to draw blocks on the board.
     *
     * @param message The DrawBlocksMessage containing the positions of the blocks to be drawn.
     */
    public void handleDraw(DrawBlocksMessage message) {
        try {
            System.out.println(message + " " + message.getPositions());
            Set<ArrayList<Integer>> positions = message.getPositions();
            //System.out.println(positions);
            if (!(getLobbyController().getMatch().getBoard().takable(positions))) throw new InvalidDrawException();
            System.out.println("è takable");
            if (!getLobbyController().getActiveTurnController().getCurrentPlayer().Lib.enoughSpace(positions.size()))
                throw new NotEnoughSpaceException();
            System.out.println("c'è spazio sufficiente");
            ArrayList<Block> drewBlock = getLobbyController().getMatch().getBoard().take(positions);
            getLobbyController().getActiveTurnController().setDrewBlocks(drewBlock);

        } catch (NotFreeBlockException | NotAdjacentException | OutOfTakeRangeException | NotEnoughSpaceException |
                 NullBlockException | InvalidDrawException | IllegalArgumentException | NotStraightException e) {
            sendMessage(new ClientErrorMessage(e.getMessage()));
            sendMessage(new DrawBlockMessage());
        }
    }

    public void handleRMIConnected() {
        System.out.println("Messaggio ricevuto");
    }

    public void handleExit() {
        System.out.println("asking to insert the name"); // cosa serve?
    }

    public void handleSetBlock(SetBlockMessage message) {
        System.out.println("The colour arrived\n");
        getLobbyController().getActiveTurnController().setToAddBlock(message.getBlock());
    }

    /**
     * Handles the SetColumnMessage to set the chosen column for adding blocks to the library.
     *
     * @param message The SetColumnMessage containing the column index and block size.
     */
    public void handleSetColumn(SetColumnMessage message) {
        System.out.println("Column chosen\n");
        try {
            if (!getLobbyController().getActiveTurnController().getCurrentPlayer().Lib.addable(message.getColumn(), message.getSize()))
                throw new ExceededLibraryLimitException();//spazio nella colonna selezionata?
            System.out.println("The blocks can be added to the column\n");
            getLobbyController().getActiveTurnController().setSelectedColumn(message.getColumn());
        } catch (ExceededLibraryLimitException e) {
            sendMessage(new ClientErrorMessage(e.getMessage()));
            sendMessage(new ColumnInsertionMessage(message.getSize()));
        }
    }

    /**
     * Handles the SetLobbySettingMessage to set up the lobby with the specified settings.
     *
     * @param message The SetLobbySettingMessage containing the lobby name, number of players, and number of common goals.
     */
    public void handleSetLobby(SetLobbySettingMessage message) {
        try {
            lobbyName = message.getLobbyName();
            LobbyController lobby = new LobbyController(message.getLobbyName(), message.getNumberOfPlayers(), message.getNumberOfCommonGoals());
            Thread thread = new Thread(lobby);
            thread.start();
            server.addLobbyController(lobby);  //getmessage() si può comunque chiamare essendo all'interno dell'SChandler
            System.out.println("Open Lobby");
            System.out.println("max player setted");
            sendMessage(new EnterNicknameMessage());
        } catch (LobbyControllerAlreadyExist e) {
            sendMessage(new ClientErrorMessage(e.getMessage()));
        }
    }

    /**
     * Handles the SetNicknameMessage to set the nickname for the player.
     *
     * @param message The SetNicknameMessage containing the nickname.
     */
    public void handleSetNickname(SetNicknameMessage message) {
        try {
            final LobbyController lobbyController = getLobbyController();
            setNickname(message.getNickname());
            sendMessage(new WaitingMessage());
            boolean numberOfPlayersReached = lobbyController.getNumberOfPlayers() == lobbyController.getClientHandlers().size();
            boolean clientHandlersNotContainsNull = !lobbyController.getClientHandlers().contains(null);

        } catch (AlreadyExistNickException | NotMissingPlayerException e) {
            sendMessage(new ClientErrorMessage(e.getMessage()));
            sendMessage(new EnterNicknameMessage());
        }
    }
}