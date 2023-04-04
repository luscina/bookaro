package pl.sztukakodu.bookaro.catalog.web;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import pl.sztukakodu.bookaro.catalog.application.port.CatalogUseCase;
import pl.sztukakodu.bookaro.catalog.domain.Book;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {CatalogController.class})
class CatalogControllerTest {
    @MockBean
    CatalogUseCase catalogUseCase;

    @Autowired
    CatalogController catalogController;

    @Test
    public void shouldGetAllBooks(){
        Book effective = new Book("Effective Java", 2005, new BigDecimal("99.00"), 50L);
        Book concurrency = new Book("Java Concurency", 2001, new BigDecimal("19.99"), 50L);
        Mockito.when(catalogUseCase.findAll()).thenReturn(List.of(effective, concurrency));
        List<Book> all = catalogController.getAll(Optional.empty(), Optional.empty());
        assertEquals(2, all.size());
    }

}