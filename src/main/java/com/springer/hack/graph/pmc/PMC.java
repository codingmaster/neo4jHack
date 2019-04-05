package com.springer.hack.graph.pmc;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.neo4j.ogm.annotation.GeneratedValue;
import org.neo4j.ogm.annotation.Id;
import org.neo4j.ogm.annotation.NodeEntity;

@Data
@NoArgsConstructor
@RequiredArgsConstructor
@NodeEntity
public class PMC {
    @Id
    @GeneratedValue
    private Long neoId;
    @NonNull
    private String id;
    @NonNull
    private String value;
}
