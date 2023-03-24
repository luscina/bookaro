package pl.sztukakodu.bookaro.order.application.price;

import lombok.Value;

import java.math.BigDecimal;

@Value
public class OrderPrice {
    BigDecimal itemPrice;
    BigDecimal deliveryPrice;
    BigDecimal discounts;

    public BigDecimal getFinalPrice() {
        return itemPrice.add(deliveryPrice).subtract(discounts);
    }
}
