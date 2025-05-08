package com.podzilla.mq;


public final class EventsConstants {

    private EventsConstants() {}


    public static final EventMetadata USER_CREATED = new EventMetadata(
        "UserCreated",
        "user.created",
        "user-exchange"
    );

    public static final EventMetadata ORDER_PLACED = new EventMetadata(
        "OrderPlaced",
        "order.placed",
        "order-exchange"
    );



}

