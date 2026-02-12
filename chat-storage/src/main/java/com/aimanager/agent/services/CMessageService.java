package com.aimanager.agent.services;

import com.aimanager.agent.models.CMessage;
import com.aimanager.agent.models.Message;
import com.aimanager.agent.repository.CMessageRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
/*
@Service
public class CMessageService {

    private final Logger log = LoggerFactory.getLogger(CMessageService.class);

    @Autowired
    CMessageRepository messageRepository;

    public CMessage convertToCData(Message m){
        CMessage cf = new CMessage();
        return CMessage.copy(m,cf);
    }

    public void storeMessage(Message form){
        CMessage cf = convertToCData(form);
        messageRepository.save(cf);
    }

    public List<CMessage> getStoredData(Long userId, UUID conversationId){
        List<CMessage>  cdata =  messageRepository.findById_UserAndId_Conversation(userId,conversationId);
        return cdata;
    }
}*/
