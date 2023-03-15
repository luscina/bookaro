package pl.sztukakodu.bookaro;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import pl.sztukakodu.bookaro.catalog.application.port.CatalogUseCase;
import pl.sztukakodu.bookaro.catalog.application.port.CatalogUseCase.CreateBookCommand;
import pl.sztukakodu.bookaro.catalog.db.AuthorJpaRepository;
import pl.sztukakodu.bookaro.catalog.domain.Author;
import pl.sztukakodu.bookaro.catalog.domain.Book;
import pl.sztukakodu.bookaro.order.application.port.ManipulateOrderUseCase;
import pl.sztukakodu.bookaro.order.application.port.QueryOrderUseCase;
import pl.sztukakodu.bookaro.order.domain.OrderItem;
import pl.sztukakodu.bookaro.order.domain.Recipient;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;

import static pl.sztukakodu.bookaro.order.application.port.ManipulateOrderUseCase.*;

@Component
@AllArgsConstructor
public class ApplicationStartup implements CommandLineRunner {
    private final CatalogUseCase catalog;
    private final ManipulateOrderUseCase placeOrder;
    private final QueryOrderUseCase queryOrder;
    private final AuthorJpaRepository authorRepository;

    @Override
    public void run(String... args) throws Exception {
        initData();
        placeOrder();
    }

    private void placeOrder() {
        Book effective = catalog.findOneByTitle("Efficient").orElseThrow(() -> new IllegalStateException("Cannot find book"));
        Book puzzler = catalog.findOneByTitle("Puzzlers").orElseThrow(() -> new IllegalStateException("Cannot find book"));
        Recipient recipient = Recipient
                .builder()
                .name("Jan Kowalski")
                .phone("123-456-789")
                .street("Armii Krajowej 31")
                .city("Krakow")
                .zipCode("30-150")
                .email("jan@example.org")
                .build();
        PlaceOrderCommand command = PlaceOrderCommand
                .builder()
                .recipient(recipient)
                .item(new OrderItem(effective.getId(), 16))
                .item(new OrderItem(puzzler.getId(), 7))
                .build();

        PlaceOrderResponse response = placeOrder.placeOrder(command);
        String result = response.handle(
                orderId -> "Created ORDER with id: " + orderId,
                error -> "Failed to created order: " + error
        );
        System.out.println(result);
        queryOrder.findAll()
                .forEach(order -> System.out.println("GOT ORDER WITH TOTAL PRICE: " + order.totalPrice() + " DETAILS: " + order));
    }

    private void initData() {
        Author joshuaBlock = new Author("Joshua", "Block");
        Author neal = new Author("Neal", "Gafter");
        authorRepository.save(joshuaBlock);
        authorRepository.save(neal);
        CreateBookCommand effictvieJava = new CreateBookCommand(
                "Efficient Java",
                Set.of(joshuaBlock.getId()),
                2006,
                new BigDecimal("169.99")
        );
        CreateBookCommand javaPuzzlers = new CreateBookCommand(
                "Java Puzzlers",
                Set.of(joshuaBlock.getId(), neal.getId()),
                2018,
                new BigDecimal("99.00")
        );
        catalog.addBook(effictvieJava);
        catalog.addBook(javaPuzzlers);
    }
}
