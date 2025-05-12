package com.podzilla.mq;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

public final class EventsConstants {

    private EventsConstants() {
    }

    // --- Service Names Constants ---
    public static final String SERVICE_ANALYTICS = "analytics";
    public static final String SERVICE_ORDER = "order";
    public static final String SERVICE_WAREHOUSE = "warehouse";
    public static final String SERVICE_COURIER = "courier";

    // --- Queue Names ---

    // analytics queues
    public static final String ANALYTICS_USER_EVENT_QUEUE = "analytics_user_event_queue";
    public static final String ANALYTICS_ORDER_EVENT_QUEUE = "analytics_order_event_queue";
    public static final String ANALYTICS_INVENTORY_EVENT_QUEUE = "analytics_inventory_event_queue";

    // order queues
    public static final String ORDER_ORDER_EVENT_QUEUE = "order_order_event_queue";
    public static final String ORDER_INVENTORY_EVENT_QUEUE = "order_inventory_event_queue";

    // warehouse queues
    public static final String WAREHOUSE_ORDER_EVENT_QUEUE = "warehouse_order_event_queue";
    public static final String WAREHOUSE_INVENTORY_EVENT_QUEUE = "warehouse_inventory_event_queue";

    // courier queues
    public static final String COURIER_USER_EVENT_QUEUE = "courier_user_event_queue";
    public static final String COURIER_ORDER_EVENT_QUEUE = "courier_order_event_queue";

    // --- Event Metadata (Routing Keys and Exchanges) ---
    @Getter
    @RequiredArgsConstructor
    public static class EventMetadata {
        private final String name;
        private final String routingKey;
        private final String exchange;
    }

    // User Events
    public static final EventMetadata COURIER_REGISTERED = new EventMetadata(
            "CourierRegistered",
            "courier.registered",
            "user_exchange");
    public static final EventMetadata CUSTOMER_REGISTERED = new EventMetadata(
            "CustomerRegistered",
            "customer.registered",
            "user_exchange");

    // Inventory Events
    public static final EventMetadata PRODUCT_CREATED = new EventMetadata(
            "ProductCreated",
            "product.created",
            "inventory_exchange");
    public static final EventMetadata INVENTORY_UPDATED = new EventMetadata(
            "InventoryUpdated",
            "inventory.updated",
            "inventory_exchange");
    public static final EventMetadata ORDER_STOCK_RESERVATION_REQUESTED = new EventMetadata(
            "OrderStockReservationRequested",
            "order.stock_reservation_requested",
            "inventory_exchange");
    public static final EventMetadata WAREHOUSE_STOCK_RESERVED = new EventMetadata(
            "WarehouseStockReserved",
            "warehouse.stock_reserved",
            "inventory_exchange");
    public static final EventMetadata WAREHOUSE_ORDER_FULFILLMENT_FAILED = new EventMetadata(
            "WarehouseOrderFulfillmentFailed",
            "warehouse.order_fulfillment_failed",
            "inventory_exchange");

    // Order Events
    public static final EventMetadata CART_CHECKEDOUT = new EventMetadata(
            "CartCheckedOut",
            "cart.checkedout",
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
    public static final EventMetadata ORDER_OUT_FOR_DELIVERY = new EventMetadata(
            "OrderOutForDelivery",
            "order.out_for_delivery",
            "order_exchange");
    public static final EventMetadata ORDER_DELIVERED = new EventMetadata(
            "OrderDelivered",
            "order.delivered",
            "order_exchange");
    public static final EventMetadata ORDER_DELIVERY_FAILED = new EventMetadata(
            "OrderDeliveryFailed",
            "order.delivery_failed",
            "order_exchange");
}
