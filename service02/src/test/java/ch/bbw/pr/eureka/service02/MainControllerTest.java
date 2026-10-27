package ch.bbw.pr.eureka.service02;

import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class MainControllerTest {
    @Test
    void callUsesEurekaServiceNameAndEnrichesResponse() {
        RestTemplate restTemplate = mock(RestTemplate.class);
        when(restTemplate.getForEntity("http://service-01/api", MyData.class))
                .thenReturn(ResponseEntity.ok(new MyData("Hello World from Service 01")));

        MyResponse response = new MainController(restTemplate, mock(BooksClient.class)).callService01();

        verify(restTemplate).getForEntity("http://service-01/api", MyData.class);
        assertEquals("Hello World from Service 01", response.originalData().name());
        assertEquals("Antwort von service-01 erfolgreich verarbeitet", response.message());
        assertNotNull(response.timestamp());
    }

    @Test
    void booksAreFormattedAsTitleAndAuthor() {
        BooksClient booksClient = mock(BooksClient.class);
        when(booksClient.getBooks()).thenReturn(List.of(new Book(3, "1984", "George Orwell")));

        List<String> books = new MainController(mock(RestTemplate.class), booksClient).getBooks();

        assertEquals(List.of("1984 (George Orwell)"), books);
    }
}
