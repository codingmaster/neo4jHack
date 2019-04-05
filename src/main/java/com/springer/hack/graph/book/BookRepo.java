package com.springer.hack.graph.book;

import org.springframework.data.neo4j.annotation.Query;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.repository.query.Param;

import java.util.Collection;

public interface BookRepo extends Neo4jRepository<Book, Long> {
    Book findByTitle(@Param("title") String title);

    Iterable<Book> findByTitleLike(@Param("title") String title);

    @Query("MATCH (b:Book)-[r]-(n) WHERE b.id = {book} RETURN b,r,n LIMIT {limit}")
    Collection<Book> graph(@Param("book") String book, @Param("limit") int limit);
}
