package com.spring.hack.marvel.story;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/stories")
public class StoryController {
    private final StoryRepo repo;
    public StoryController(StoryRepo repo) { this.repo = repo; }

    @GetMapping
    public Iterable<Story> getAllStories() {
        return repo.findAll();
    }
}
