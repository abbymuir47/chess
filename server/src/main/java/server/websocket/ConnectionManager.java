package server.websocket;

import org.eclipse.jetty.websocket.api.Session;
import websocket.messages.ServerMessage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ConnectionManager {

    public final Map<Integer, Connection> connections = new HashMap<>();
    //what kinds of operations do i need to do? add? or get? or just be able to iterate through several what operations
    // do i need to do vs not do?

    public void add(int gameID, String username, Session session) {
        var connection = new Connection(username, session);
        connections.put(gameID, connection);
    }

    public void remove(int gameID) {
        connections.remove(gameID);
    }

    public void sendMessage(Session session, ServerMessage message) throws IOException {
        var removeList = new ArrayList<Connection>();
        for (var c : connections.values()) {
            if (c.session.isOpen()) {
                c.session.getRemote().sendPong(message.toString());
            }
        }
    }

}
