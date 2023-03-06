package pl.sztukakodu.bookaro.catalog.web;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.sztukakodu.bookaro.catalog.application.port.CatalogUseCase;
import pl.sztukakodu.bookaro.catalog.domain.Book;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/catalog")

@AllArgsConstructor
public class CatalogController {
    private final CatalogUseCase catalog;
//    @GetMapping
//    @ResponseStatus(HttpStatus.OK)
//    public List<Book> getAll(
//            @RequestParam Optional<String> title,
//            @RequestParam Optional<String> author
//    ){
//        return catalog.findAll();
//    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<Book> getAllFiltrated(
            @RequestParam Optional<String> title,
            @RequestParam Optional<String> author
            ){
        if(author.isPresent() && title.isPresent()){
            return catalog.findByTitleAndAuthor(title.get(), author.get());
        }else if(title.isPresent()){
            return catalog.findByTitle(title.get());
        }else if(author.isPresent()){
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
}
