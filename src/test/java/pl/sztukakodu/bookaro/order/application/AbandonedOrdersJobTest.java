package pl.sztukakodu.bookaro.order.application;

import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import pl.sztukakodu.bookaro.Clock.Clock;
import pl.sztukakodu.bookaro.catalog.db.BookJpaRepository;
import pl.sztukakodu.bookaro.catalog.domain.Book;
import pl.sztukakodu.bookaro.order.application.port.AbandonedOrdersJob;
import pl.sztukakodu.bookaro.order.application.port.ManipulateOrderUseCase;
import pl.sztukakodu.bookaro.order.application.port.ManipulateOrderUseCase.PlaceOrderCommand;
import pl.sztukakodu.bookaro.order.application.port.QueryOrderUseCase;
import pl.sztukakodu.bookaro.order.db.OrderJpaRepository;
import pl.sztukakodu.bookaro.order.domain.OrderStatus;
import pl.sztukakodu.bookaro.order.domain.Recipient;

import java.time.Duration;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(
        "app.orders.payment-period = 1H"
)
class AbandonedOrdersJobTest {

    @TestConfiguration
    static class TestConfig {
        @Bean
        public Clock.Fake clock() {
            return new Clock.Fake();
        }
    }
    @Autowired
    AbandonedOrdersJob abandonedOrdersJob;
    @Autowired
    ManipulateOrderService orderService;
    @Autowired
    BookJpaRepository bookJpaRepository;
    @Autowired
    QueryOrderUseCase queryOrderService;
    @Autowired
    OrderJpaRepository orderJpaRepository;
    @Autowired
    Clock.Fake clock;

    @Test
    @Transactional
    public void shouldMarkOrderAsAbandoned(){
        Book book = bookJpaRepository.getById(3L);
        book.setAvailable(50L);
        bookJpaRepository.save(book);
        Long orderId = placeOrder(book.getId(), 15);

        clock.tick(Duration.ofHours(2));
        abandonedOrdersJob.run();
        assertEquals(OrderStatus.ABANDONED, orderJpaRepository.findById(orderId).get().getStatus());
        assertEquals(35L, book.getAvailable());

        book.setAvailable(50L);
        bookJpaRepository.save(book);
    }

    private Long placeOrder(Long bookId, int copies) {
        PlaceOrderCommand command = PlaceOrderCommand
                .builder()
                .recipient(recipient())
                .item(new ManipulateOrderUseCase.OrderItemCommand(bookId, copies))
                .build();
        return orderService.placeOrder(command).getRight();
    }

    private Recipient recipient(){
        return Recipient.builder().email("slowik@onet.pl").build();
    }
    private Recipient recipient(String email){
        return Recipient.builder().email(email).build();
    }
}