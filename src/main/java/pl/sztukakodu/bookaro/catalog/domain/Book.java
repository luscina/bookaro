package pl.sztukakodu.bookaro.catalog.domain;

import lombok.*;

import java.math.BigDecimal;
import java.util.StringJoiner;

@ToString
@RequiredArgsConstructor
@Getter
@Setter
public class Book {
    private Long id;
    private String title;
    private String author;
    private Integer year;
    private BigDecimal price;
    private String coverId;


    public Book(String title, String author, Integer year, BigDecimal price) {
        this.title = title;
        this.author = author;
        this.year = year;
        this.price = price;
    }
}
