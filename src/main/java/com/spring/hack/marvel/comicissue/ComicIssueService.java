package com.spring.hack.marvel.comicissue;

import com.spring.hack.marvel.creator.Creator;
import com.spring.hack.marvel.event.Event;
import com.spring.hack.marvel.series.Series;
import com.spring.hack.marvel.story.Story;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;


@AllArgsConstructor
@Service
public class ComicIssueService {

    private final ComicIssueRepo issueRepo;

    @Transactional(readOnly = true)
    public Iterable<ComicIssue> findAllComicIssues(){
        return issueRepo.findAll();
    }

    @Transactional(readOnly = true)
    public ComicIssue findByName(String name){
        return issueRepo.findByName(name);
    }

    @Transactional(readOnly = true)
    public Iterable<ComicIssue> findByNameLike(String name) {
        return issueRepo.findByNameLike(name);
    }
    @Transactional(readOnly = true)
    public Map<String, Object> graph(ComicIssue issue, Integer limit) {
        Collection<ComicIssue> graph = issueRepo.graph(issue.getId(), limit);
        return toD3Format(graph);
    }

    private Map<String, Object> toD3Format(Iterable<ComicIssue> issues){
        List<Map<String, Object>> nodes = new ArrayList<>();
        List<Map<String, Object>> rels = new ArrayList<>();
        int i = 0;
        Iterator<ComicIssue> result = issues.iterator();

        while (result.hasNext()) {
            ComicIssue issue = result.next();
            Map<String, Object> comicIssueMap = map("name", issue.getName(), "label", "issue");
            comicIssueMap.put("image", issue.getThumbnail());
            nodes.add(comicIssueMap);
            int target = i;
            i++;
            for (com.spring.hack.marvel.character.Character character : issue.getCharacters()) {
                String label = "character";
                Map<String, Object> comicChar = map("name", character.getName(), "label", label);
                comicChar.put("image", character.getThumbnail());
                int source = nodes.indexOf(comicChar);
                if (source == -1) {
                    nodes.add(comicChar);
                    source = i++;
                }
                rels.add(map("source", source, "target", target));
            }
            for (Creator creator : issue.getCreators()) {
                String label = "creator";
                Map<String, Object> comicCreators = map("name", creator.getName(), "label", label);
                int source = nodes.indexOf(comicCreators);
                if (source == -1) {
                    nodes.add(comicCreators);
                    source = i++;
                }
                rels.add(map("source", source, "target", target));
            }
            for (Series series : issue.getSeries()) {
                String label = "series";
                Map<String, Object> comicSeries = map("name", series.getName(), "label", label);
                int source = nodes.indexOf(comicSeries);
                if (source == -1) {
                    nodes.add(comicSeries);
                    source = i++;
                }
                rels.add(map("source", source, "target", target));
            }
            for (Story story : issue.getStories()) {
                String label = "stories";
                Map<String, Object> comicStories = map("name", story.getName(), "label", label);
                int source = nodes.indexOf(comicStories);
                if (source == -1) {
                    nodes.add(comicStories);
                    source = i++;
                }
                rels.add(map("source", source, "target", target));

            }
            for (Event event : issue.getEvents()) {
                String label = "event";
                Map<String, Object> comicEvents = map("name", event.getName(), "label", label);
                int source = nodes.indexOf(comicEvents);
                if (source == -1) {
                    nodes.add(comicEvents);
                    source = i++;
                }
                rels.add(map("source", source, "target", target));
            }
        }
        return map("nodes", nodes, "links", rels);
    }
    private Map<String, Object> map(String key1, Object value1, String key2, Object value2) {
        Map<String, Object> result = new HashMap<String, Object>(2);
        result.put(key1, value1);
        result.put(key2, value2);
        return result;
    }
}
