package com.springer.hack.graph.book;

import com.springer.hack.graph.keyword.Keyword;
import com.springer.hack.graph.originator.Originator;
import com.springer.hack.graph.pmc.PMC;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.neo4j.ogm.annotation.GeneratedValue;
import org.neo4j.ogm.annotation.Id;
import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Relationship;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@RequiredArgsConstructor
@NodeEntity
public class Book {
    @Id
    @GeneratedValue
    private Long neoId;
    @NonNull
    private String id;
    @NonNull
    private String title, subTitle;

    @Relationship(type = "HAS_ORIGINATOR")
    private List<Originator> originators = new ArrayList<>();
    @Relationship(type = "HAS_PMC")
    private List<PMC> pmcs = new ArrayList<>();
    @Relationship(type = "HAS_KEYWORD")
    private List<Keyword> keywords = new ArrayList<>();

    public List<Originator> getOriginators() {
        return originators;
    }

    public List<PMC> getPmcs() {
        return pmcs;
    }

    public List<Keyword> getKeywords() {
        return keywords;
    }
}
