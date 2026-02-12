package com.aimanager.agent.models;

import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorType;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.aimanager.agent.nodes.NodeContext;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(schema = "aimanager", name = "graph_nodes")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "node_type", discriminatorType = DiscriminatorType.STRING)

@JsonTypeInfo(
  use = JsonTypeInfo.Id.NAME,
  include = JsonTypeInfo.As.PROPERTY,
  property = "type"
)
@JsonSubTypes({
        @JsonSubTypes.Type(value = StartNode.class, name = "START"),
        @JsonSubTypes.Type(value = EndNode.class, name = "END"),
        @JsonSubTypes.Type(value = QuestionNode.class, name = "QUESTION"),
        @JsonSubTypes.Type(value = NumberAnswer.class, name = "NUMBER_ANSWER"),
        @JsonSubTypes.Type(value = TextAnswer.class, name = "TEXT_ANSWER"),
        @JsonSubTypes.Type(value = FileAnswer.class, name = "FILE_ANSWER"),
        @JsonSubTypes.Type(value = DateAnswer.class, name = "DATE_ANSWER"),
        @JsonSubTypes.Type(value = FetchDataNode.class, name = "FETCH_DATA_NODE"),
        @JsonSubTypes.Type(value = IteratorNode.class, name = "ITERATOR_NODE"),
        @JsonSubTypes.Type(value = DownStreamNode.class, name = "DOWNSTREAM_NODE"),
        @JsonSubTypes.Type(value = StatementNode.class, name = "STATEMENT_NODE"),
        @JsonSubTypes.Type(value = LLMNode.class, name = "LLM_NODE"),
        @JsonSubTypes.Type(value = GoogleTaxonomyNode.class, name = "GOOGLE_TAXONOMY_NODE"),
        @JsonSubTypes.Type(value = SubGraphNode.class, name = "SubGraphNode"),
        @JsonSubTypes.Type(value = CreateConversationNode.class, name = "CREATE_CONVERSATION"),
        @JsonSubTypes.Type(value = QuestionWithFreeFormNode.class, name = "QUESTION_FREE_FORM"),
        @JsonSubTypes.Type(value = NotificationNode.class, name = "NOTIFICATION"),
})
public class GraphNode extends BaseEntity implements Node {

    @Enumerated(EnumType.STRING)
    @Column(name = "n_type")
    protected NodeType type;

    @Transient
    protected String label;

    @ManyToOne
    @JoinColumn(name = "graph_id")
    private GraphEntity graph; 

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    protected NodeStatus status;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "replaced_by_node_id")
    protected GraphNode replacedBy;

    @JsonIgnore
    @OneToMany(mappedBy = "startNode")
    protected List<GraphEdge> outgoingEdges;

    @JsonIgnore
    @OneToMany(mappedBy = "targetNode")
    protected List<GraphEdge> incomingEdges;

    public GraphNode(NodeType type) {
        this.type = type; 
    }

    public GraphNode(NodeType type, Long id) {
        this.type = type;
        this.id = id;
    }

    public NodeType getType() {
        return type;
    }

        public Long getId() {
        return id;
    }

    // Setup function to be overridden by specific nodes
    @Override
    public void setup() {
        // this will be overridden by specific node types
        throw new UnsupportedOperationException("Setup method not implemented for node type: " + type);
    }

    // Abstract process method to be implemented by each node type
    @Override
    public void process(NodeContext context) {
        // Default implementation can be empty or throw an exception
        throw new UnsupportedOperationException("Process method not implemented for node type: " + type);
    }

    public String getLabel() {
        return type.getName()+"_"+id;
    }

    // Simulated UI interaction methods
    protected void sendToUI(String message) {
        System.out.println("[UI] " + message);
    }

    protected String receiveNodeIdFromUI() {
        Scanner scanner = new Scanner(System.in);
        return scanner.nextLine().trim();
    }

    // Traverses to the specified node ID if valid
    @Override
    public void traverse(String nodeId) {
        // Default implementation can be empty or throw an exception
        throw new UnsupportedOperationException("Traverse method not implemented for node type: " + type);
      /*  for (GraphNode node : connections) {
            if (node.getId().equals(nodeId)) {
                sendToUI("Navigating to node: " + nodeId);
                node.process();
                return;
            }
        }
        sendToUI("Invalid node selection. Please try again.");
        process(); // Re-ask the question if selection is invalid

       */
    }

    @JsonIgnore
    public List<GraphNode> getConnections() {
        return getOutgoingEdges().stream().map(GraphEdge::getTargetNode).collect(Collectors.toList());
    }

    @JsonIgnore
    public List<GraphNode> getParents() {
        return getIncomingEdges().stream().map(GraphEdge::getStartNode).collect(Collectors.toList());
    }

    @Override
    public void copyData(GraphNode clone) {
        clone.setType(this.type);
        clone.setGraph(this.graph);
        clone.setLabel(this.label);
        clone.setStatus(this.status);
        clone.setStatus(NodeStatus.ACTIVE);
    }

    @Override
    public GraphNode clone() {
        GraphNode clone = new GraphNode();
        copyData(clone);
        return clone;
    }
    
}
