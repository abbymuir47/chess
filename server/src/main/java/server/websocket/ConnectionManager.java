package server.websocket;

import org.eclipse.jetty.websocket.api.Session;

import java.util.HashMap;
import java.util.Map;

public class ConnectionManager {
    public final Map<String, Connection> connections = new HashMap<>();
    //what kinds of operations do i need to do? add? or get? or just be able to iterate through several what operations
    // do i need to do vs not do?

    public void add(String userName, Session session) {
        var connection = new Connection(userName, session);
        connections.put(userName, connection);
    }

    public void remove(String userName) {
        connections.remove(userName);
    }
}
