package com.aimanager.agent.config;

import com.corundumstudio.socketio.SocketIOClient;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ConnectedUsers {

    private static ConnectedUsers instance;

    // Map to store userId and their corresponding SocketIOClient
    private final Map<String, SocketIOClient> userMap;

    // Private constructor for singleton
    private ConnectedUsers() {
        userMap = new ConcurrentHashMap<>();
    }

    // Get the single instance of ConnectedUsers
    public static synchronized ConnectedUsers getInstance() {
        if (instance == null) {
            instance = new ConnectedUsers();
        }
        return instance;
    }

    // Add a user to the map
    public void addUser(String userId, SocketIOClient client) {
        userMap.put(userId, client);
        System.out.println("User added: " + userId);
    }

    // Remove a user from the map
    public void removeUser(String userId) {
        userMap.remove(userId);
        System.out.println("User removed: " + userId);
    }

    // Get a user by their userId
    public SocketIOClient getUser(String userId) {
        return userMap.get(userId);
    }

    // Check if a user is connected
    public boolean isUserConnected(String userId) {
        return userMap.containsKey(userId);
    }

    // Get the total number of connected users
    public int getConnectedUserCount() {
        return userMap.size();
    }

    public List<String> getConnectedUsers() {
        return new ArrayList<>(userMap.keySet());
    }
}
