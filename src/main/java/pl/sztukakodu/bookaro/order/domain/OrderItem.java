package pl.sztukakodu.bookaro.order.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.*;
import pl.sztukakodu.bookaro.jpa.BaseEntity;


@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class OrderItem extends BaseEntity {
    private Long bookId;
    private int quantity;

}
