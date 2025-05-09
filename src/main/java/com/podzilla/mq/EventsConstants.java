package com.podzilla.mq;

public final class EventsConstants {

    private EventsConstants() {
    }

    public static final String ABC = "abc";

    public static final String ANALYTICS_USER_EVENT_QUEUE = "analytics_user_event_queue";
    public static final String ANALYTICS_ORDER_EVENT_QUEUE = "analytics_order_event_queue";
    public static final String ANALYTICS_INVENTORY_EVENT_QUEUE = "analytics_inventory_event_queue";
    public static final String ORDER_ORDER_EVENT_QUEUE = "order_order_event_queue";
    public static final String WAREHOUSE_ORDER_EVENT_QUEUE = "warehouse_order_event_queue";
    public static final String COURIER_ORDER_EVENT_QUEUE = "courier_order_event_queue";

    public static final EventMetadata COURIER_REGISTERED = new EventMetadata(
            "CourierRegistered",
            "courier.registered",
            "courier_exchange");
    public static final EventMetadata CUSTOMER_REGISTERED = new EventMetadata(
            "CustomerRegistered",
            "customer.registered",
            "customer_exchange");

    public static final EventMetadata PRODUCT_CREATED = new EventMetadata(
            "ProductCreated",
            "product.created",
            "inventory_exchange");

    public static final EventMetadata INVENTORY_UPDATED = new EventMetadata(
            "InventoryUpdated",
            "inventory.updated",
            "inventory_exchange");

    public static final EventMetadata CART_CHECKEDOUT = new EventMetadata(
            "CartCheckedOut",
            "cart.checkout",
            "order_exchange");

    public static final EventMetadata ORDER_PLACED = new EventMetadata(
            "OrderPlaced",
            "order.placed",
            "order_exchange");

    public static final EventMetadata ORDER_CANCELLED = new EventMetadata(
            "OrderCancelled",
            "order.cancelled",
            "order_exchange");

    public static final EventMetadata ORDER_PACKAGED = new EventMetadata(
            "OrderPackaged",
            "order.packaged",
            "order_exchange");

    public static final EventMetadata ORDER_ASSIGNED_TO_COURIER = new EventMetadata(
            "OrderAssignedToCourier",
            "order.assigned_to_courier",
            "order_exchange");

    public static final EventMetadata ORDER_SHIPPED = new EventMetadata(
            "OrderShipped",
            "order.shipped",
            "order_exchange");

    public static final EventMetadata ORDER_DELIVERED = new EventMetadata(
            "OrderDelivered",
            "order.delivered",
            "order_exchange");

    public static final EventMetadata ORDER_FAILED = new EventMetadata(
            "OrderFailed",
            "order.failed",
            "order_exchange");
}
