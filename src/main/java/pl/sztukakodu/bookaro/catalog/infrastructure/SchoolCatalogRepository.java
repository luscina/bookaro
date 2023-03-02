package pl.sztukakodu.bookaro.catalog.infrastructure;

import org.springframework.stereotype.Repository;
import pl.sztukakodu.bookaro.catalog.domain.Book;
import pl.sztukakodu.bookaro.catalog.domain.CatalogRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Repository
class SchoolCatalogRepository implements CatalogRepository {
    private final Map<Long, Book> storage = new ConcurrentHashMap<>();

    public SchoolCatalogRepository() {
       storage.put(1L, new Book(1L, "Pan Tadeusz", "Adam Mickiewicz", 1884));
       storage.put(2L, new Book(2L, "Ogniem i Mieczem", "Henryk Sienkiewicz", 1884));
       storage.put(3L, new Book(3L, "Chłopi", "Adam Mickiewicz", 1904));
       storage.put(4L, new Book(4L, "Pan Wołodyjowski", "Henryk Sienkiewicz", 1904));
    }

    @Override
    public List<Book> listAll() {
        return new ArrayList<>(storage.values());
    }
}
