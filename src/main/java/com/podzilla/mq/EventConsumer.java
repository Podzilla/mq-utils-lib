package com.podzilla.mq;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class EventConsumer {

    @RabbitListener(queues = EventsConstants.ANALYTICS_USER_EVENT_QUEUE)
    public void consumeEvent(String payload) {
        System.out.println("Consuming event: " + payload);
    }
}
