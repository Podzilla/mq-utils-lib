package com.podzilla.mq.utils;

import com.podzilla.mq.EventMetadata;

public class RabbitMqNaming {

    private RabbitMqNaming() {}

    private static final String QUEUE_NAME_FORMAT = "QUEUE_%s_%s_%s";

    public static String getQueueName(String exchangeName, String eventName, String serviceName) {
        return String.format(QUEUE_NAME_FORMAT, exchangeName, eventName, serviceName);
    }

    public static String getQueueName(EventMetadata event, String serviceName) {

        return String.format(QUEUE_NAME_FORMAT, event.exchange, event.name , serviceName);
    }
}