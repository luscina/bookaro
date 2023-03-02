package pl.sztukakodu.bookaro.catalog.domain;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import pl.sztukakodu.bookaro.catalog.domain.Book;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Service

public class CatalogService{

    private CatalogRepository repository;

    public CatalogService(@Qualifier("schoolCatalogRepository") CatalogRepository repository){
          this.repository = repository;
    }

    public List<Book> findByTitle(String title){
        return repository.listAll()
                .stream()
                .filter(book -> book.title.startsWith(title))
                .collect(Collectors.toList());
    }
}
