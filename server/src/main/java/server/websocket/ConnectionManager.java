package server.websocket;

import org.eclipse.jetty.websocket.api.Session;

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
}
