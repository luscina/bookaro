package pl.sztukakodu.bookaro.order.application;

import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import pl.sztukakodu.bookaro.catalog.application.port.CatalogUseCase;
import pl.sztukakodu.bookaro.catalog.db.BookJpaRepository;
import pl.sztukakodu.bookaro.catalog.domain.Book;
import pl.sztukakodu.bookaro.order.application.port.ManipulateOrderUseCase.OrderItemCommand;
import pl.sztukakodu.bookaro.order.application.port.ManipulateOrderUseCase.PlaceOrderCommand;
import pl.sztukakodu.bookaro.order.application.port.ManipulateOrderUseCase.PlaceOrderResponse;
import pl.sztukakodu.bookaro.order.application.port.ManipulateOrderUseCase.UpdateStatusCommand;
import pl.sztukakodu.bookaro.order.domain.OrderStatus;
import pl.sztukakodu.bookaro.order.domain.Recipient;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class OrderServiceTest {
    @Autowired
    BookJpaRepository bookRepository;
    @Autowired
    ManipulateOrderService service;
    @Autowired
    CatalogUseCase catalogUseCase;
    @Test
    @Transactional
    public void userCanPlaceOrder(){
        Book effective = bookRepository.findById(1L).get();
        Book jcip = bookRepository.findById(2L).get();
        effective.setAvailable(50L);
        bookRepository.save(effective);
        jcip.setAvailable(50L);
        bookRepository.save(jcip);
        PlaceOrderCommand command = PlaceOrderCommand
                .builder()
                .recipient(recipient())
                .item(new OrderItemCommand(1L, 15))
                .item(new OrderItemCommand(2L, 10))
                .build();
        PlaceOrderResponse placeOrder = service.placeOrder(command);
        assertTrue(placeOrder.isSuccess());
        assertEquals(35L, getAvailable(effective));
        assertEquals(40L, getAvailable(jcip));
        effective.setAvailable(50L);
        bookRepository.save(effective);
        jcip.setAvailable(50L);
        bookRepository.save(jcip);
    }

    private Long getAvailable(Book effective) {
        return catalogUseCase.findById(effective.getId()).get().getAvailable();
    }

    @Test
    @Transactional
    public void userCanRevokeOrder(){
        Book effective = bookRepository.findById(1L).get();
        effective.setAvailable(50L);
        bookRepository.save(effective);
        Long orderId = placeOrder(effective.getId(), 15);
        UpdateStatusCommand command = new UpdateStatusCommand(orderId, OrderStatus.CANCELLED, null);
        service.updateOrderStatus(command);

        assertEquals(50L, getAvailable(effective));

        effective.setAvailable(50L);
        bookRepository.save(effective);
    }



    @Test
    @Transactional
    public void userCannotRevokeOtherUserOrder(){
        Book effective = bookRepository.findById(1L).get();
        effective.setAvailable(50L);
        bookRepository.save(effective);
        String recipient = "adam@wp.pl";
        Long orderId = placeOrder(effective.getId(), 15, recipient);
        UpdateStatusCommand command = new UpdateStatusCommand(orderId, OrderStatus.CANCELLED, user(recipient));
        service.updateOrderStatus(command);

        assertEquals(35L, getAvailable(effective));

        effective.setAvailable(50L);
        bookRepository.save(effective);
    }

    @Test
    @Transactional
    public void adminCanRevokeOtherUserOrder(){
        Book effective = bookRepository.findById(1L).get();
        effective.setAvailable(50L);
        bookRepository.save(effective);
        String recipient = "marek@wp.pl";
        Long orderId = placeOrder(effective.getId(), 15, recipient);
        UpdateStatusCommand command = new UpdateStatusCommand(orderId, OrderStatus.CANCELLED, adminUser());
        service.updateOrderStatus(command);

        assertEquals(50L, getAvailable(effective));

        effective.setAvailable(50L);
        bookRepository.save(effective);
    }

    private Book givenEffectiveJava(long available) {
        return new Book("Effective Java", 2005, new BigDecimal("99.00"), available);
    }
    private Book givenJavaConcurrency(long available) {
        return new Book("Java Concurency", 2001, new BigDecimal("19.99"), available);
    }

    private User user(String email){
        return new User(email, "", List.of(new SimpleGrantedAuthority("ROLE_ADMIN")));
    }

    private User adminUser(){
        return new User("systemUser", "", List.of(new SimpleGrantedAuthority("ROLE_ADMIN")));
    }
    private Recipient recipient(){
        return Recipient.builder().email("slowik@onet.pl").build();
    }
    private Recipient recipient(String email){
        return Recipient.builder().email(email).build();
    }
    private Long placeOrder(Long bookId, int copies, String recipient) {
        PlaceOrderCommand command = PlaceOrderCommand
                .builder()
                .recipient(recipient(recipient))
                .item(new OrderItemCommand(bookId, copies))
                .build();
        return service.placeOrder(command).getRight();
    }
    private Long placeOrder(Long bookId, int copies) {
        return placeOrder(bookId, copies, "john@test.order");
    }
}