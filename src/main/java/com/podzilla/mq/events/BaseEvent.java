package com.podzilla.mq.events;

import lombok.Data;
import lombok.experimental.SuperBuilder;
import java.time.Instant;

@Data
@SuperBuilder
public abstract class BaseEvent implements EventPayload {

    private static final long serialVersionUID = 1L;

    private Instant timestamp;

    public BaseEvent() {
        this.timestamp = Instant.now();
    }
}
