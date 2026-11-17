package ch.bbw.pr.eureka.service01;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import org.springframework.beans.factory.annotation.Value;
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

    // Nur zum Testen des Timeout-Patterns: mit --book.delay-ms=3000 starten, dann ist service-01 "langsam"
    @Value("${book.delay-ms:0}")
    private long delayMs;

    @GetMapping("/books")
    public List<Book> getBooks() throws InterruptedException {
        Thread.sleep(delayMs);
        return books;
    }

    @PostMapping("/books")
    public Book addBook(@RequestBody Book book) {
        books.add(book);
        return book;
    }
}
