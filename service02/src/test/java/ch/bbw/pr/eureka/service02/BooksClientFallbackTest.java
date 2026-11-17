package ch.bbw.pr.eureka.service02;

import org.junit.jupiter.api.Test;
import org.springframework.web.server.ResponseStatusException;

import static org.junit.jupiter.api.Assertions.*;

class BooksClientFallbackTest {
    private final BooksClientFallback fallback = new BooksClientFallback();

    @Test
    void getBooksReturnsReplacementInsteadOfError() {
        assertEquals("Fallback", fallback.getBooks().get(0).author());
    }

    @Test
    void addBookFailsLoudlySoNoBookGetsLost() {
        ResponseStatusException e = assertThrows(ResponseStatusException.class,
                () -> fallback.addBook(new Book(5, "Harry Potter", "J.K. Rowling")));
        assertEquals(503, e.getStatusCode().value());
    }
}
