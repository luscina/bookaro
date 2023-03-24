package pl.sztukakodu.bookaro.order.application.price;

import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import pl.sztukakodu.bookaro.order.domain.Order;

import java.math.BigDecimal;
import java.util.List;

@Service
public class PriceService {

    private final List<DiscountStrategy> strategies = List.of(
            new DeliveryDiscountStrategy(),
            new TotalPriceDiscountStrategy()
    );

    @Transactional
    public OrderPrice calculatePrice(Order order){
        return new OrderPrice(
                order.totalPrice(),
                order.getDelivery().getPrice(),
                discounts(order)
        );
    }

    private BigDecimal discounts(Order order) {
        return strategies.stream()
                .map(stategy -> stategy.calculate(order))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

}
