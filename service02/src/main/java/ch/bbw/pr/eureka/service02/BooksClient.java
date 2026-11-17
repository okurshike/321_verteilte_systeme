package ch.bbw.pr.eureka.service02;

import java.util.List;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

// "service-01" ist der Name in Eureka, Feign baut daraus die HTTP-Aufrufe selbst
@FeignClient(name = "service-01", fallback = BooksClientFallback.class)
public interface BooksClient {
    @GetMapping("/books")
    List<Book> getBooks();

    @PostMapping("/books")
    Book addBook(@RequestBody Book book);
}
