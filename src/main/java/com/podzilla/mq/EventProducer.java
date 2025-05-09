package com.podzilla.mq;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

import lombok.AllArgsConstructor;

@Component
@AllArgsConstructor
public class EventProducer {
    private final RabbitTemplate rabbitTemplate;

    public void publishEvent(EventMetadata eventMetadata) {
        rabbitTemplate.convertAndSend(eventMetadata.getExchange(),
                eventMetadata.getKey(), "Hello World");
    }
}
