package com.springer.hack.graph.originator;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/originators")
public class OriginatorController {
    private final OriginatorRepo repo;

    public OriginatorController(OriginatorRepo repo) {
        this.repo = repo;
    }

    @GetMapping
    public Iterable<Originator> getAllOriginators() {
        return repo.findAll();
    }
}
