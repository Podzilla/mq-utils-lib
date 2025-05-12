package com.podzilla.mq.events;

import java.io.Serializable;

/**
 * Marker interface for all event payload objects.
 * All event data classes should implement this interface.
 */
public interface EventPayload extends Serializable {
}
