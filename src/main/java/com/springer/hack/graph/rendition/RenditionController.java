package com.springer.hack.graph.rendition;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/renditions")
public class RenditionController {
    private final RenditionRepo repo;

    public RenditionController(RenditionRepo repo) {
        this.repo = repo;
    }

    @GetMapping
    public Iterable<Rendition> getAllRenditions() {
        return repo.findAll();
    }
}
