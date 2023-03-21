package pl.sztukakodu.bookaro.catalog.web;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import pl.sztukakodu.bookaro.catalog.application.port.AuthorsUseCase;
import pl.sztukakodu.bookaro.catalog.application.port.CatalogUseCase;
import pl.sztukakodu.bookaro.catalog.db.AuthorJpaRepository;
import pl.sztukakodu.bookaro.catalog.domain.Author;
import pl.sztukakodu.bookaro.catalog.domain.Book;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static pl.sztukakodu.bookaro.catalog.application.port.CatalogUseCase.*;

@SpringBootTest
class CatalogControllerIT {
    @Autowired
    CatalogController catalogController;
    @Autowired
    CatalogUseCase catalogUseCase;
    @Autowired
    AuthorJpaRepository authorJpaRepository;
    @Test
    public void getAllBooks(){
        List<Book> all = catalogController.getAll(Optional.empty(), Optional.empty());
        Assertions.assertEquals(30, all.size());
    }
}