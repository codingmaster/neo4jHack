package com.springer.hack.graph.keyword;

import org.springframework.data.neo4j.repository.Neo4jRepository;

public interface KeywordRepo extends Neo4jRepository<Keyword, Long> {
}
