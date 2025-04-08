package ui.websocket;

import com.google.gson.Gson;
import exception.ResponseException;
import websocket.messages.ServerMessage;

import javax.websocket.*;
import javax.websocket.ContainerProvider;
import javax.websocket.WebSocketContainer;
import javax.websocket.Endpoint;
import javax.websocket.MessageHandler;
import javax.websocket.Session;
import javax.websocket.EndpointConfig;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

public class WebSocketFacade extends Endpoint{

    Session session;
    ServerMessageObserver serverMessageObserver;

    public WebSocketFacade(String url, ServerMessageObserver serverMessageObserver) throws ResponseException {
        try {
            url = url.replace("http", "ws");
            URI socketURI = new URI(url + "/ws");
            this.serverMessageObserver = serverMessageObserver;

            WebSocketContainer container = ContainerProvider.getWebSocketContainer();
            this.session = container.connectToServer(this, socketURI);

            //set message handler
            this.session.addMessageHandler(new MessageHandler.Whole<String>() {
                @Override
                public void onMessage(String message) {
                    try {
                        ServerMessage message =
                                gson.fromJson(message, ServerMessage.class);
                        observer.notify(message);
                    } catch(Exception ex) {
                        observer.notify(new ErrorMessage(ex.getMessage()));
                    }
                }
            });
        } catch (DeploymentException | IOException | URISyntaxException ex) {
            throw new ResponseException(500, ex.getMessage());
        }
    }

    //Endpoint requires this method, but you don't have to do anything
    @Override
    public void onOpen(Session session, EndpointConfig endpointConfig) {
    }

    /*
    public void onMessage(String message) {
  try {
    ServerMessage message =
        gson.fromJson(message, ServerMessage.class);
    observer.notify(message);
  } catch(Exception ex) {
    observer.notify(new ErrorMessage(ex.getMessage()));
  }
}

     */

}
