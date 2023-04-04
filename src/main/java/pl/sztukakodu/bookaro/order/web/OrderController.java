package pl.sztukakodu.bookaro.order.web;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import pl.sztukakodu.bookaro.Security.UserSecurity;
import pl.sztukakodu.bookaro.order.application.RichOrder;
import pl.sztukakodu.bookaro.order.application.port.ManipulateOrderUseCase;
import pl.sztukakodu.bookaro.order.application.port.QueryOrderUseCase;
import pl.sztukakodu.bookaro.order.domain.OrderStatus;
import pl.sztukakodu.bookaro.web.CreatedURI;

import java.net.URI;
import java.util.List;
import java.util.Map;

import static org.springframework.http.HttpStatus.FORBIDDEN;
import static pl.sztukakodu.bookaro.order.application.port.ManipulateOrderUseCase.*;

@RestController
@RequestMapping("orders")
@AllArgsConstructor
public class OrderController {
    private final ManipulateOrderUseCase manipulateOrder;
    private final QueryOrderUseCase queryOrder;
    private final UserSecurity userSecurity;
    @Secured({"ROLE_ADMIN"})
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<RichOrder> getOrders() {
        return queryOrder.findAll();
    }
    @Secured({"ROLE_ADMIN", "ROLE_USER"})
    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<RichOrder> getById(@PathVariable Long id, @AuthenticationPrincipal UserDetails user) {
        return queryOrder.findById(id)
                .map(order -> authorize(order, user))
                .orElse(ResponseEntity.notFound().build());
    }

    private ResponseEntity authorize(RichOrder order, UserDetails user){
        if(userSecurity.isOwnerOrAdmin(order.getRecipient().getEmail(), user)){
            return ResponseEntity.ok(order);
        }
        return ResponseEntity.status(FORBIDDEN).build();
    }

    @Secured({"ROLE_ADMIN"})
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
    @Secured({"ROLE_ADMIN", "ROLE_USER"})
    @PutMapping("/{id}/status")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public ResponseEntity<Object> updateOrderStatus(@PathVariable Long id, @RequestBody Map<String, String> body, @AuthenticationPrincipal UserDetails user) {
        String status = body.get("status");
        OrderStatus orderStatus = OrderStatus
                .parseString(status)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Unknown status: " + status));
        UpdateStatusCommand updateStatusCommand = new UpdateStatusCommand(id, orderStatus, user);
        return manipulateOrder.updateOrderStatus(updateStatusCommand)
                .handle(
                        newStatus -> ResponseEntity.accepted().build(),
                        error -> ResponseEntity.status(error.getStatus()).build()
                );
    }

    URI orderUri(Long orderId) {
        return new CreatedURI("/" + orderId).uri();
    }

}
