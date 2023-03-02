package pl.sztukakodu.bookaro.catalog;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import pl.sztukakodu.bookaro.catalog.application.CatalogController;
import pl.sztukakodu.bookaro.catalog.domain.Book;

import java.util.List;
@Component
public class ApplicationStartup implements CommandLineRunner {
    private final CatalogController controller;

    public ApplicationStartup(CatalogController controller) {
        this.controller = controller;
    }

    @Override
    public void run(String... args) throws Exception {
        List<Book> panTadeusz = controller.findByTitle("Pan Tadeusz");
        panTadeusz.forEach(System.out::println);
    }
}
