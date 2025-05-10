package com.podzilla.mq.events;

import lombok.Data;
import lombok.experimental.SuperBuilder;
import java.time.Instant;
import java.util.UUID;

/**
 * Abstract base class for all event payloads.
 * Contains common auditing fields like eventId and timestamp.
 */
@Data
@SuperBuilder
public abstract class BaseEvent implements EventPayload {

    private static final long serialVersionUID = 1L;

    private String eventId;
    private Instant timestamp;

    public BaseEvent() {
        this.eventId = UUID.randomUUID().toString();
        this.timestamp = Instant.now();
    }
}