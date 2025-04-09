package server.websocket;

import com.google.gson.Gson;
import org.eclipse.jetty.websocket.api.Session;
import websocket.messages.ServerMessage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

    public class ConnectionManager {

        private final Map<Integer, Map<String,Session>> connections = new ConcurrentHashMap<>();
        private final Gson gson = new Gson();
        //what kinds of operations do i need to do? add? or get? or just be able to iterate through several what operations
        // do i need to do vs not do?

        public void add(int gameID, String username, Session session) {
            connections.putIfAbsent(gameID, new ConcurrentHashMap<>());
            connections.get(gameID).put(username, session);
        }

        public void remove(int gameID, String username) {
            if(connections.containsKey(gameID)){
                if(connections.get(gameID).containsKey(username)){
                    connections.get(gameID).remove(username);
                }
                if(connections.get(gameID).isEmpty()){
                    connections.remove(gameID);
                }
            }

            connections.remove(gameID);
        }

        public void removeGame(int gameID){
            connections.remove(gameID);
        }

        public void sendMessage(Session session, ServerMessage message) throws IOException {
            if(session.isOpen()){
                session.getRemote().sendString(gson.toJson(message));
            }
        }

        public void broadcast(int gameID, ServerMessage message) throws IOException {
            broadcast(gameID, null, message);
        }

        public void broadcast(int gameID, String excludeUsername, ServerMessage message) throws IOException {
            System.out.println("in broadcast");
            if(!connections.containsKey(gameID)){
                return;
            }
            else{
                System.out.println("gameID connection found");
                var gameConnections = connections.get(gameID);
                for(var user: gameConnections.keySet()){
                    System.out.println("user:" + user);
                    if(!user.equals(excludeUsername)){
                        Session currSession = gameConnections.get(user);

                        if(currSession.isOpen()){
                            Connection currConnection = new Connection(user, currSession);
                            System.out.println("currSession open:" + currSession);
                            currConnection.send(message.toString());
                        }
                    }
                }
            }
        }

    }
