package pl.sztukakodu.bookaro.catalog.web;

import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;
import pl.sztukakodu.bookaro.catalog.application.port.AuthorsUseCase;
import pl.sztukakodu.bookaro.catalog.application.port.CatalogUseCase;
import pl.sztukakodu.bookaro.catalog.application.port.CatalogUseCase.CreateBookCommand;
import pl.sztukakodu.bookaro.catalog.application.port.CatalogUseCase.UpdateBookCommand;
import pl.sztukakodu.bookaro.catalog.application.port.CatalogUseCase.UpdateBookCoverCommand;
import pl.sztukakodu.bookaro.catalog.application.port.CatalogUseCase.UpdateBookResponse;
import pl.sztukakodu.bookaro.catalog.domain.Book;
import pl.sztukakodu.bookaro.web.CreatedURI;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.URI;
import java.util.*;


@RestController
@RequestMapping("/catalog")
@AllArgsConstructor
public class CatalogController {
    private final CatalogUseCase catalog;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<Book> getAll(
            @RequestParam Optional<String> title,
            @RequestParam Optional<String> author
            ){
        if (title.isPresent()) {
            return catalog.findByTitle(title.get());
        } else if(author.isPresent()) {
            return catalog.findByAuthor(author.get());
        }
        return catalog.findAll();
    }
    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable Long id) {
        return catalog
            .findById(id)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }
    @Secured({"ROLE_ADMIN"})
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Void> addBook(@Valid @RequestBody RestBookCommand command) {
        Book book = catalog.addBook(command.toCommand());
        return ResponseEntity.created(getUri(book)).build();
    }
    @Secured({"ROLE_ADMIN"})
    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public void updateBook(@PathVariable Long id, @RequestBody RestBookCommand command) {
        UpdateBookResponse response = catalog.updateBook(command.toUpdateCommand(id));
        if(!response.isSuccess()){
            String message = String.join(", ", response.getError());
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, message);
        }
    }

    private static URI getUri(Book book) {
        return new CreatedURI("/" + book.getId().toString()).uri();
    }
    @Secured({"ROLE_ADMIN"})
    @PutMapping(value = "/{id}/cover", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    @ResponseStatus(HttpStatus.ACCEPTED)
    public void addBookCover(@PathVariable Long id, @RequestParam("file")MultipartFile file) throws IOException {
        System.out.println("Got file name: " + file.getOriginalFilename());
        catalog.updateBookCover(new UpdateBookCoverCommand(
                id,
                file.getBytes(),
                file.getContentType(),
                file.getOriginalFilename()
        ));
    }
    @Secured({"ROLE_ADMIN"})
    @DeleteMapping("/{id}/cover")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void removeBookCover(@PathVariable Long id){
        catalog.removeBookCover(id);
    }

    @Data
    private static class RestBookCommand {
        @NotBlank
        private String title;

        private Set<Long> authors;

        @NotNull
        private Integer year;
        @NotNull
        @PositiveOrZero
        private Long available;

        @NotNull
        @DecimalMin("0.00")
        private BigDecimal price;

        CreateBookCommand toCommand() {
            return new CreateBookCommand(title, authors, year, price, available);
        }
        UpdateBookCommand toUpdateCommand(Long id) {
            return new UpdateBookCommand(id, title, authors, year, price);
        }
    }
    @Secured({"ROLE_ADMIN"})
    @DeleteMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteById(Long id) {
        catalog.removeById(id);
    }
}
