package pl.sztukakodu.bookaro.order.application;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.sztukakodu.bookaro.catalog.db.BookJpaRepository;
import pl.sztukakodu.bookaro.order.application.port.QueryOrderUseCase;
import pl.sztukakodu.bookaro.order.application.price.OrderPrice;
import pl.sztukakodu.bookaro.order.application.price.PriceService;
import pl.sztukakodu.bookaro.order.db.OrderJpaRepository;
import pl.sztukakodu.bookaro.order.domain.Order;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class QueryOrderService implements QueryOrderUseCase {
    private final OrderJpaRepository repository;
    private final PriceService priceService;
    @Override
    @Transactional
    public List<RichOrder> findAll() {
        return repository.findAll()
                .stream()
                .map(this::toRichOrder)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public Optional<RichOrder> findById(Long id) {
        return repository.findById(id).map(this::toRichOrder);
    }

    private RichOrder toRichOrder(Order order) {
        OrderPrice orderPrice = priceService.calculatePrice(order);
        return new RichOrder(
                order.getId(),
                order.getStatus(),
                order.getItems(),
                order.getRecipient(),
                order.getCreatedAt(),
                orderPrice,
                orderPrice.getFinalPrice()
        );
    }

    @Override
    public void deleteById(Long id) {
        repository.deleteById(id);
    }
}
