package com.aimanager.agent.services;

import java.util.Arrays;
import java.util.Optional;

import com.aimanager.agent.repositories.AnswerNodeRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.aimanager.agent.Form.CreateChoicesQuestionForm;
import com.aimanager.agent.Form.CreateQuestionForm;
import com.aimanager.agent.Form.UpdateQuestionNodeForm;
import com.aimanager.agent.exceptions.MissingEntityException;
import com.aimanager.agent.models.GraphEntity;
import com.aimanager.agent.models.NodeStatus;
import com.aimanager.agent.models.QuestionNode;
import com.aimanager.agent.models.QuestionType;
import com.aimanager.agent.models.AnswerType;
import com.aimanager.agent.repositories.GraphRepository;
import com.aimanager.agent.repositories.QuestionNodeRepository;

@Service
public class QuestionNodeService {

    private static final Logger logger = LoggerFactory.getLogger(QuestionNodeService.class);

    @Autowired
    GraphRepository graphRepository;

    @Autowired
    QuestionNodeRepository questionNodeRepository;

    @Autowired
    AnswerNodeRepository answerNodeRepository;

    @Autowired
    AnswerNodeService answerNodeService;

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

    /*
     * Creates a yes/no question
     * @param form The form containing question details
     * @return The created yes/no question
     * @throws MissingEntityException if the question type is invalid or required fields are missing
     */
    public QuestionNode addQuestion(CreateQuestionForm form, QuestionType questionType, AnswerType answerType) throws MissingEntityException {
        GraphEntity graph = getGraphEntity(form.getGraphId());
        QuestionNode question = new QuestionNode(form.getQuestionText(),questionType);
        question.setAnswerType(answerType);
        question.setGraph(graph);
        question.setStatus(NodeStatus.ACTIVE);
        question = questionNodeRepository.save(question);
        commitService.addGraphCommit(graph, question, "Add "+questionType+" question: "+question.getQuestionText());
        return question;
    }

    /*
     * Creates a yes/no question
     * @param form The form containing question details
     * @return The created yes/no question
     * @throws MissingEntityException if the question type is invalid or required fields are missing
     */
    public QuestionNode addYesNoQuestion(CreateQuestionForm form) throws MissingEntityException {
       /* CreateChoicesQuestionForm choicesForm = new CreateChoicesQuestionForm();
        choicesForm.setGraphId(form.getGraphId());
        choicesForm.setQuestionText(form.getQuestionText());
        choicesForm.setQuestionType(QuestionType.YES_NO);
        choicesForm.setChoices(Arrays.asList("Yes", "No"));
        return createQuestion(choicesForm);*/
        QuestionNode question = addQuestion(form, QuestionType.YES_NO, AnswerType.TEXT);
        answerNodeService.createResponseNodes(question, Arrays.asList("Yes", "No"));
        return questionNodeRepository.save(question);
    }


    /*
     * Creates a question node and its response nodes
     * @param form The form containing question details
     * @return The created question node
     * @throws MissingEntityException if the question type is invalid or required fields are missing
     */
    public QuestionNode createMultipleChoicesQuestion(CreateChoicesQuestionForm form) throws MissingEntityException {
        QuestionNode question = addQuestion(form, QuestionType.MULTIPLE_CHOICE,form.getResponseType());
       // answerNodeService.createResponseNodes(question, form.getChoices());
        return question;
    }

       /*
     * Creates a question node and its response nodes
     * @param form The form containing question details
     * @return The created question node
     * @throws MissingEntityException if the question type is invalid or required fields are missing
     */
    public QuestionNode createSingleChoiceQuestion(CreateChoicesQuestionForm form) throws MissingEntityException {
        QuestionNode question = addQuestion(form, QuestionType.SINGLE_CHOICE,form.getResponseType());
        //answerNodeService.createResponseNodes(question, form.getChoices());
        return question;
    }
    
    
    /*
     * Retrieves a question node by its ID and graph
     * @param questionId The ID of the question to retrieve
     * @param graph The graph containing the question
     * @return The retrieved question node
     * @throws MissingEntityException if the question is not found in the specified graph
     */
    public QuestionNode getQuestionById(Long questionId, GraphEntity graph) throws MissingEntityException {
        // Try to find the question in both repositories
        Optional<QuestionNode> question = questionNodeRepository.findByIdAndGraph(questionId, graph);
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
    public QuestionNode getQuestionById(Long graphId, Long questionId) throws MissingEntityException {
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
    public Page<QuestionNode> getQuestionsPaginated(Long graphId, Pageable pageable) throws MissingEntityException {
        GraphEntity graph = getGraphEntity(graphId);
        return questionNodeRepository.findByGraph(graph, pageable);
    }

    public void checkIfQuestionExists(GraphEntity graph, String questionText) throws MissingEntityException {
      if(questionNodeRepository.existsByGraphAndQuestionText(graph, questionText)){
        throw new MissingEntityException("Question already exists for the specified graph");
      }
    }

    /*
     * Updates a question node
     * @param form The form containing question details
     * @return The updated question node
     * @throws MissingEntityException if the question is not found in the specified graph
     */
    public QuestionNode updateQuestion(UpdateQuestionNodeForm form) throws MissingEntityException {
        GraphEntity graph = getGraphEntity(form.getGraphId());
        QuestionNode question = getQuestionById(form.getGraphId(), form.getQuestionId());
        checkIfQuestionExists(graph, form.getQuestion());
        question.setQuestionText(form.getQuestion());
        return questionNodeRepository.save(question);
    }

}
