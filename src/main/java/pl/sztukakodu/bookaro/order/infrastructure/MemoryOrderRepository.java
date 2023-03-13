package pl.sztukakodu.bookaro.order.infrastructure;

import org.springframework.stereotype.Repository;
import pl.sztukakodu.bookaro.order.domain.Order;
import pl.sztukakodu.bookaro.order.domain.OrderRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@Repository
public class MemoryOrderRepository implements OrderRepository {
    private Map<Long, Order> storage = new ConcurrentHashMap<>();
    private AtomicLong NEXT_ID = new AtomicLong(0L);

    @Override
    public Order save(Order order) {
        if(order.getId() != null){
            storage.put(order.getId(), order);
        }else {
            long nextId = nextId();
            order.setId(nextId);
            order.setCreatedAt(LocalDateTime.now());
            storage.put(nextId, order);
        }
        return order;
    }

    @Override
    public List<Order> findAll() {
        return new ArrayList<>(storage.values());
    }

    @Override
    public Optional<Order> findById(Long id) {
        return Optional.of(storage.get(id));
    }

    @Override
    public void deleteById(Long id) {
        storage.remove(id);
    }

    private long nextId() {
        return NEXT_ID.getAndIncrement();
    }
}
