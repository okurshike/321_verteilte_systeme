package ch.bbw.pr.eureka.service01;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RESTBookController {
    // Keine Datenbank: die Liste lebt nur im Speicher, nach einem Neustart sind hinzugefügte Bücher weg
    private final List<Book> books = new CopyOnWriteArrayList<>(List.of(
            new Book(1, "Die Verwandlung", "Franz Kafka"),
            new Book(2, "Der Process", "Franz Kafka"),
            new Book(3, "1984", "George Orwell"),
            new Book(4, "Animal Farm", "George Orwell")));

    @GetMapping("/books")
    public List<Book> getBooks() {
        return books;
    }

    @PostMapping("/books")
    public Book addBook(@RequestBody Book book) {
        books.add(book);
        return book;
    }
}
