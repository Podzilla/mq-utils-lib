package com.podzilla.mq;

import com.podzilla.mq.events.EventPayload;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Component
public class EventPublisher {

    private final RabbitTemplate rabbitTemplate;

    /**
     * Constructor for dependency injection. Spring automatically injects the
     * RabbitTemplate
     * bean that was configured in RabbitMQConfig.
     * 
     * @param rabbitTemplate The configured RabbitTemplate instance.
     */
    public EventPublisher(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    /**
     * Publishes an event to RabbitMQ.
     * The payload will be automatically serialized to JSON by the configured
     * MessageConverter.
     *
     * @param eventMetadata The metadata (exchange, routing key) of the event to
     *                      publish.
     * @param payload       The actual event payload object. This should be
     *                      a POJO that Jackson can serialize.
     * @param <T>           The type of the event payload.
     */
    public <T extends EventPayload> void publishEvent(
            EventsConstants.EventMetadata eventMetadata, T payload) {
        if (eventMetadata == null || payload == null) {
            System.err.println("Cannot publish event: metadata or payload is null.");
            return;
        }

        try {
            rabbitTemplate.convertAndSend(
                    eventMetadata.getExchange(),
                    eventMetadata.getRoutingKey(),
                    payload);
            System.out.printf("Published event '%s' to exchange '%s' with routing key '%s'%n",
                    eventMetadata.getName(), eventMetadata.getExchange(), eventMetadata.getRoutingKey());
        } catch (Exception e) {
            System.err.printf("Failed to publish event '%s': %s%n", eventMetadata.getName(), e.getMessage());
        }
    }
}