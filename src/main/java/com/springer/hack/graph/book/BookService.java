package com.springer.hack.graph.book;

import com.springer.hack.graph.keyword.Keyword;
import com.springer.hack.graph.originator.Originator;
import com.springer.hack.graph.pmc.PMC;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;


@AllArgsConstructor
@Service
public class BookService {

    private final BookRepo bookRepo;

    @Transactional(readOnly = true)
    public Iterable<Book> findAllBooks(){
        return bookRepo.findAll();
    }

    @Transactional(readOnly = true)
    public Book findByTitle(String title){
        return bookRepo.findByTitle(title);
    }

    @Transactional(readOnly = true)
    public Iterable<Book> findByTitleLike(String title) {
        return bookRepo.findByTitleLike(title);
    }

    public Map<String, Object> graph(Book book, Integer limit) {
        Collection<Book> graph = bookRepo.graph(book.getId(), limit);
        return toD3Format(graph);
    }

    private Map<String, Object> toD3Format(Iterable<Book> books){
        List<Map<String, Object>> nodes = new ArrayList<>();
        List<Map<String, Object>> rels = new ArrayList<>();
        int i = 0;
        Iterator<Book> result = books.iterator();

        while (result.hasNext()) {
            Book book = result.next();
            Map<String, Object> BookMap = map("name", book.getTitle(), "label", "book");
            nodes.add(BookMap);
            int target = i;
            i++;
            for (Originator originator : book.getOriginators()) {
                String label = "originator";
                Map<String, Object> bookOriginator = map("name", originator.getName(), "label", label);
                int source = nodes.indexOf(bookOriginator);
                if (source == -1) {
                    nodes.add(bookOriginator);
                    source = i++;
                }
                rels.add(map("source", source, "target", target));
            }
            for (Keyword keyword : book.getKeywords()) {
                String label = "keyword";
                Map<String, Object> bookKeywords = map("name", keyword.getValue(), "label", label);
                int source = nodes.indexOf(bookKeywords);
                if (source == -1) {
                    nodes.add(bookKeywords);
                    source = i++;
                }
                rels.add(map("source", source, "target", target));
            }
            for (PMC pmc : book.getPmcs()) {
                String label = "pmc";
                Map<String, Object> comicSeries = map("name", pmc.getValue(), "label", label);
                int source = nodes.indexOf(comicSeries);
                if (source == -1) {
                    nodes.add(comicSeries);
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
