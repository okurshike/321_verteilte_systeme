package ch.bbw.pr.eureka.service02;

import java.time.LocalDateTime;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

// Kein @CrossOrigin mehr: React kommt über das Gateway, und das Gateway setzt die CORS-Header
@RestController
public class MainController {
    private static final Logger log = LoggerFactory.getLogger(MainController.class);
    private final RestTemplate restTemplate;
    private final BooksClient booksClient;

    public MainController(RestTemplate restTemplate, BooksClient booksClient) {
        this.restTemplate = restTemplate;
        this.booksClient = booksClient;
    }

    @GetMapping("/call")
    public MyResponse callService01() {
        ResponseEntity<MyData> response = restTemplate.getForEntity("http://service-01/api", MyData.class);
        return new MyResponse(response.getBody(), "Antwort von service-01 erfolgreich verarbeitet", LocalDateTime.now().toString());
    }

    // Ohne "/api"-Präfix, weil das Gateway "/api" schon wegschneidet (siehe 13.2)
    @GetMapping("/books")
    public List<String> getBooks() {
        log.info("GET /books -> rufe service-01 über Feign auf");
        List<String> books = booksClient.getBooks().stream()
                .map(book -> book.title() + " (" + book.author() + ")")
                .toList();
        log.debug("service-01 lieferte {} Bücher", books.size());
        return books;
    }

    @PostMapping("/books")
    public Book addBook(@RequestBody Book book) {
        log.info("POST /books -> speichere '{}' bei service-01", book.title());
        return booksClient.addBook(book);
    }
}
