package com.aimanager.agent.services;

import com.aimanager.agent.Form.traverse.TraverseGraphForm;
import com.aimanager.agent.dto.ConversationResponseDto;
import com.aimanager.agent.dto.VisitorResponseDto;
import com.aimanager.agent.exceptions.MessageException;
import com.aimanager.agent.exceptions.MissingEntityException;
import com.aimanager.agent.models.GraphNode;
import com.aimanager.agent.models.Message;
import com.aimanager.agent.models.NodeType;
import com.aimanager.agent.models.StartNode;
import com.aimanager.agent.models.conversation.Conversation;
import com.aimanager.agent.models.conversation.UserConversation;
import com.aimanager.agent.repository.ConversationRepository;
import com.aimanager.agent.repository.UserConversationRepository;
import com.aimanager.agent.request.ContinueConversationForm;
import com.aimanager.agent.request.ConversationSearchForm;
import com.aimanager.agent.request.CreateUserConversationForm;
import com.aimanager.agent.request.UserConversationForm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class ConversationService<N extends GraphNode, C extends Conversation, M extends Message> {

    private static final Logger log = LoggerFactory.getLogger(ConversationService.class);

    @Autowired
    ConversationRepository<C> conversationRepository;

    @Autowired
    UserConversationRepository userConversationRepository;

    @Autowired
    private GraphVisitorService<N> graphVisitorService;

    @Autowired
    MessageService<C> messageService;

    @Autowired
    CommitService<N> commitService;

    public void clearConversation(Long userId) throws MessageException, MissingEntityException {
        List<C> conversations = conversationRepository.findByUserId(userId);
        if (conversations == null || conversations.isEmpty())
            throw new RuntimeException("No conversations found for the user");
        for (C c : conversations) {
            messageService.deleteConversationMessages(c);
        }
        conversationRepository.deleteAll((List<C>) conversations);
    }

    public void deleteConversation(UserConversationForm form) {
        Optional<C> conversation = conversationRepository.findByIdAndUserId(form.getConversationId(), form.getUserId());
        if (!conversation.isPresent())
            throw new RuntimeException("Conversation not found");
        C c = conversation.get();
        messageService.deleteConversationMessages(c);
        conversationRepository.delete(c);
    }

    public boolean hasActiveConversation(Long userId) {
        return conversationRepository.existsByUserIdAndStartedAndEnded(userId, true, false);
    }

    public Page<C> searchConversations(ConversationSearchForm form) {
        return conversationRepository.findByUserId(form.getUserId(), form.getPageable());
    }

    public C getOldestActiveConversation(Long userId) {
        C conversation = null;
        Optional<C> conversations = conversationRepository.findOldestActiveConversationByUserId(userId);
        if (conversations.isPresent()) {
            conversation = conversations.get();
        }
        return conversation;
    }

    public void skipConversation(UserConversationForm form) {
        Optional<C> conversation = conversationRepository.findByIdAndUserId(form.getConversationId(), form.getUserId());
        if (!conversation.isPresent())
            throw new RuntimeException("Conversation not found");
        C c = conversation.get();

        if (c.isEnded())
            throw new RuntimeException("You can not skip an ended conversation");

        c.setSkipped(true);
        conversationRepository.save(conversation.get());
    }

    public void closeConversation(UserConversationForm form) {
        Optional<C> conversation = conversationRepository.findByIdAndUserId(form.getConversationId(), form.getUserId());
        if (!conversation.isPresent())
            throw new RuntimeException("Conversation not found");

        C c = conversation.get();

        if (!c.isStarted())
            throw new RuntimeException("Conversation not started yet");

        if (c.isEnded())
            throw new RuntimeException("Conversation already ended");

        c.setEnded(true);
        conversationRepository.save(c);
    }

    public void startConversation(C conversation) {
        conversation.setStarted(true);
        conversation.setStartedAt(LocalDateTime.now());
        conversationRepository.save(conversation);
    }

    /*
     * public VisitGraphForm buildVisitGraphForm(C conversation) {
     * VisitGraphForm form = new VisitGraphForm();
     * form.setGraphId(conversation.getGraphId());
     * form.setTitle(conversation.getTaskTitle());
     * form.setDescription(conversation.getTaskTitle());
     * form.setTaskStatus(conversation.getStatus());
     * if(conversation.getScheduleStatus() != null)
     * form.setScheduleStatus(conversation.getScheduleStatus());
     * else
     * form.setScheduleStatus(ScheduleStatus.BehindSchedule);
     * return form;
     * }
     */

    public void setup(C conversation, GraphNode node) {
        switch (node.getType()) {
            case QUESTION:
                GraphLoaderService.setup(conversation.getGraphId(), conversation.getCommitId(), node);
                break;
            case GOOGLE_TAXONOMY:
                GraphLoaderService.setup(conversation.getGraphId(), conversation.getCommitId(), node);
                break;
        }
    }

    public ConversationResponseDto buildResponse(C conversation, GraphNode node) {
        setup(conversation, node);
        ConversationResponseDto response = new ConversationResponseDto();
        response.setConversationId(conversation.getId());
        response.setResponse(VisitorResponseDto.of(node));
        return response;
    }

    /*
     * public GraphNode getGraphStartNode(Long graphId) throws
     * MissingEntityException {
     * GraphEntity graph = graphVisitorService.getGraph(graphId);
     * GraphNode startNode = graphVisitorService.getStartNode(graph);
     * if(startNode.getConnections() == null ||
     * startNode.getConnections().isEmpty())
     * throw new
     * IllegalArgumentException("Please check the graph quality : There is no next node for the start node."
     * );
     * return startNode.getConnections().get(0);
     * }
     */

    public GraphNode getConversationStartNode(Long graphId, Long commitId) throws MissingEntityException {
        StartNode startNode = GraphLoaderService.getStartNode(graphId, commitId);
        List<GraphNode> connections = GraphLoaderService.getConnections(graphId, commitId, startNode);
        return connections.get(0);
    }

    public GraphNode getConversationStartNode(C conversation) throws MissingEntityException {
        return getConversationStartNode(conversation.getGraphId(), conversation.getCommitId());
    }

    public ConversationResponseDto startConversation(UserConversationForm form)
            throws MessageException, MissingEntityException {
        Optional<C> conversation = conversationRepository.findByIdAndUserId(form.getConversationId(), form.getUserId());
        if (!conversation.isPresent())
            throw new RuntimeException("Conversation not found");
        C c = conversation.get();
        startConversation(c);
        // VisitGraphForm visitGraphForm = buildVisitGraphForm(c);
        // GraphNode node = graphVisitorService.visitGraph(visitGraphForm);
        GraphNode node = getConversationStartNode(c);
        ConversationResponseDto response = buildResponse(c, node);
        messageService.saveMessage(c, response.getResponse());
        return response;
    }

    public TraverseGraphForm buildTraverseGraphForm(C conversation, ContinueConversationForm req) {
        TraverseGraphForm form = new TraverseGraphForm();
        form.setGraphId(conversation.getGraphId());
        /*
         * form.setTitle(conversation.getTaskTitle());
         * form.setDescription(conversation.getTaskTitle());
         * form.setTaskStatus(conversation.getStatus());
         * if(conversation.getScheduleStatus() != null)
         * form.setScheduleStatus(conversation.getScheduleStatus());
         * else
         * form.setScheduleStatus(ScheduleStatus.BehindSchedule);
         */
        form.setNodeId(req.getNodeId());
        form.setAnswer(req.getAnswer());
        return form;
    }

    public void checkIfConversationIsEnded(C conversation, GraphNode node) {
        if (node.getType().equals(NodeType.END)) {
            conversation.setEnded(true);
            conversationRepository.save(conversation);
        }

        if (node.getType().equals(NodeType.STATEMENT)) {
            List<GraphNode> connections = GraphLoaderService.getConnections(conversation.getGraphId(), conversation.getCommitId(), node);
            if (connections != null && !connections.isEmpty()) {
                GraphNode next = connections.get(0);
                if (next.getType().equals(NodeType.END)) {
                    conversation.setEnded(true);
                    conversationRepository.save(conversation);
                }
            }
        }
    }

    public ConversationResponseDto replyToConversation(ContinueConversationForm form, String answer)
            throws MessageException, MissingEntityException {
        Optional<C> conversation = conversationRepository.findByIdAndUserId(form.getConversationId(), form.getUserId());
        if (!conversation.isPresent())
            throw new RuntimeException("Conversation not found");
        C c = conversation.get();
        //check and set the response
        form.setAnswer(answer);
        messageService.setQuestionASAnswered(c.getUserId(), c.getId());
        messageService.saveMessage(c, form.getAnswer());
        TraverseGraphForm tgForm = buildTraverseGraphForm(c, form);
        GraphNode node = graphVisitorService.traverseGraph(c, tgForm);
        checkIfConversationIsEnded(c, node);
        ConversationResponseDto response = buildResponse(c, node);
        messageService.saveMessage(c, response.getResponse());
        return response;
    }

    public C getConversation(UserConversationForm form) throws MessageException, MissingEntityException {
        Optional<C> conversation = conversationRepository.findByIdAndUserId(form.getConversationId(), form.getUserId());
        if (!conversation.isPresent())
            throw new RuntimeException("Conversation not found");
        C c = conversation.get();
        List<Message> messages = messageService.fetchMessages(c);
        c.setMessages(messages);
        return c;
    }

    public void backConversation(UserConversationForm form) throws MessageException, MissingEntityException {
        Optional<C> conversation = conversationRepository.findByIdAndUserId(form.getConversationId(), form.getUserId());
        if (!conversation.isPresent())
            throw new RuntimeException("Conversation not found");
        messageService.deleteLatestMessage(form.getUserId(), form.getConversationId());
    }

    public UserConversation addUserConversation(CreateUserConversationForm form)
            throws MessageException, MissingEntityException {
        UserConversation conversation = new UserConversation();
        conversation.setUserId(form.getUserId());
        conversation.setGraphId(form.getGraphId());
        conversation.setCommitId(form.getCommitId());
        conversation.setCreatedAt(LocalDateTime.now());
        conversation.setStarted(false);
        conversation.setEnded(false);
        return userConversationRepository.save(conversation);
    }

    public void checkIfGraphExists(Long graphId, Long commitId) throws MissingEntityException {
        boolean exists = GraphLoaderService.checkIfGraphExists(graphId, commitId);
        if (!exists)
            throw new MissingEntityException("Graph not found with id: " + graphId + " and commit id: " + commitId);
    }

    public UserConversation createUserConversation(CreateUserConversationForm form)
            throws MessageException, MissingEntityException {
        checkIfGraphExists(form.getGraphId(), form.getCommitId());
        UserConversation conversation = addUserConversation(form);
        return conversation;
    }

}
