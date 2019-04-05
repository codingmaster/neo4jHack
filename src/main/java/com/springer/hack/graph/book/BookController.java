package com.springer.hack.graph.book;

import lombok.AllArgsConstructor;
import org.apache.logging.log4j.util.Strings;
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
@RequestMapping("/books")
public class BookController {

    private final BookService bookService;

    @GetMapping("/findbytitle")
    @ResponseBody
    public Book findByName(@RequestParam String title) {
        return bookService.findByTitle(title);
    }

    @GetMapping("/findbytitlelike")
    @ResponseBody
    public Iterable<Book> findByTitleLike(@RequestParam String title) {
        return bookService.findByTitleLike(title);
    }

    @GetMapping("/buildgraph")
    @ResponseBody
    public Map<String, Object> buildgraph(@RequestParam(required = false) String title, @RequestParam(required = false) Integer limit) {
        title = !Strings.isEmpty(title) ? title.replace("%23", "#") : "";
        Iterator<Book> bookIterator = findByTitleLike("*" + title + "*").iterator();
        if (bookIterator.hasNext()) {
            Book book = bookIterator.next();
            return bookService.graph(book, limit == null ? 100 : limit);
        } else {
            return new HashMap<>();
        }
    }

    @GetMapping("/graph")
    public String graph(@RequestParam(required = false) String title, @RequestParam(required = false) Integer limit, Model model) {
        model.addAttribute(buildgraph(title, limit));
        return "booksgraph";
    }

}
