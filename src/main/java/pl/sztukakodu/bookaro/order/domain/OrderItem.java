package pl.sztukakodu.bookaro.order.domain;

import jakarta.persistence.*;
import lombok.*;
import pl.sztukakodu.bookaro.catalog.domain.Book;
import pl.sztukakodu.bookaro.jpa.BaseEntity;


@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class OrderItem extends BaseEntity {
    @ManyToOne
    @JoinColumn(name = "book_id")
    private Book book;
    private int quantity;

}
