package com.interviews.req10079266.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.data.cassandra.repository.config.EnableCassandraRepositories;

@Configuration
@Profile("local-cassandra")
@EnableCassandraRepositories(basePackages = {"com.interviews.req10079266.dao.cassandra"})
public class CassandraConfig {
}
