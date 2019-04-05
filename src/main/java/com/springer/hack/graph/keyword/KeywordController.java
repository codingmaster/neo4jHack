package com.springer.hack.graph.keyword;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/keywords")
public class KeywordController {
    private final KeywordRepo repo;

    public KeywordController(KeywordRepo repo) {
        this.repo = repo;
    }

    @GetMapping
    public Iterable<Keyword> getAllKeywords() {
        return repo.findAll();
    }
}
