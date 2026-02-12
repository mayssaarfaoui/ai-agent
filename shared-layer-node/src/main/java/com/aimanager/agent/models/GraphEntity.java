package com.aimanager.agent.models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;


import lombok.Getter;
import lombok.Setter;

@Entity
@Table(schema = "aimanager",name = "graph_node")
@Getter
@Setter
public class GraphEntity extends BaseEntity {

    @Column(name = "name")
    private String name;

    @Column(name = "description",columnDefinition = "TEXT")
    private String description;

}
