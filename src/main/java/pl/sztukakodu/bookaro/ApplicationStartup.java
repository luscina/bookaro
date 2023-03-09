package pl.sztukakodu.bookaro;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;
import pl.sztukakodu.bookaro.catalog.application.port.CatalogUseCase;
import pl.sztukakodu.bookaro.catalog.application.port.CatalogUseCase.CreateBookCommand;
import pl.sztukakodu.bookaro.catalog.application.port.CatalogUseCase.UpdateBookCommand;
import pl.sztukakodu.bookaro.catalog.domain.Book;
import pl.sztukakodu.bookaro.order.application.port.PlaceOrderUseCase;
import pl.sztukakodu.bookaro.order.application.port.QueryOrderUseCase;
import pl.sztukakodu.bookaro.order.domain.OrderItem;

import java.math.BigDecimal;
import java.util.List;

import static pl.sztukakodu.bookaro.order.application.port.PlaceOrderUseCase.*;

@Component
public class ApplicationStartup implements CommandLineRunner {
    private final CatalogUseCase catalog;
    private final String title;
    private final PlaceOrderUseCase placeOrder;
    private final QueryOrderUseCase queryOrder;

    public ApplicationStartup(
            CatalogUseCase catalog,
            @Value("${bookaro.catalog.query}") String title,
            PlaceOrderUseCase placeOrder,
            QueryOrderUseCase queryOrder
    ) {
            this.catalog = catalog;
            this.title = title;
            this.placeOrder = placeOrder;
            this.queryOrder = queryOrder;
    }

    @Override
    public void run(String... args) throws Exception {
        initData();
    }

    private void placeOrder() {
        Book sezon = catalog.findOneByTitle("Sezon").orElseThrow(() -> new IllegalStateException("Cannot find book"));
        Book harry = catalog.findOneByTitle("Harry").orElseThrow(() -> new IllegalStateException("Cannot find book"));
        PlaceOrderCommand command = PlaceOrderCommand.builder()
                .recipient(null)
                .item(new OrderItem(sezon, 2))
                .item(new OrderItem(harry, 3))
                .build();
        PlaceOrderResponse response = placeOrder.placeOrder(command);
        System.out.println("Created order with id : " + response.getOrderId());
        queryOrder.findAll()
                .forEach(order -> {
                    System.out.println("GOT ORDER WITH TOTAL PRICE " + order.totalPrice() + " DETAILS " + order);
                });
    }

    private void searchCatalog() {

    }


    private void initData() {
        catalog.addBook(new CreateBookCommand("Harry Potter i Komnata Tajemnic", "JK Rowlings", 1988, BigDecimal.valueOf(19.99)));
        catalog.addBook(new CreateBookCommand("Władca Pierścieni: Dwie wieże", "JRR Tolkien", 1954, new BigDecimal(10)));
        catalog.addBook(new CreateBookCommand("Mężczyźni, którzy nienawidzą kobiet", "Stieg Larsson", 2005, new BigDecimal(10)));
        catalog.addBook(new CreateBookCommand("Sezon Burz", "Andrzej Sapkowski", 2013, BigDecimal.valueOf(29.99)));
    }

    private void findByTitle() {
        List<Book> panTadeusz = catalog.findByTitle(title);
        panTadeusz.forEach(System.out::println);
    }
}
