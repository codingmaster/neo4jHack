package com.spring.hack.marvel.comicissue;

import org.springframework.data.neo4j.annotation.Query;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.repository.query.Param;

import java.util.Collection;

public interface ComicIssueRepo extends Neo4jRepository<ComicIssue, Long> {
    ComicIssue findByName(@Param("name") String name);

    Iterable<ComicIssue> findByNameLike(@Param("name") String name);

    @Query("MATCH (i:ComicIssue)-[r]-(n) WHERE i.id = {issue} RETURN i,r,n LIMIT {limit}")
    Collection<ComicIssue> graph(@Param("issue") Long issue, @Param("limit") int limit);
}
