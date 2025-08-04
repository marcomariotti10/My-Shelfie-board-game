package it.polimi.ingsw.messages.toServer;

import it.polimi.ingsw.messages.toClient.ClientErrorMessage;
import it.polimi.ingsw.messages.toClient.EnterNicknameMessage;
import it.polimi.ingsw.exceptions.AlreadyExistNickException;
import it.polimi.ingsw.server.ClientHandler;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class SetNicknameMessage implements ServerMessage {
    public String nickname;

    public SetNicknameMessage(String nickname) {
        this.nickname = nickname;
    }

    public String getNickname(){
        return nickname;
    }

}
