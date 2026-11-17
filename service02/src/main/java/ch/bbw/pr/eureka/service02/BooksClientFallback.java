package ch.bbw.pr.eureka.service02;

import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

// Circuit Breaker + Fallback: wird aufgerufen, wenn service-01 nicht antwortet (Fehler, Timeout oder Breaker offen)
@Component
public class BooksClientFallback implements BooksClient {
    private static final Logger log = LoggerFactory.getLogger(BooksClientFallback.class);

    @Override
    public List<Book> getBooks() {
        log.warn("Fallback: service-01 nicht erreichbar, liefere Ersatz-Antwort");
        return List.of(new Book(0, "Bücher momentan nicht verfügbar", "Fallback"));
    }

    @Override
    public Book addBook(Book book) {
        // Kein "so tun als ob": sonst glaubt der Benutzer, das Buch sei gespeichert
        log.warn("Fallback: Buch '{}' konnte nicht gespeichert werden", book.title());
        throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE, "service-01 nicht erreichbar");
    }
}
