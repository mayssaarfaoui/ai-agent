package com.aimanager.agent.services;

import com.aimanager.agent.converter.Converter;
import com.aimanager.agent.models.CConversation;
import com.aimanager.agent.models.conversation.TaskConversation;
import com.aimanager.agent.repository.CConversationRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CConversationService{

    private final Logger log = LoggerFactory.getLogger(CConversationService.class);

    @Autowired
    CConversationRepository conversationRepository;


    public CConversation convertToCData(TaskConversation c){
        CConversation cf = new CConversation();
        return CConversation.copy(c,cf);
    }

    public void storeConversation(TaskConversation c){
        CConversation cf = convertToCData(c);
        conversationRepository.save(cf);
    }

    public List<TaskConversation> getStoredData(Long userId){
        List<CConversation>  cdata =  conversationRepository.findById_User(userId);

        List<TaskConversation> conversations = cdata.stream().
        map(item -> Converter.of(item)).collect(Collectors.toList());

        return conversations;
    }
}
