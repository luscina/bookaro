package pl.sztukakodu.bookaro.order.web;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import pl.sztukakodu.bookaro.order.application.RichOrder;
import pl.sztukakodu.bookaro.order.application.port.ManipulateOrderUseCase;
import pl.sztukakodu.bookaro.order.application.port.QueryOrderUseCase;
import pl.sztukakodu.bookaro.order.domain.OrderStatus;
import pl.sztukakodu.bookaro.web.CreatedURI;

import java.net.URI;
import java.util.List;
import java.util.Map;

import static pl.sztukakodu.bookaro.order.application.port.ManipulateOrderUseCase.*;

@RestController
@RequestMapping("orders")
@AllArgsConstructor
public class OrderController {
    private final ManipulateOrderUseCase manipulateOrder;
    private final QueryOrderUseCase queryOrder;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<RichOrder> getOrders() {
        return queryOrder.findAll();
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<RichOrder> getById(@PathVariable Long id) {
        return queryOrder.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteById(@PathVariable Long id) {
        queryOrder.deleteById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<?> addOrder(@RequestBody PlaceOrderCommand command) {
        return manipulateOrder
                .placeOrder(command)
                .handle(
                    orderId -> ResponseEntity.created(orderUri(orderId)).build(),
                    error -> ResponseEntity.badRequest().body(error)
                );
    }

    @PutMapping("/{id}/status")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public void updateOrderStatus(@PathVariable Long id, @RequestBody Map<String, String> body) {
        String status = body.get("status");
        OrderStatus orderStatus = OrderStatus
                .parseString(status)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Unknown status: " + status));
        UpdateStatusCommand updateStatusCommand = new UpdateStatusCommand(id, orderStatus, null);
        manipulateOrder.updateOrderStatus(updateStatusCommand);
    }

    URI orderUri(Long orderId) {
        return new CreatedURI("/" + orderId).uri();
    }

}
