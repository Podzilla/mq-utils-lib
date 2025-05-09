package com.podzilla.mq.utils;

import com.podzilla.mq.EventMetadata;

public class RabbitMqNaming {

    private RabbitMqNaming() {
    }

    private static final String QUEUE_NAME_FORMAT = "%s_%s_queue";

    public static String getQueueName(String exchangeName, String serviceName) {
        return String.format(QUEUE_NAME_FORMAT, serviceName, exchangeName);
    }

    public static String getQueueName(EventMetadata event, String serviceName) {

        return String.format(QUEUE_NAME_FORMAT, serviceName, event.exchange);
    }
}
