package com.aimanager.agent.repository;

import com.aimanager.agent.models.CFData;
import org.springframework.data.cassandra.repository.CassandraRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CFDataRepository extends CassandraRepository<CFData,CFData.KeyClass> {
    List<CFData> findById_Node(long node);
}
