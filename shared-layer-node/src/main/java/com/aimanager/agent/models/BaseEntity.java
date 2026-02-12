package com.aimanager.agent.models;

import java.util.Objects;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.SequenceGenerator;
import javax.persistence.Transient;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

// Base entity for all entities in the system
// This is used to generate a global id for all entities
// CREATE SEQUENCE global_id_sequence START WITH 1 INCREMENT BY 1;

@MappedSuperclass
public abstract class BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "global_id_generator")
    @SequenceGenerator(name = "global_id_generator", sequenceName = "global_id_sequence", allocationSize = 1)
    protected Long id;

    @Transient
    protected Logger logger = LoggerFactory.getLogger(BaseEntity.class);

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        BaseEntity that = (BaseEntity) obj;
        return id != null && id.equals(that.id);    
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }   
    
}
