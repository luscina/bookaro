package pl.sztukakodu.bookaro.order.application.port;

import lombok.AllArgsConstructor;
import lombok.Value;
import pl.sztukakodu.bookaro.catalog.domain.Book;
import pl.sztukakodu.bookaro.order.domain.Order;
import pl.sztukakodu.bookaro.order.domain.OrderStatus;
import pl.sztukakodu.bookaro.order.domain.Recipient;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface QueryOrderUseCase {
    List<RichOrder> findAll();

    Optional<RichOrder> findById(Long id);

    void deleteById(Long id);

    @Value
    class  RichOrder {
        Long id;
        OrderStatus status;
        List<RichOrderItem> items;
        Recipient recipient;
        LocalDateTime createdAt;

        public BigDecimal totalPrice(){
            return items.stream()
                    .map(item -> item.book.getPrice().multiply(new BigDecimal(item.quantity)))
                    .reduce(BigDecimal.ZERO, BigDecimal::add);
        }


    }
    @Value
    class RichOrderItem{
        Book book;
        int quantity;
    }
}
