package pl.sztukakodu.bookaro.Clock;

import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class SystemClock implements Clock {
    @Override
    public LocalDateTime now() {
        return LocalDateTime.now();
    }
}
