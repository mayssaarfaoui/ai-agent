package com.aimanager.agent.services;

import com.aimanager.agent.Form.CreateQuestionWithFFForm;
import com.aimanager.agent.Form.ServiceDetailsForm;
import com.aimanager.agent.Form.UpdateQuestionWithFFForm;
import com.aimanager.agent.exceptions.MissingEntityException;
import com.aimanager.agent.models.*;
import com.aimanager.agent.repositories.GraphRepository;
import com.aimanager.agent.repositories.QuestionWithFreeFormNodeRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class QuestionWithFreeFormNodeService {

    private static final Logger logger = LoggerFactory.getLogger(QuestionWithFreeFormNodeService.class);

    @Autowired
    GraphRepository graphRepository;

    @Autowired
    QuestionWithFreeFormNodeRepository questionWithFreeFormNodeRepository;

    @Autowired
    CommitService commitService;

    /*
     * Retrieves a graph entity by its ID
     * @param graphId The ID of the graph to retrieve
     * @return The retrieved graph entity
     * @throws MissingEntityException if the graph is not found
     */
    public GraphEntity getGraphEntity(Long graphId) throws MissingEntityException {
        Optional<GraphEntity> graphEntity = graphRepository.findById(graphId);
        if (!graphEntity.isPresent()) {
            throw new MissingEntityException("Graph not found");
        }
        return graphEntity.get();
    }

    /**
     * Check service details
     * @param service
     */
    public void checkServiceDetails(ServiceDetailsForm service){
        List<String> err = ServiceDetailsValidator.validate(service);
        if(!err.isEmpty())
            throw new IllegalArgumentException("Check service details : "+err.toString());
    }

    /**
     * Set Free Form Question's service
     * @param question
     * @param form
     * @param service
     */
    public void setQuestionFreeFormDetails( QuestionWithFreeFormNode question, CreateQuestionWithFFForm form, ServiceDetailsForm service){
        if(form.isSendResponse())
            checkServiceDetails(service);
        question.setQuestionText(form.getQuestionText());
        question.setSendResponse(form.isSendResponse());
        question.setSendType(service.getSendType());
        question.setResponseParameterName(service.getResponseParameterName());
        question.setSendTo(service.getSendTo());
        question.setHeaders(service.getHeaders());
    }

    /*
     * Creates a yes/no question
     * @param form The form containing question details
     * @return The created yes/no question
     * @throws MissingEntityException if the question type is invalid or required fields are missing
     */
    public QuestionWithFreeFormNode addQuestion(CreateQuestionWithFFForm form, ServiceDetailsForm service) throws MissingEntityException {
        GraphEntity graph = getGraphEntity(form.getGraphId());
        QuestionWithFreeFormNode question = new QuestionWithFreeFormNode(form.getQuestionText());
        question.setGraph(graph);
        question.setStatus(NodeStatus.ACTIVE);
        setQuestionFreeFormDetails(question,form,service);
        question = questionWithFreeFormNodeRepository.save(question);
        commitService.addGraphCommit(graph, question, "Add Free Form question: "+question.getQuestionText());
        return question;
    }
    
    
    /*
     * Retrieves a question node by its ID and graph
     * @param questionId The ID of the question to retrieve
     * @param graph The graph containing the question
     * @return The retrieved question node
     * @throws MissingEntityException if the question is not found in the specified graph
     */
    public QuestionWithFreeFormNode getQuestionById(Long questionId, GraphEntity graph) throws MissingEntityException {
        // Try to find the question in both repositories
        Optional<QuestionWithFreeFormNode> question = questionWithFreeFormNodeRepository.findByIdAndGraph(questionId, graph);
        if (question.isPresent()) {
            return question.get();
        }
        else {
            throw new MissingEntityException("Question not found in the specified graph");
        }   
    }

    /*
     * Retrieves a question node by its ID and graph ID
     * @param graphId The ID of the graph containing the question
     * @param questionId The ID of the question to retrieve
     * @return The retrieved question node
     * @throws MissingEntityException if the question is not found in the specified graph
     */
    public QuestionWithFreeFormNode getQuestionById(Long graphId, Long questionId) throws MissingEntityException {
        GraphEntity graph = getGraphEntity(graphId);
       return getQuestionById(questionId, graph);
    }

    /*
     * Retrieves paginated questions for a specific graph
     * @param graphId The ID of the graph
     * @param pageRequest The page request containing pagination details
     * @return The paginated list of questions
     * @throws MissingEntityException if the graph is not found
     */
    public Page<QuestionWithFreeFormNode> getQuestionsPaginated(Long graphId, Pageable pageable) throws MissingEntityException {
        GraphEntity graph = getGraphEntity(graphId);
        return questionWithFreeFormNodeRepository.findByGraph(graph, pageable);
    }

    public void checkIfQuestionExists(GraphEntity graph, String questionText) throws MissingEntityException {
      if(questionWithFreeFormNodeRepository.existsByGraphAndQuestionText(graph, questionText)){
        throw new MissingEntityException("Question already exists for the specified graph");
      }
    }

    /*
     * Updates a question node
     * @param form The form containing question details
     * @return The updated question node
     * @throws MissingEntityException if the question is not found in the specified graph
     */
    public QuestionWithFreeFormNode updateQuestion(UpdateQuestionWithFFForm form, ServiceDetailsForm service) throws MissingEntityException {
        GraphEntity graph = getGraphEntity(form.getGraphId());
        QuestionWithFreeFormNode question = getQuestionById(form.getGraphId(), form.getQuestionId());
        checkIfQuestionExists(graph, form.getQuestionText());
        setQuestionFreeFormDetails(question,form,service);
        return questionWithFreeFormNodeRepository.save(question);
    }

}
