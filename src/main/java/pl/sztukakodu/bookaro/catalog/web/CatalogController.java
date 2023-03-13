package pl.sztukakodu.bookaro.catalog.web;

import jakarta.validation.Valid;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import pl.sztukakodu.bookaro.catalog.application.port.CatalogUseCase;
import pl.sztukakodu.bookaro.catalog.application.port.CatalogUseCase.CreateBookCommand;
import pl.sztukakodu.bookaro.catalog.application.port.CatalogUseCase.UpdateBookCommand;
import pl.sztukakodu.bookaro.catalog.application.port.CatalogUseCase.UpdateBookCoverCommand;
import pl.sztukakodu.bookaro.catalog.domain.Book;
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
    public List<Book> getAllFiltrated(
            @RequestParam Optional<String> title,
            @RequestParam Optional<String> author
            ){
        if (author.isPresent() && title.isPresent()) {
            return catalog.findByTitleAndAuthor(title.get(), author.get());
        } else if(title.isPresent()) {
            return catalog.findByTitle(title.get());
        } else if(author.isPresent()) {
            return catalog.findByAuthor(author.get());
        }
        return catalog.findAll();
    }
    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable Long id){
        return catalog
            .findById(id)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Void> addBook(@Valid @RequestBody RestBookCommand command) {
        Book book = catalog.addBook(command.toCommand());
        return ResponseEntity.created(getUri(book)).build();
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public void updateBook(@PathVariable Long id, @Valid @RequestBody RestBookCommand command) {
        catalog.updateBook(command.toUpdateCommand(id));
    }



    private static URI getUri(Book book) {
        return ServletUriComponentsBuilder.fromCurrentRequestUri().path("/" + book.getId().toString()).build().toUri();
    }

    @PutMapping("/{id}/cover")
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

    @Data
    private static class RestBookCommand {
        @NotBlank
        private String title;

        @NotBlank
        private String author;

        @NotNull
        private Integer year;

        @NotNull
        @DecimalMin("0.00")
        private BigDecimal price;

        CreateBookCommand toCommand() {
            return new CreateBookCommand(title, author, year, price);
        }
        UpdateBookCommand toUpdateCommand(Long id) {
            return new UpdateBookCommand(id, title, author, year, price);
        }
    }
    @DeleteMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteById(Long id) {
        catalog.removeById(id);
    }
}
