package pl.sztukakodu.bookaro.order.application.port;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import pl.sztukakodu.bookaro.Clock.Clock;
import pl.sztukakodu.bookaro.order.application.port.ManipulateOrderUseCase.UpdateStatusCommand;
import pl.sztukakodu.bookaro.order.db.OrderJpaRepository;
import pl.sztukakodu.bookaro.order.domain.Order;
import pl.sztukakodu.bookaro.order.domain.OrderStatus;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
@Slf4j
@Component
@AllArgsConstructor
public class AbandonedOrdersJob {
    private final Clock clock;
    private final OrderJpaRepository repository;
    private final ManipulateOrderUseCase orderUseCase;
    private final OrderProperties properties;
    @Scheduled(cron = "${app.orders.abandon-cron}")
    @Transactional
    public void run(){
        Duration paymentPeriod = properties.getPaymentPeriod();
        LocalDateTime olderThen = clock.now().minus(paymentPeriod);
        List<Order> orders = repository.findByStatusAndCreatedAtLessThanEqual(OrderStatus.NEW, olderThen);
        log.info("Find orders to be abandoned " + orders.size());
        orders.forEach(order -> {
            UpdateStatusCommand updateStatusCommand = new UpdateStatusCommand(order.getId(), OrderStatus.ABANDONED, order.getRecipient().getEmail());
            orderUseCase.updateOrderStatus(updateStatusCommand);
        });
    }
}
