package com.spring.hack.marvel.character;

import org.springframework.data.neo4j.repository.Neo4jRepository;

public interface CharacterRepo
        extends Neo4jRepository<Character, Long> {
}