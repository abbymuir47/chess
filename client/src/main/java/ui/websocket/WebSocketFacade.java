package ui.websocket;

import com.google.gson.Gson;
import exception.ResponseException;
import websocket.commands.UserGameCommand;
import websocket.messages.ErrorMessage;
import websocket.messages.ServerMessage;

import javax.websocket.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import static websocket.commands.UserGameCommand.CommandType.CONNECT;
import static websocket.messages.ServerMessage.ServerMessageType.ERROR;

public class WebSocketFacade extends Endpoint{

    Session session;
    ServerMessageObserver observer;
    Gson gson = new Gson();

    public WebSocketFacade(String url, ServerMessageObserver observer) throws ResponseException {
        try {
            url = url.replace("http", "ws");
            URI socketURI = new URI(url + "/ws");
            this.observer = observer;

            WebSocketContainer container = ContainerProvider.getWebSocketContainer();
            this.session = container.connectToServer(this, socketURI);

            //set message handler
            this.session.addMessageHandler(new MessageHandler.Whole<String>() {
                @Override
                public void onMessage(String message) {
                    System.out.println("client-side, about to call notify from onMessage");
                    observer.notify(message);
                    /*
                    try {
                        observer.notify(message);
                    } catch(Exception ex) {
                        observer.notify(new ErrorMessage(ERROR, ex.getMessage()));
                    }

                     */
                }
            });
        } catch (DeploymentException | IOException | URISyntaxException ex) {
            throw new ResponseException(ex.getMessage());
        }
    }

    //Endpoint requires this method, but you don't have to do anything
    @Override
    public void onOpen(Session session, EndpointConfig endpointConfig) {
        System.out.println("client-side, websocket connection opened");
    }

    public void observeGame(String authToken, int gameID) throws ResponseException {
        System.out.println("client-side, observe game request made");

        try {
            UserGameCommand observeCommand = new UserGameCommand(CONNECT, authToken, gameID);
            this.session.getBasicRemote().sendText(new Gson().toJson(observeCommand));
        } catch (IOException ex) {
            throw new ResponseException(ex.getMessage());
        }
    }

    public void leaveGame(String authToken, int gameID) throws ResponseException {
        System.out.println("client-side, observe game request made");

        try {
            UserGameCommand observeCommand = new UserGameCommand(CONNECT, authToken, gameID);
            this.session.getBasicRemote().sendText(new Gson().toJson(observeCommand));
        } catch (IOException ex) {
            throw new ResponseException(ex.getMessage());
        }
    }
}
