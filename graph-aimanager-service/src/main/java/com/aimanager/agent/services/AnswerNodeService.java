package com.aimanager.agent.services;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.aimanager.agent.Form.answers.CreateDateAnswerForm;
import com.aimanager.agent.Form.answers.CreateFileAnswerForm;
import com.aimanager.agent.Form.answers.CreateNumberAnswerForm;
import com.aimanager.agent.Form.answers.CreateTextAnswerForm;
import com.aimanager.agent.Form.answers.EditDateAnswerForm;
import com.aimanager.agent.Form.answers.EditFileAnswerForm;
import com.aimanager.agent.Form.answers.EditNumberAnswerForm;
import com.aimanager.agent.Form.answers.EditTextAnswerForm;
import com.aimanager.agent.files.FileStorageService;
import com.aimanager.agent.models.AnswerType;
import com.aimanager.agent.models.DateAnswer;
import com.aimanager.agent.models.FileAnswer;
import com.aimanager.agent.models.GraphEdge;
import com.aimanager.agent.models.AnswerNode;
import com.aimanager.agent.models.GraphEntity;
import com.aimanager.agent.models.NodeStatus;
import com.aimanager.agent.models.NumberAnswer;
import com.aimanager.agent.models.QuestionNode;
import com.aimanager.agent.models.QuestionType;
import com.aimanager.agent.models.TextAnswer;
import com.aimanager.agent.models.commits.Commit;
import com.aimanager.agent.repositories.AnswerNodeRepository;
import com.aimanager.agent.repositories.DateAnswerRepository;
import com.aimanager.agent.repositories.FileAnswerRepository;
import com.aimanager.agent.repositories.GraphRepository;
import com.aimanager.agent.repositories.NumberAnswerRepository;
import com.aimanager.agent.repositories.QuestionNodeRepository;
import com.aimanager.agent.repositories.TextAnswerRepository;
@Service
public class AnswerNodeService<T extends AnswerNode> {

    private static final Logger logger = LoggerFactory.getLogger(AnswerNodeService.class);

    @Value("${com.aimanager.limit.uploaded.item.files}")
    private int limit;

    @Autowired
    private AnswerNodeRepository<T> answerNodeRepository;

    @Autowired
    private GraphRepository graphRepository;

    @Autowired
    private QuestionNodeRepository questionNodeRepository;

    @Autowired
    private TextAnswerRepository textAnswerRepository;

    @Autowired
    private NumberAnswerRepository numberAnswerRepository;

    @Autowired
    private FileAnswerRepository fileAnswerRepository;

    @Autowired
    private DateAnswerRepository dateAnswerRepository;

    @Autowired
    private FileStorageService fileStorageService;

    @Autowired
    private GraphEdgeService graphEdgeService;

    @Autowired
    private CommitService commitService;


    public T getAnswerNode(Long answerId, GraphEntity graph) {
        return answerNodeRepository.findByIdAndGraph(answerId, graph).orElseThrow(
            () -> new RuntimeException("AnswerNode not found with id: " + answerId)
        );
    }

    public T getAnswerNode(Long id,Long questionId,Long graphId) {
        GraphEntity graph = getGraphById(graphId);
        QuestionNode question = getQuestionNodeById(questionId, graph);
        return answerNodeRepository.findByIdAndQuestion(id, question).orElseThrow(
            () -> new RuntimeException("AnswerNode not found with id: " + id)
        );
    }


    public GraphEntity getGraphById(Long id) {
        return graphRepository.findById(id).orElseThrow(
            () -> new RuntimeException("Graph not found with id: " + id)
        );
    }

    public QuestionNode getQuestionNodeById(Long id,GraphEntity graph) {
        return questionNodeRepository.findByIdAndGraph(id, graph).orElseThrow(
            () -> new RuntimeException("QuestionNode not found with id: " + id+" and graph id: "+graph.getId())
        );
    }

    public Page<T> findAnswersByQuestionId(Long graphId, Long questionId, Pageable pageable) {
        GraphEntity graph = getGraphById(graphId);
        QuestionNode question = getQuestionNodeById(questionId, graph);
        return answerNodeRepository.findByQuestion(question, pageable);
    }


    public void addResponseNodes(QuestionNode questionNode, List<String> options) throws IllegalArgumentException {
        logger.info("Adding response nodes to question node: {} with options: {}", questionNode.getId(), options);
        List<TextAnswer> answers = new ArrayList<>();
        List<GraphEdge> edges = new ArrayList<>();
        for(String option : options){
            TextAnswer answerNode = new TextAnswer();
            answerNode.setStatus(NodeStatus.ACTIVE);
            answerNode.setQuestion(questionNode);
            answerNode.setAnswer(option);
            answerNode.setGraph(questionNode.getGraph());
            answerNode = textAnswerRepository.save(answerNode);
            answers.add(answerNode);
            GraphEdge edge =  graphEdgeService.createEdge(questionNode, answerNode);
            edges.add(edge);
        }
        commitService.addToCurrentCommit(questionNode.getGraph(), answers);
        commitService.addEdgesToCurrentCommit(questionNode.getGraph(), edges);
    }


        /*
     * Creates response nodes for a question node
     * @param questionNode The parent question node
     * @param options List of options/choices for the question
     * @return List of created response nodes
     */
    public void createResponseNodes(QuestionNode questionNode, List<String> options) throws IllegalArgumentException {
        logger.info("Adding response nodes to question node: {} with type: {}", questionNode.getId(), questionNode.getQuestionType());
       if(questionNode.getQuestionType().equals(QuestionType.YES_NO)){
        List<String> yesNoOptions = Arrays.asList("Yes", "No");
        addResponseNodes(questionNode, yesNoOptions);
       }
       else{
        addResponseNodes(questionNode, options);
       }
    }

    /*
     * Checks if the question is a yes/no question
     * @param answerNode The answer node to check
     * @throws IllegalArgumentException if the question is a yes/no question
     */
    public void checkIfIsYesNoQuestion(QuestionNode questionNode) {
       if(questionNode.getQuestionType().equals(QuestionType.YES_NO)){
      throw new IllegalArgumentException("You can't update answers for a yes/no question.");
       }
    }

    /*
     * Checks if the answer already exists for the question
     * @param questionNode The question node
     * @param answer The answer to check
     * @throws IllegalArgumentException if the answer already exists
     */
    public void checkIfAnswerAlreadyExists(QuestionNode questionNode,String answer) {
        if(textAnswerRepository.existsByQuestionAndAnswer(questionNode, answer)){
            throw new IllegalArgumentException("Answer already exists.");
        }
    }

    /*
     * Adds a response node to a question node
     * @param questionNode The parent question node
     * @param answer The answer to add
     * @return The added answer node
     */
    public TextAnswer addTextAnswerNode(QuestionNode questionNode, String answer) {
        TextAnswer textAnswer = new TextAnswer();
        textAnswer.setStatus(NodeStatus.ACTIVE);
        textAnswer.setQuestion(questionNode);
        textAnswer.setAnswer(answer);
        textAnswer.setGraph(questionNode.getGraph());
        return textAnswerRepository.save(textAnswer);
    }

    /*
     * Checks if the answer type is valid for the question
     * @param questionNode The question node
     * @param answerType The answer type to check
     * @throws IllegalArgumentException if the answer type is not valid
     */
    public void checkSemanticAnswerType(QuestionNode questionNode,AnswerType answerType){
        if(!questionNode.getAnswerType().equals(answerType)){
            throw new IllegalArgumentException("Answer type is not valid for this question. "+
            "Expected: "+questionNode.getAnswerType()+" but got: "+answerType);
        }
    }

    /*
     * Checks if the answer count limit is reached for the question
     * @param questionNode The question node
     * @throws IllegalArgumentException if the answer count limit is reached
     */
    public void checkAnswerCountLimit(QuestionNode questionNode){
        int answerCount = answerNodeRepository.countByQuestion(questionNode);
       if(answerCount >= limit){
        throw new IllegalArgumentException("Answers count limit reached for this question. You can not add more answers.");
       }
    }

    /*
     * Creates an answer node
     * @param form The create answer node form
     * @return The created answer node
     */
    public TextAnswer createTextAnswerNode(CreateTextAnswerForm form) {
        GraphEntity graph = getGraphById(form.getGraphId());
        QuestionNode questionNode = getQuestionNodeById(form.getQuestionId(), graph);
        checkIfIsYesNoQuestion(questionNode);
        checkSemanticAnswerType(questionNode,AnswerType.TEXT);
        checkIfAnswerAlreadyExists(questionNode, form.getAnswer());
        checkAnswerCountLimit(questionNode);
        Commit commit = commitService.initiateCommit(graph);
        commit.setLabel("Add text answer: "+form.getAnswer());
        QuestionNode copiedQuestionNode = (QuestionNode) commitService.copyNode(questionNode);
        commitService.copyEdgesAndReplaceNode(commit, questionNode, copiedQuestionNode);
        commitService.removeFromCurrentCommit(commit, questionNode);
        TextAnswer addedAnswerNode = addTextAnswerNode(copiedQuestionNode, form.getAnswer());
        GraphEdge edge = graphEdgeService.createEdge(copiedQuestionNode, addedAnswerNode);
        commitService.addToCurrentCommit(commit,copiedQuestionNode);
        commitService.addToCurrentCommit(commit, addedAnswerNode);
        commitService.addToCurrentCommit(commit, edge);
        commitService.saveCommit(commit);
        return addedAnswerNode;
    }

    /*
     * Adds a number answer node to a question node
     * @param questionNode The parent question node
     * @param answer The answer to add
     * @return The added number answer node
     */
    public NumberAnswer addNumberAnswerNode(QuestionNode questionNode, Double answer) {
        NumberAnswer numberAnswer = new NumberAnswer();
        numberAnswer.setStatus(NodeStatus.ACTIVE);
        numberAnswer.setQuestion(questionNode);
        numberAnswer.setAnswerNumber(answer);
        numberAnswer.setGraph(questionNode.getGraph());
        return numberAnswerRepository.save(numberAnswer);
    }

    /*
     * Checks if the number answer already exists for the question
     * @param questionNode The question node
     * @param answer The answer to check
     * @throws IllegalArgumentException if the answer already exists
     */
    public void checkIfNumberAnswerAlreadyExists(QuestionNode questionNode,Double answer) {
        if(numberAnswerRepository.existsByQuestionAndAnswerNumber(questionNode, answer)){
            throw new IllegalArgumentException("Answer already exists.");
        }
    }

    /*
     * Creates a number answer node
     * @param form The create number answer node form
     * @return The created number answer node
     */
    public NumberAnswer createNumberAnswerNode(CreateNumberAnswerForm form) {
        GraphEntity graph = getGraphById(form.getGraphId());
        QuestionNode questionNode = getQuestionNodeById(form.getQuestionId(), graph);
        checkSemanticAnswerType(questionNode,AnswerType.NUMBER);
        checkIfNumberAnswerAlreadyExists(questionNode, form.getAnswer());
        checkAnswerCountLimit(questionNode);
        Commit commit = commitService.initiateCommit(graph);
        commit.setLabel("Add number answer: "+form.getAnswer());
        QuestionNode copiedQuestionNode = (QuestionNode) commitService.copyNode(questionNode);
        commitService.copyEdgesAndReplaceNode(commit, questionNode, copiedQuestionNode);
        commitService.removeFromCurrentCommit(commit, questionNode);
        NumberAnswer addedAnswerNode = addNumberAnswerNode(copiedQuestionNode, form.getAnswer());
        GraphEdge edge = graphEdgeService.createEdge(copiedQuestionNode, addedAnswerNode);
        commitService.addToCurrentCommit(commit,copiedQuestionNode);
        commitService.addToCurrentCommit(commit, addedAnswerNode);
        commitService.addToCurrentCommit(commit, edge);
        commitService.saveCommit(commit);
        return addedAnswerNode;
    }

     /*
     * Gets the path of the file answer
     * @param file The file to get the path from
     * @return The path of the file
     */
    public void setFileAnswerPath(FileAnswer fileAnswer, MultipartFile file){
        try {
            String filePath = fileStorageService.uploadFile(file);
            fileAnswer.setAnswerFilePath(filePath);
        } catch (IOException e) {
            throw new RuntimeException("Failed to upload file: " + e.getMessage());
        }
    }

    /*
     * Creates a file answer node
     * @param form The create file answer node form
     * @return The created file answer node
     */ 
    public FileAnswer   addFileAnswerNode(QuestionNode questionNode, MultipartFile file) { 
        FileAnswer fileAnswer = new FileAnswer();
        fileAnswer.setStatus(NodeStatus.ACTIVE);
        fileAnswer.setQuestion(questionNode);
        fileAnswer.setGraph(questionNode.getGraph());
        setFileAnswerPath(fileAnswer, file);
        return fileAnswerRepository.save(fileAnswer);
    }

    /*
     * Creates a file answer node
     * @param form The create file answer node form
     * @return The created file answer node
     */ 
    public FileAnswer createFileAnswerNode(CreateFileAnswerForm form) {
        GraphEntity graph = getGraphById(form.getGraphId());
        QuestionNode questionNode = getQuestionNodeById(form.getQuestionId(), graph);
        checkSemanticAnswerType(questionNode,AnswerType.FILE);
        checkAnswerCountLimit(questionNode);
        Commit commit = commitService.initiateCommit(graph);
        commit.setLabel("Add file answer: "+form.getFile().getOriginalFilename());
        QuestionNode copiedQuestionNode = (QuestionNode) commitService.copyNode(questionNode);
        commitService.removeFromCurrentCommit(commit, questionNode);
        FileAnswer addedAnswerNode = addFileAnswerNode(copiedQuestionNode, form.getFile());
        GraphEdge edge = graphEdgeService.createEdge(copiedQuestionNode, addedAnswerNode);
        commitService.addToCurrentCommit(commit,copiedQuestionNode);
        commitService.addToCurrentCommit(commit, addedAnswerNode);
        commitService.addToCurrentCommit(commit, edge);
        commitService.saveCommit(commit);
        return addedAnswerNode;
    }

    /*
     * Adds a date answer node to a question node
     * @param questionNode The parent question node
     * @param answer The answer to add
     * @return The added date answer node
     */
    public DateAnswer addDateAnswerNode(QuestionNode questionNode, Calendar answer) {
        DateAnswer dateAnswer = new DateAnswer();
        dateAnswer.setStatus(NodeStatus.ACTIVE);
        dateAnswer.setQuestion(questionNode);
        dateAnswer.setAnswerDate(answer);
        dateAnswer.setGraph(questionNode.getGraph());
        return dateAnswerRepository.save(dateAnswer);
    }

    /*
     * Checks if the date answer already exists for the question
     * @param questionNode The question node
     * @param answer The answer to check
     * @throws IllegalArgumentException if the answer already exists
     */
    public void checkIfDateAnswerAlreadyExists(QuestionNode questionNode,Calendar answer) {
        if(dateAnswerRepository.existsByQuestionAndAnswerDate(questionNode, answer)){
            throw new IllegalArgumentException("Answer already exists.");
        }
    }

    /*
     * Converts a string to a date
     * @param dateString The date string to convert
     * @return The converted date
     */

    public Calendar convertStringToDate(String dateString) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
            Date date = sdf.parse(dateString);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            return calendar;
        } catch (ParseException e) {
            throw new RuntimeException("Error while parsing the due date");
        }
    }

    /*
     * Creates a date answer node
     * @param form The create date answer node form
     * @return The created date answer node
     */
    public DateAnswer createDateAnswerNode(CreateDateAnswerForm form) {
        GraphEntity graph = getGraphById(form.getGraphId());
        QuestionNode questionNode = getQuestionNodeById(form.getQuestionId(), graph);
        checkSemanticAnswerType(questionNode,AnswerType.DATE);
        Calendar date = convertStringToDate(form.getAnswerDate());
        checkIfDateAnswerAlreadyExists(questionNode, date);
        checkAnswerCountLimit(questionNode);
        Commit commit = commitService.initiateCommit(graph);
        commit.setLabel("Add date answer: "+form.getAnswerDate());
        QuestionNode copiedQuestionNode = (QuestionNode) commitService.copyNode(questionNode);
        commitService.removeFromCurrentCommit(commit, questionNode);
        DateAnswer addedAnswerNode = addDateAnswerNode(copiedQuestionNode, date);
        GraphEdge edge = graphEdgeService.createEdge(copiedQuestionNode, addedAnswerNode);
        commitService.addToCurrentCommit(commit,copiedQuestionNode);
        commitService.addToCurrentCommit(commit, addedAnswerNode);
        commitService.addToCurrentCommit(commit, edge);
        commitService.saveCommit(commit);
        return addedAnswerNode;
    }

    /*
     * Updates a text answer node
     * @param form The edit text answer node form
     * @return The updated text answer node
     */
    public TextAnswer updateTextAnswerNode(EditTextAnswerForm form) {
        GraphEntity graph = getGraphById(form.getGraphId());
        TextAnswer answer = (TextAnswer) getAnswerNode(form.getAnswerId(), graph);
        checkIfIsYesNoQuestion(answer.getQuestion());
        checkIfAnswerAlreadyExists(answer.getQuestion(), form.getAnswer());
        answer.setAnswer(form.getAnswer());
        return textAnswerRepository.save(answer);
    }

    /*
     * Updates a number answer node
     * @param form The edit number answer node form
     * @return The updated number answer node
     */
    public NumberAnswer updateNumberAnswerNode(EditNumberAnswerForm form) {
        GraphEntity graph = getGraphById(form.getGraphId());
        NumberAnswer answer = (NumberAnswer) getAnswerNode(form.getAnswerId(), graph);
        checkIfNumberAnswerAlreadyExists(answer.getQuestion(), form.getAnswer());
        answer.setAnswerNumber(form.getAnswer());
        return numberAnswerRepository.save(answer);
    }

    /*
     * Updates a file answer node
     * @param form The edit file answer node form
     * @return The updated file answer node
     */
    public FileAnswer updateFileAnswerNode(EditFileAnswerForm form) {
        GraphEntity graph = getGraphById(form.getGraphId());
        FileAnswer answer = (FileAnswer) getAnswerNode(form.getAnswerId(), graph);
        fileStorageService.deleteFile(answer.getAnswerFilePath());
        setFileAnswerPath(answer, form.getFile());
        return fileAnswerRepository.save(answer);
    }

    /*
     * Updates a date answer node
     * @param form The edit date answer node form
     * @return The updated date answer node
     */
    public DateAnswer updateDateAnswerNode(EditDateAnswerForm form) {
        GraphEntity graph = getGraphById(form.getGraphId());
        DateAnswer answer = (DateAnswer) getAnswerNode(form.getAnswerId(), graph);
        Calendar date = convertStringToDate(form.getAnswerDate());
        checkIfDateAnswerAlreadyExists(answer.getQuestion(), date);
        answer.setAnswerDate(date);
        return dateAnswerRepository.save(answer);
    }
}