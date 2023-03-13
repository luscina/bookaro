package pl.sztukakodu.bookaro.order.domain;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import pl.sztukakodu.bookaro.catalog.domain.CatalogRepository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
public class Order {
    private final CatalogRepository repository;
    private Long id;
    private List<OrderItem> items;
    private Recipient recipient;
    @Builder.Default
    private OrderStatus status = OrderStatus.NEW;
    private LocalDateTime createdAt;

}
