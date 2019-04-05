package com.springer.hack.graph.pmc;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/pmcs")
public class PMCController {
    private final PMCRepo repo;

    public PMCController(PMCRepo repo) {
        this.repo = repo;
    }

    @GetMapping
    public Iterable<PMC> getAllPMCs() {
        return repo.findAll();
    }
}
