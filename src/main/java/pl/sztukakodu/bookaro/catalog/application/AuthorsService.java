package pl.sztukakodu.bookaro.catalog.application;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import pl.sztukakodu.bookaro.catalog.application.port.AuthorsUseCase;
import pl.sztukakodu.bookaro.catalog.db.AuthorJpaRepository;
import pl.sztukakodu.bookaro.catalog.domain.Author;
import pl.sztukakodu.bookaro.catalog.domain.Book;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
@AllArgsConstructor
public class AuthorsService implements AuthorsUseCase {
    private final AuthorJpaRepository authorJpaRepository;
    @Override
    public List<Author> findAll() {
        return authorJpaRepository.findAll();
    }

    @Override
    public Author findById(Long id) {
        return authorJpaRepository.findById(id).get();
    }

    public Set<Author> findByBook(Book book){
        return book.getAuthors();
    }
}
