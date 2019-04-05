package com.springer.hack.graph.rendition;

import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.repository.query.Param;

public interface RenditionRepo extends Neo4jRepository<Rendition, Long> {

    Rendition findByIsbn(@Param("isbn") String isbn);

}
