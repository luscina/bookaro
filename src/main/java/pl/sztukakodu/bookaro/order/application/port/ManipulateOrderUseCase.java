package pl.sztukakodu.bookaro.order.application.port;

import lombok.*;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.User;
import pl.sztukakodu.bookaro.commons.Either;
import pl.sztukakodu.bookaro.order.domain.Delivery;
import pl.sztukakodu.bookaro.order.domain.OrderStatus;
import pl.sztukakodu.bookaro.order.domain.Recipient;

import java.util.List;

public interface ManipulateOrderUseCase {
    PlaceOrderResponse placeOrder(PlaceOrderCommand command);
    UpdateStatusResponse updateOrderStatus(UpdateStatusCommand updateStatusCommand);

    void deleteOrderById(Long id);
    @Builder
    @Value
    @AllArgsConstructor
    public class PlaceOrderCommand {
        @Singular
        List<OrderItemCommand> items;
        Recipient recipient;
        Delivery delivery;
    }
    @Data
    @AllArgsConstructor
    static class OrderItemCommand {
        Long bookId;
        int quantity;
    }
    @Value
    class PlaceOrderResponse extends Either<String, Long> {
        public PlaceOrderResponse(boolean success, String left, Long right){
            super(success, left, right);
        }

        public static PlaceOrderResponse success(Long orderId) {
            return new PlaceOrderResponse(true, null, orderId);
        }

        public static PlaceOrderResponse failure(String error) {
            return new PlaceOrderResponse(false, error, null);
        }
    }

    class UpdateStatusResponse extends Either<Error, OrderStatus> {
        public UpdateStatusResponse(boolean success, Error left, OrderStatus right){
            super(success, left, right);
        }

        public static UpdateStatusResponse success(OrderStatus orderStatus) {
            return new UpdateStatusResponse(true, null, orderStatus);
        }

        public static UpdateStatusResponse failure(Error error) {
            return new UpdateStatusResponse(false, error, null);
        }
    }

    @AllArgsConstructor
    @Getter
    enum Error{
        NOT_FOUND(HttpStatus.NOT_FOUND),
        FORBIDDEN(HttpStatus.FORBIDDEN);

        private final HttpStatus status;
    }

    @Value
    class UpdateStatusCommand{
        Long orderId;
        OrderStatus status;
        User user;
    }
}
