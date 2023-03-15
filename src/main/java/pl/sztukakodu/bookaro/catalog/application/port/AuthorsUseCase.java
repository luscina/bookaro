package pl.sztukakodu.bookaro.catalog.application.port;

import pl.sztukakodu.bookaro.catalog.domain.Author;
import pl.sztukakodu.bookaro.catalog.domain.Book;

import java.util.List;
import java.util.Set;

public interface AuthorsUseCase {
    public List<Author> findAll();
    public Author findById(Long id);
    public Set<Author> findByBook(Book book);
}
