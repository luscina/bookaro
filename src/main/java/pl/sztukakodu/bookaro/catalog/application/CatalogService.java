package pl.sztukakodu.bookaro.catalog.application;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.sztukakodu.bookaro.catalog.application.port.AuthorsUseCase;
import pl.sztukakodu.bookaro.catalog.application.port.CatalogUseCase;
import pl.sztukakodu.bookaro.catalog.db.AuthorJpaRepository;
import pl.sztukakodu.bookaro.catalog.db.BookJpaRepository;
import pl.sztukakodu.bookaro.catalog.domain.Author;
import pl.sztukakodu.bookaro.catalog.domain.Book;
import pl.sztukakodu.bookaro.uploads.application.port.UploadUseCase;
import pl.sztukakodu.bookaro.uploads.domain.Upload;

import java.util.*;
import java.util.stream.Collectors;

import static pl.sztukakodu.bookaro.uploads.application.port.UploadUseCase.*;

@Service
@RequiredArgsConstructor
class CatalogService implements CatalogUseCase {
    private final AuthorJpaRepository authorJpaRepository;
    private final BookJpaRepository repository;
    private final UploadUseCase upload;
    private final AuthorsUseCase authorsUseCase;

    @Override
    public List<Book> findByTitle(String title) {
        return repository.findByTitleStartingWithIgnoreCase(title);
    }

    @Override
    public Optional<Book> findOneByTitle(String title) {
        return repository.findAll()
                .stream()
                .filter(book -> book.getTitle().contains(title))
                .findFirst();
    }

    @Override
    public List<Book> findByAuthor(String author) {
        return repository.findByAuthors_firstNameContainsIgnoreCaseOrAuthors_lastNameContainsIgnoreCase(author, author);
    }

    @Override
    public List<Book> findAll() {
        return repository.findAll();
    }

    @Override
    public Book addBook(CreateBookCommand command) {
        Book book = toBook(command);
        return repository.save(book);
    }

    private Book toBook(CreateBookCommand command) {
        Book book = new Book(command.getTitle(), command.getYear(), command.getPrice());
        Set<Author> authors = fetchAuthorsByIds(command.getAuthors());
        book.setAuthors(authors);
        return book;
    }

    private Set<Author> fetchAuthorsByIds(Set<Long> authors) {
        return authors
                .stream()
                .map(authorId -> authorJpaRepository
                        .findById(authorId)
                        .orElseThrow(() -> new IllegalArgumentException("Unable to find author by id "))
                )
                .collect(Collectors.toSet());
    }

    @Override
    public void removeById(Long id){
        repository.deleteById(id);
    }

    @Override
    public UpdateBookResponse updateBook(UpdateBookCommand command) {
        return repository
                .findById(command.getId())
                .map(book -> {
                    Book updatedBook = updateFields(command, book);
                    repository.save(updatedBook);
                    return UpdateBookResponse.SUCCESS;
                })
                .orElseGet(() -> new UpdateBookResponse(false, Arrays.asList("Book not found with id: " + command.getId())));
    }

    private Book updateFields(UpdateBookCommand command, Book book){
        if(command.getTitle() != null){
            book.setTitle(command.getTitle());
        }
        if(command.getAuthors() != null && command.getAuthors().size() > 0){
            book.setAuthors(fetchAuthorsByIds(command.getAuthors()));
        }
        if(command.getYear() != null){
            book.setYear(command.getYear());
        }
        if(command.getPrice() != null){
            book.setPrice(command.getPrice());
        }
        return book;
    }

    @Override
    public void updateBookCover(UpdateBookCoverCommand command) {
        System.out.println("Recived file name" + command.getFilename() + "bytes: " + command.getFile().length);
        repository.findById(command.getId())
                .ifPresent(book -> {
                    Upload savedUpload = upload.save(new SaveUploadCommand(command.getFilename(), command.getFile(), command.getContentType()));
                    book.setCoverId(savedUpload.getId());
                    repository.save(book);
                });

    }

    @Override
    public void removeBookCover(Long id) {
        repository.findById(id)
                .ifPresent(book -> {
                    if (book.getCoverId() != null) {
                        upload.removeById(book.getCoverId());
                        book.setCoverId(null);
                        repository.save(book);
                    }
                });
    }
    @Override
    public Optional<Book> findById(Long id) {
        return repository.findById(id);
    }
}
