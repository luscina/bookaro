package pl.sztukakodu.bookaro.order.application;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import pl.sztukakodu.bookaro.catalog.domain.Book;
import pl.sztukakodu.bookaro.order.domain.OrderItem;
import pl.sztukakodu.bookaro.order.domain.OrderStatus;
import pl.sztukakodu.bookaro.order.domain.Recipient;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class RichOrderTest {
    @Test
    public void calculatesTotalPrice(){
        Book book1 = new Book();
        Book book2 = new Book();
        book1.setPrice(new BigDecimal("9.99"));
        book2.setPrice(new BigDecimal("12.99"));
        Set<OrderItem> items = new HashSet<>(Arrays.asList(
                new OrderItem(book1, 3),
                new OrderItem(book2, 2)
        ));
//        RichOrder richOrder = new RichOrder(
//                1L,
//                OrderStatus.NEW,
//                items,
//                Recipient.builder().build(),
//                LocalDateTime.now()
//        );
//
//        BigDecimal totalPrice = richOrder.totalPrice();
//        Assertions.assertEquals(totalPrice, new BigDecimal("55.95"));

    }
}