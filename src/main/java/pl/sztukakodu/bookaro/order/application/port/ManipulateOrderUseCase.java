package pl.sztukakodu.bookaro.order.application.port;

import lombok.*;
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

    class UpdateStatusResponse extends Either<String, OrderStatus> {
        public UpdateStatusResponse(boolean success, String left, OrderStatus right){
            super(success, left, right);
        }

        public static UpdateStatusResponse success(OrderStatus orderStatus) {
            return new UpdateStatusResponse(true, null, orderStatus);
        }

        public static UpdateStatusResponse failure(String error) {
            return new UpdateStatusResponse(false, error, null);
        }
    }

    @Value
    class UpdateStatusCommand{
        Long orderId;
        OrderStatus status;
        String email;
    }
}
