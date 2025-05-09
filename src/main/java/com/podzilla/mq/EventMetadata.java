package com.podzilla.mq;

public class EventMetadata {
    public final String name;
    public final String key;
    public final String exchange;

    public EventMetadata(String name, String key, String exchange) {
        this.name = name;
        this.key = key;
        this.exchange = exchange;
    }

    public String getName() {
        return name;
    }

    public String getKey() {
        return key;
    }

    public String getExchange() {
        return exchange;
    }

}
