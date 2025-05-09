package com.podzilla.mq;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class EventMetadata {
    public final String name;
    public final String key;
    public final String exchange;
}
