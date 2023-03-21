package pl.sztukakodu.bookaro.order.application.port;

import lombok.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.bind.ConstructorBinding;

import java.time.Duration;

@Value
@ConfigurationProperties("app.orders")
public class OrderProperties {
    String abandonCone;
    Duration paymentPeriod;
}
