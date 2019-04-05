package com.springer.hack.graph.pmc;

import org.springframework.data.neo4j.repository.Neo4jRepository;

public interface PMCRepo extends Neo4jRepository<PMC, Long> {
}
