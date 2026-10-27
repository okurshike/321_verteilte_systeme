package ch.bbw.pr.eureka.service01;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(properties = {"eureka.client.enabled=false"})
@AutoConfigureMockMvc
class RESTBookControllerTest {
    @Autowired MockMvc mvc;

    @Test
    void booksCanBeListedAndAdded() throws Exception {
        mvc.perform(get("/books"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].title").value("Die Verwandlung"));

        mvc.perform(post("/books").contentType(MediaType.APPLICATION_JSON)
                        .content("{\"id\":5,\"title\":\"Harry Potter and the Philosopher's Stone\",\"author\":\"J.K. Rowling\"}"))
                .andExpect(status().isOk());

        mvc.perform(get("/books")).andExpect(jsonPath("$[4].author").value("J.K. Rowling"));
    }
}
