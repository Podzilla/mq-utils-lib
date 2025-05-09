package com.podzilla.mq;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Component
public class EventProducer {
    private final RabbitTemplate rabbitTemplate;

    public EventProducer(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void publishEvent(EventMetadata eventMetadata) {
        rabbitTemplate.convertAndSend(eventMetadata.getExchange(),
                eventMetadata.getRoutingKey(), "Hello World");
    }
}
