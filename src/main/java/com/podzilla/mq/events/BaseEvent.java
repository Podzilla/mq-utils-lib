package com.podzilla.mq.events;

import lombok.Builder;
import lombok.Data;
import lombok.experimental.SuperBuilder;
import java.time.Instant;

@Data
@SuperBuilder
public abstract class BaseEvent implements EventPayload {

    private static final long serialVersionUID = 1L;

    @Builder.Default
    private Instant timestamp = Instant.now();

    
    public BaseEvent() {
        this.timestamp = Instant.now();
    }
}
