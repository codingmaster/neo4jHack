package com.springer.hack.graph.originator;

import org.springframework.data.neo4j.repository.Neo4jRepository;

public interface OriginatorRepo extends Neo4jRepository<Originator, Long> {
}
