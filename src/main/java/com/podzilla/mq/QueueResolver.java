package com.podzilla.mq;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Data;

public final class QueueResolver {

    private QueueResolver() {
    }

    @Data
    @AllArgsConstructor
    private static class ServiceEventKey {
        private final String serviceName;
        private final String eventName;
    }

    private static final Map<ServiceEventKey, String> QUEUE_LISTEN_MAP;

    static {
        Map<ServiceEventKey, String> tempMap = new HashMap<>();

        tempMap.put(
                new ServiceEventKey(EventsConstants.SERVICE_ANALYTICS, EventsConstants.COURIER_REGISTERED.getName()),
                EventsConstants.ANALYTICS_USER_EVENT_QUEUE);
        tempMap.put(
                new ServiceEventKey(EventsConstants.SERVICE_ANALYTICS, EventsConstants.CUSTOMER_REGISTERED.getName()),
                EventsConstants.ANALYTICS_USER_EVENT_QUEUE);
        tempMap.put(new ServiceEventKey(EventsConstants.SERVICE_ANALYTICS, EventsConstants.PRODUCT_CREATED.getName()),
                EventsConstants.ANALYTICS_INVENTORY_EVENT_QUEUE);
        tempMap.put(new ServiceEventKey(EventsConstants.SERVICE_ANALYTICS, EventsConstants.INVENTORY_UPDATED.getName()),
                EventsConstants.ANALYTICS_INVENTORY_EVENT_QUEUE);
        tempMap.put(new ServiceEventKey(EventsConstants.SERVICE_ANALYTICS, EventsConstants.ORDER_PLACED.getName()),
                EventsConstants.ANALYTICS_ORDER_EVENT_QUEUE);
        tempMap.put(new ServiceEventKey(EventsConstants.SERVICE_ANALYTICS, EventsConstants.ORDER_CANCELLED.getName()),
                EventsConstants.ANALYTICS_ORDER_EVENT_QUEUE);
        tempMap.put(
                new ServiceEventKey(EventsConstants.SERVICE_ANALYTICS,
                        EventsConstants.ORDER_ASSIGNED_TO_COURIER.getName()),
                EventsConstants.ANALYTICS_ORDER_EVENT_QUEUE);
        tempMap.put(
                new ServiceEventKey(EventsConstants.SERVICE_ANALYTICS,
                        EventsConstants.ORDER_OUT_FOR_DELIVERY.getName()),
                EventsConstants.ANALYTICS_ORDER_EVENT_QUEUE);
        tempMap.put(new ServiceEventKey(EventsConstants.SERVICE_ANALYTICS, EventsConstants.ORDER_DELIVERED.getName()),
                EventsConstants.ANALYTICS_ORDER_EVENT_QUEUE);
        tempMap.put(
                new ServiceEventKey(EventsConstants.SERVICE_ANALYTICS, EventsConstants.ORDER_DELIVERY_FAILED.getName()),
                EventsConstants.ANALYTICS_ORDER_EVENT_QUEUE);

        // --- Order Service Bindings ---
        tempMap.put(
                new ServiceEventKey(EventsConstants.SERVICE_ORDER, EventsConstants.WAREHOUSE_STOCK_RESERVED.getName()),
                EventsConstants.ORDER_INVENTORY_EVENT_QUEUE);
        tempMap.put(
                new ServiceEventKey(EventsConstants.SERVICE_ORDER,
                        EventsConstants.WAREHOUSE_ORDER_FULFILLMENT_FAILED.getName()),
                EventsConstants.ORDER_INVENTORY_EVENT_QUEUE);
        tempMap.put(new ServiceEventKey(EventsConstants.SERVICE_ORDER, EventsConstants.CART_CHECKEDOUT.getName()),
                EventsConstants.ORDER_ORDER_EVENT_QUEUE);
        tempMap.put(new ServiceEventKey(EventsConstants.SERVICE_ORDER, EventsConstants.ORDER_PACKAGED.getName()),
                EventsConstants.ORDER_ORDER_EVENT_QUEUE);
        tempMap.put(
                new ServiceEventKey(EventsConstants.SERVICE_ORDER, EventsConstants.ORDER_ASSIGNED_TO_COURIER.getName()),
                EventsConstants.ORDER_ORDER_EVENT_QUEUE);
        tempMap.put(
                new ServiceEventKey(EventsConstants.SERVICE_ORDER, EventsConstants.ORDER_OUT_FOR_DELIVERY.getName()),
                EventsConstants.ORDER_ORDER_EVENT_QUEUE);
        tempMap.put(new ServiceEventKey(EventsConstants.SERVICE_ORDER, EventsConstants.ORDER_DELIVERED.getName()),
                EventsConstants.ORDER_ORDER_EVENT_QUEUE);

        // --- Warehouse Service Bindings ---
        tempMap.put(
                new ServiceEventKey(EventsConstants.SERVICE_WAREHOUSE,
                        EventsConstants.ORDER_STOCK_RESERVATION_REQUESTED.getName()),
                EventsConstants.WAREHOUSE_INVENTORY_EVENT_QUEUE);
        tempMap.put(new ServiceEventKey(EventsConstants.SERVICE_WAREHOUSE, EventsConstants.ORDER_PLACED.getName()),
                EventsConstants.WAREHOUSE_ORDER_EVENT_QUEUE);
        tempMap.put(
                new ServiceEventKey(EventsConstants.SERVICE_WAREHOUSE, EventsConstants.ORDER_DELIVERY_FAILED.getName()),
                EventsConstants.WAREHOUSE_ORDER_EVENT_QUEUE);

        // --- Courier Service Bindings ---
        tempMap.put(new ServiceEventKey(EventsConstants.SERVICE_COURIER, EventsConstants.COURIER_REGISTERED.getName()),
                EventsConstants.COURIER_USER_EVENT_QUEUE);
        tempMap.put(
                new ServiceEventKey(EventsConstants.SERVICE_COURIER, EventsConstants.ORDER_OUT_FOR_DELIVERY.getName()),
                EventsConstants.COURIER_ORDER_EVENT_QUEUE);
        tempMap.put(
                new ServiceEventKey(EventsConstants.SERVICE_COURIER,
                        EventsConstants.ORDER_ASSIGNED_TO_COURIER.getName()),
                EventsConstants.COURIER_ORDER_EVENT_QUEUE);

        QUEUE_LISTEN_MAP = Collections.unmodifiableMap(tempMap);
    }

    /**
     * Returns the RabbitMQ queue name that a given service should listen to for a
     * specific event.
     *
     * @param serviceName   The name of the service (e.g.,
     *                      EventsConstants.SERVICE_ANALYTICS,
     *                      EventsConstants.SERVICE_ORDER). This input is
     *                      case-insensitive (e.g., "Analytics" will match
     *                      "analytics").
     * @param eventMetadata The EventMetadata object representing the event (e.g.,
     *                      EventsConstants.ORDER_PLACED).
     * @return The queue name string, or {@code null} if no such binding is found or
     *         if inputs are invalid.
     */
    public static String getQueueForServiceEvent(String serviceName, EventsConstants.EventMetadata eventMetadata) {
        if (serviceName == null || eventMetadata == null || eventMetadata.getName() == null) {
            return null;
        }
        String normalizedServiceName = serviceName.toLowerCase();
        ServiceEventKey key = new ServiceEventKey(normalizedServiceName, eventMetadata.getName());
        return QUEUE_LISTEN_MAP.get(key);
    }
}