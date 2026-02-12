package com.aimanager.agent.graph.node;/* see LICENSE file in the root */

// Abstract class for a node in a graph

import com.aimanager.agent.models.BaseEntity;
import com.aimanager.agent.models.GraphEntity;
import com.aimanager.agent.models.NodeType;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.CascadeType;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorType;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

@Getter
@Setter
@Entity
@Table(schema = "aimanager", name = "node")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "node_type", discriminatorType = DiscriminatorType.STRING, columnDefinition = "VARCHAR(255)")
public abstract class Node extends BaseEntity {
    
    @Enumerated(EnumType.STRING)
    protected NodeType type;

    @ManyToOne
    @JoinColumn(name = "graph_id",referencedColumnName = "id")
    protected GraphEntity graph;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "next_node_id", referencedColumnName = "id")
    protected Node nextNode;
   
    @Transient
    protected String label;

    public Node() {
    }

    public Node(NodeType type) {
        this.type = type;
        String t = type.getName() + this.id;
        this.label = t;
    }

    public Node getNextNode() {
       /* if (nextNode == null && type != NodeType.TERMINAL) {
            throw new IllegalArgumentException("Next node is not set for node with id: " + this.id);
        }*/
        return nextNode;
    }

}