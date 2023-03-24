package pl.sztukakodu.bookaro.order.application.price;

import pl.sztukakodu.bookaro.order.domain.Order;

import java.math.BigDecimal;

public class DeliveryDiscountStrategy implements DiscountStrategy {
    private final BigDecimal THRESHOLD = new BigDecimal("100.00");
    @Override
    public BigDecimal calculate(Order order) {

        if(order.totalPrice().compareTo(THRESHOLD) >= 0) {
            return order.getDeliveryPrice();
        }
        return BigDecimal.ZERO;
    }
}
