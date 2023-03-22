package pl.sztukakodu.bookaro.catalog.web;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import pl.sztukakodu.bookaro.catalog.application.port.CatalogUseCase;
import pl.sztukakodu.bookaro.catalog.domain.Book;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@WebMvcTest({CatalogController.class})
class CatalogControllerWebTest {

    @MockBean
    CatalogUseCase catalogUseCase;

    @Autowired
    MockMvc mockMvc;

    @Test
    public void shouldGiveAllBooks() throws Exception {
        Book concurrency = new Book("Java Concurrency", 2006, new BigDecimal("29.99"), 50L);
        Book effective = new Book("Effective Java", 2005, new BigDecimal("19.99"), 50L);
        Mockito.when(catalogUseCase.findAll()).thenReturn(List.of(concurrency, effective));
        mockMvc.perform(MockMvcRequestBuilders.get("/catalog"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)));
    }
}