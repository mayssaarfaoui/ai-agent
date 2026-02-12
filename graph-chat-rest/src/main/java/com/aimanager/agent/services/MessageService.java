package com.aimanager.agent.services;


import com.aimanager.agent.dto.VisitorResponseDto;
import com.aimanager.agent.enums.Sender;
import com.aimanager.agent.models.*;
import com.aimanager.agent.models.conversation.Conversation;
import com.aimanager.agent.models.conversation.TaskConversation;
import com.aimanager.agent.repository.CMessageRepository;
import com.google.gson.Gson;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.cassandra.core.CassandraOperations;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class MessageService<C extends Conversation> {

    private static final Logger logger = org.slf4j.LoggerFactory.getLogger(MessageService.class);

    @Autowired
    CMessageRepository cMessageRepository;

    @Autowired
    private CassandraOperations cassandraOperations;

    protected final Gson gson = new Gson();

    public void setQuestionASAnswered(long userId, UUID conversationId) {
        CMessage message = cMessageRepository.findLastMessage(userId, conversationId);
        if(message.getSender()==Sender.SYSTEM) {
            VisitorResponseDto response = gson.fromJson(message.getContent(), VisitorResponseDto.class);
            response.setIsAnswered(true);
            String content = gson.toJson(response);
            logger.info("Saving message: {}", content);
            message.setContent(content);
            message.setCreatedAt(LocalDateTime.now());
            cMessageRepository.save(message);
        }
    }

    public void saveMessage(C conversation, VisitorResponseDto response) {
        CMessage message = new CMessage();
        CMessage.MessageKey key = new CMessage.MessageKey();
        key.setConversation(conversation.getId());
        key.setUser(conversation.getUserId());
        key.setTimestamp(Instant.now());
        message.setId(key);
        message.setSender(Sender.SYSTEM);
        logger.info("Convert content for message with node : {}", response.getNodeId());
        String content = gson.toJson(response);
        logger.info("Saving message: {}", content);
        message.setContent(content);
        message.setCreatedAt(LocalDateTime.now());
        cMessageRepository.save(message);
    }

    public void saveMessage(C conversation, String content) {
        CMessage message = new CMessage();
        CMessage.MessageKey key = new CMessage.MessageKey();
        key.setConversation(conversation.getId());
        key.setUser(conversation.getUserId());
        key.setTimestamp(Instant.now());
        message.setId(key);
        message.setSender(Sender.USER);
        logger.info("Saving message: {}", content);
        message.setContent(content);
        message.setCreatedAt(LocalDateTime.now());
        cMessageRepository.save(message);
    }

    public List<Message> fetchMessages(C conversation) {
        List<CMessage> cms =  cMessageRepository.findById_UserAndId_Conversation(conversation.getUserId(), conversation.getId());
        List<Message> messages= of(cms);
        return messages;
    }

    public UMessage ofMessageUser(CMessage cm){
        UMessage message = new UMessage();
        message.setSender(cm.getSender());
        message.setContent(cm.getContent());
        message.setCreatedAt(cm.getCreatedAt());
        return message;
    }

    public SMessage ofMessageSystem(CMessage cm){
        SMessage message = new SMessage();
        message.setSender(cm.getSender());
        VisitorResponseDto response = gson.fromJson(cm.getContent(), VisitorResponseDto.class);
        message.setContent(response);
        message.setCreatedAt(cm.getCreatedAt());
        return message;
    }

    public Message of(CMessage cm){
        switch (cm.getSender()){
            case USER:
                return  ofMessageUser(cm);
            case SYSTEM:
                return ofMessageSystem(cm);
            default:
                throw new IllegalArgumentException("Invalid sender: " + cm.getSender());
        }
    }

    public  List<Message> of(List<CMessage> cms){
        return cms.stream().map(item -> of(item)).collect(Collectors.toList());
    }

    /**
     * Deletes the latest message for a conversation from Cassandra.
     *
     * @param user         the user ID
     * @param conversation the conversation UUID
     */
    public void deleteLatestMessage(long user, UUID conversation) {

        // Execute the query and return the result
        CMessage latestMessage = cMessageRepository.findLastMessage(user, conversation);

        if (latestMessage != null)
            cassandraOperations.delete(latestMessage);
    }

    /**
     * delete all conversation's messages
     * @param conversation
     */

    public void deleteConversationMessages(C conversation){
        cMessageRepository.deleteByUserAndConversation(conversation.getUserId(),conversation.getId());
    }
}
