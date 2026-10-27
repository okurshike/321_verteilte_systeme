package ch.bbw.pr.eureka.service02;

import java.time.LocalDateTime;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

// Kein @CrossOrigin mehr: React kommt über das Gateway, und das Gateway setzt die CORS-Header
@RestController
public class MainController {
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
        return booksClient.getBooks().stream()
                .map(book -> book.title() + " (" + book.author() + ")")
                .toList();
    }

    @PostMapping("/books")
    public Book addBook(@RequestBody Book book) {
        return booksClient.addBook(book);
    }
}
