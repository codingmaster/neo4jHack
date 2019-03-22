package com.spring.hack.marvel.comicissue;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

@AllArgsConstructor
@Controller
@RequestMapping("/comicissues")
public class ComicIssueController {

    private final ComicIssueService issueService;

    @GetMapping("/findbyname")
    @ResponseBody
    public ComicIssue findByName(@RequestParam String name) {
        return issueService.findByName(name);
    }

    @GetMapping("/findbynamelike")
    @ResponseBody
    public Iterable<ComicIssue> findByNameLike(@RequestParam String name) {
        return issueService.findByNameLike(name);
    }

    @GetMapping("/buildgraph")
    @ResponseBody
    public Map<String, Object> buildgraph(@RequestParam(required = false) String issue, @RequestParam(required = false) Integer limit) {
        issue = issue.replace("%23", "#");
        Iterator<ComicIssue> issuesIterator = findByNameLike("*" + issue + "*").iterator();
        if (issuesIterator.hasNext()) {
            ComicIssue comicIssue = issuesIterator.next();
            return issueService.graph(comicIssue, limit == null ? 100 : limit);
        } else {
            return new HashMap<>();
        }
    }

    @GetMapping("/graph")
    public String graph(@RequestParam(required = false) String issue, @RequestParam(required = false) Integer limit, Model model) {
        model.addAttribute(buildgraph(issue, limit));
        return "issuesgraph";
    }
}
