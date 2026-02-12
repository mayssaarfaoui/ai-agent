package com.aimanager.agent.services;


import com.aimanager.agent.config.ConnectedUsers;
import com.aimanager.agent.config.SocketIOController;
import com.aimanager.agent.models.conversation.Conversation;
import com.aimanager.agent.models.conversation.TaskConversation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class NotificationService<C extends Conversation> {

    @Autowired
    SocketIOController socketIOController;


    public void askUserToStartConversation(C conversation) {
        Long userId = conversation.getUserId();
        boolean isConnected = ConnectedUsers.getInstance().isUserConnected(userId.toString());
        if(isConnected) {
            socketIOController.notifyUser(conversation);
        }

    }
}
