package com.podzilla.mq;

public class EventMetadata {
    public final String name;
    public final String routingKey;
    public final String exchange;

    public EventMetadata(String name, String key, String exchange) {
        this.name = name;
        this.routingKey = key;
        this.exchange = exchange;
    }

    public String getName() {
        return name;
    }

    public String getRoutingKey() {
        return routingKey;
    }

    public String getExchange() {
        return exchange;
    }
}
