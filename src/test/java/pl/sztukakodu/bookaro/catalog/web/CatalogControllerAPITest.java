package pl.sztukakodu.bookaro.catalog.web;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import pl.sztukakodu.bookaro.catalog.application.port.CatalogUseCase;
import pl.sztukakodu.bookaro.catalog.domain.Book;

import java.math.BigDecimal;
import java.util.List;
import java.net.URI;

import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class CatalogControllerAPITest {

    @LocalServerPort
    private int port;

    @MockBean
    CatalogUseCase catalogUseCase;
    @Autowired
    TestRestTemplate restTemplate;

    @Test
    public void shouldGiveAllBooks() throws Exception {
        Book concurrency = new Book("Java Concurrency", 2006, new BigDecimal("29.99"), 50L);
        Book effective = new Book("Effective Java", 2005, new BigDecimal("19.99"), 50L);
        Mockito.when(catalogUseCase.findAll()).thenReturn(List.of(effective, concurrency));
        ParameterizedTypeReference<List<Book>> type = new ParameterizedTypeReference<>() {
        };

        String uri = "http://localhost:" + port + "/catalog";
        RequestEntity<Void> request = RequestEntity.get(URI.create("http://localhost:" + port + "/catalog")).build();
        ResponseEntity<List<Book>> response = restTemplate.exchange(request, type);

        assertEquals(2, response.getBody().size());

    }
}