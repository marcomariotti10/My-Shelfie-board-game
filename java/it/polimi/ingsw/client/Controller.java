package it.polimi.ingsw.client;
import it.polimi.ingsw.messages.toClient.*;

/**
 * Interface shared by the gui and cli containing all possible methods for the commands in the game
 * @author Francesco Foresti, Andrea Gollo
 */
public interface Controller {
    default void handleMessage(ClientMessage message){
        switch (message.getClass().getSimpleName()) {
            default -> {
            }
            case "AskBlockMessage" -> {
                handleAskBlockMessage((AskBlockMessage) message);
            }
            case "ClientErrorMessage" -> {
                handleClientErrorMessage((ClientErrorMessage) message);
            }
            case "ColumnInsertionMessage" -> {
                handleColumnInsertionMessage((ColumnInsertionMessage) message);
            }
            case "DrawBlockMessage" -> {
                handleDrawBlockRequest((DrawBlockMessage) message);
            }
            case "EndGameMessage" -> {
                handleEndGameMessage((EndGameMessage) message);
            }
            case "StampEndGameMessage" -> {
                handleStampEndGameMessage((StampEndGameMessage) message);
            }
            case "StampEndTurnMessage" -> {
                handleStampEndTurnMessage((StampEndTurnMessage) message);
            }
            case "StampMiddleTurnMessage" -> {
                handleStampMiddleTurnMessage((StampMiddleTurnMessage) message);
            }
            case "EnterNicknameMessage" -> {
                handleEnterNicknameMessage((EnterNicknameMessage) message);
            }
            case "StampStartTurnMessage" -> {
                handleStampStartTurnMessage((StampStartTurnMessage) message);
            }
            case "StartGameMessage" -> {
                handleStartGameMessage((StartGameMessage) message);
            }
            case "GameSettingsMessage" -> {
                handleGameSettingsMessage((GameSettingsMessage) message);
            }
            case "LoginMessage" -> {
                handleLoginMessage((LoginMessage) message);
            }
            case "PingMessage" -> {
                handlePingMessage((PingMessage) message);
            }
            case "WaitingMessage" ->{
                handleWaitingMessage((WaitingMessage) message);
            }
        }
    }
    void handlePingMessage(PingMessage message);
    void handleLoginMessage(LoginMessage message);
    void handleAskBlockMessage(AskBlockMessage message);
    void handleClientExitMessage(ClientExitMessage message);
    void handleClientErrorMessage(ClientErrorMessage message);
    void handleColumnInsertionMessage(ColumnInsertionMessage message);
    void handleDrawBlockRequest(DrawBlockMessage message);
    void handleEndGameMessage(EndGameMessage message);
    void handleStampEndGameMessage(StampEndGameMessage message);
    void handleStampEndTurnMessage(StampEndTurnMessage message);
    void handleStampMiddleTurnMessage(StampMiddleTurnMessage message);
    void handleEnterNicknameMessage(EnterNicknameMessage message);
    void handleStampStartTurnMessage(StampStartTurnMessage message);
    void handleGameSettingsMessage(GameSettingsMessage message);
    void handleStartGameMessage(StartGameMessage message);
    void handleWaitingMessage(WaitingMessage message);
}