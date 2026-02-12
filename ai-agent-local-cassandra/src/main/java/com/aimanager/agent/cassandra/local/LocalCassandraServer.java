package com.aimanager.agent.cassandra.local;

import java.io.IOException;
import java.net.InetSocketAddress;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.apache.cassandra.exceptions.ConfigurationException;
import org.apache.thrift.transport.TTransportException;
import org.cassandraunit.utils.EmbeddedCassandraServerHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.datastax.oss.driver.api.core.CqlSession;
import com.datastax.oss.driver.api.core.cql.SimpleStatement;
import com.datastax.oss.driver.api.core.metadata.schema.ClusteringOrder;
import com.datastax.oss.driver.api.core.type.DataTypes;
import com.datastax.oss.driver.api.querybuilder.SchemaBuilder;

@Service
public class LocalCassandraServer  {
	
	private static final Logger log = LoggerFactory.getLogger(LocalCassandraServer.class);

	@Value("${spring.data.cassandra.keyspace-name}")
	private String keyspace;
	
	@Value("${spring.data.cassandra.contact-points}")
	private String contactPoints;
	
	@Value("${spring.data.cassandra.port}")
	private int port;
	
	@Value("${spring.data.cassandra.local-datacenter}")
	private String datacenter;
	
	@Value("${com.hippo.cassandra.local.config.file:cu-cassandra.yaml}")
	private String cassandraConfig;

	@PostConstruct
	public void setup() throws ConfigurationException, TTransportException, IOException, InterruptedException {
		
		log.info("Starting local agent memory cassandra server. Cassandra config: {} ...", cassandraConfig);
		
		EmbeddedCassandraServerHelper.startEmbeddedCassandra(cassandraConfig);

		try (CqlSession session = CqlSession.builder().addContactPoint(new InetSocketAddress("127.0.0.1", port))
				.withLocalDatacenter(datacenter).build()) {
			
			SimpleStatement keyspaceStatement = SchemaBuilder.createKeyspace(keyspace).ifNotExists().withSimpleStrategy(1).build();
			session.execute(keyspaceStatement);
			
			SimpleStatement tableStatement = SchemaBuilder.createTable(keyspace,"fetched").ifNotExists()
					.withPartitionKey("node", DataTypes.BIGINT)
					.withClusteringColumn("timestamp", DataTypes.TIMESTAMP)
					.withClusteringColumn("key", DataTypes.TEXT)
					.withColumn("value", DataTypes.TEXT)
					.withClusteringOrder("timestamp", ClusteringOrder.DESC)
					.build();
			session.execute(tableStatement);

			SimpleStatement conversationStatement = SchemaBuilder.createTable(keyspace,"conversations").ifNotExists()
					.withPartitionKey("user", DataTypes.BIGINT)
					.withPartitionKey("conversation", DataTypes.UUID)
					.withClusteringColumn("timestamp", DataTypes.TIMESTAMP)
					.withClusteringColumn("task_id", DataTypes.UUID)
					.withClusteringColumn("task_title", DataTypes.TEXT)
					.withClusteringColumn("task_status", DataTypes.TEXT)
					.withClusteringColumn("created_at", DataTypes.TIMESTAMP)
					.withColumn("started", DataTypes.BOOLEAN)
					.withClusteringOrder("timestamp", ClusteringOrder.DESC)
					.build();
			session.execute(conversationStatement);

			SimpleStatement messageStatement = SchemaBuilder.createTable(keyspace,"messages").ifNotExists()
					.withPartitionKey("user", DataTypes.BIGINT)
					.withPartitionKey("conversation", DataTypes.UUID)
					.withClusteringColumn("timestamp", DataTypes.TIMESTAMP)
					.withClusteringColumn("sender", DataTypes.TEXT)
					.withClusteringColumn("content", DataTypes.TEXT)
					.withColumn("created_at", DataTypes.TIMESTAMP)
					.withClusteringOrder("timestamp", ClusteringOrder.DESC)
					.build();
			session.execute(messageStatement);
		}
	}
	
	@PreDestroy
	public void close() {
		log.info("Shutting down local agent memory cassandra server ...");
	    EmbeddedCassandraServerHelper.cleanEmbeddedCassandra();
	}
}
