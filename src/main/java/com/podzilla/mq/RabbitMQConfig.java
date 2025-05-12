package com.podzilla.mq;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    @Bean
    public TopicExchange userExchange() {
        return new TopicExchange("user_exchange", true, false);
    }

    @Bean
    public TopicExchange inventoryExchange() {
        return new TopicExchange("inventory_exchange", true, false);
    }

    @Bean
    public TopicExchange orderExchange() {
        return new TopicExchange("order_exchange", true, false);
    }

    // Analytics Queues
    @Bean
    public Queue analyticsUserEventQueue() {
        return new Queue(EventsConstants.ANALYTICS_USER_EVENT_QUEUE, true, false, false);
    }

    @Bean
    public Queue analyticsOrderEventQueue() {
        return new Queue(EventsConstants.ANALYTICS_ORDER_EVENT_QUEUE, true, false, false);
    }

    @Bean
    public Queue analyticsInventoryEventQueue() {
        return new Queue(EventsConstants.ANALYTICS_INVENTORY_EVENT_QUEUE, true, false, false);
    }

    // Order Queues
    @Bean
    public Queue orderOrderEventQueue() {
        return new Queue(EventsConstants.ORDER_ORDER_EVENT_QUEUE, true, false, false);
    }

    @Bean
    public Queue orderInventoryEventQueue() {
        return new Queue(EventsConstants.ORDER_INVENTORY_EVENT_QUEUE, true, false, false);
    }

    // Warehouse Queues
    @Bean
    public Queue warehouseOrderEventQueue() {
        return new Queue(EventsConstants.WAREHOUSE_ORDER_EVENT_QUEUE, true, false, false);
    }

    @Bean
    public Queue warehouseInventoryEventQueue() {
        return new Queue(EventsConstants.WAREHOUSE_INVENTORY_EVENT_QUEUE, true, false, false);
    }

    // Courier Queues
    @Bean
    public Queue courierUserEventQueue() {
        return new Queue(EventsConstants.COURIER_USER_EVENT_QUEUE, true, false, false);
    }

    @Bean
    public Queue courierOrderEventQueue() {
        return new Queue(EventsConstants.COURIER_ORDER_EVENT_QUEUE, true, false, false);
    }

    // Bindings for User Events
    @Bean
    public Binding bindCourierRegisteredToAnalyticsUser() {
        return BindingBuilder.bind(analyticsUserEventQueue())
                .to(userExchange())
                .with(EventsConstants.COURIER_REGISTERED.getRoutingKey());
    }

    @Bean
    public Binding bindCustomerRegisteredToAnalyticsUser() {
        return BindingBuilder.bind(analyticsUserEventQueue())
                .to(userExchange())
                .with(EventsConstants.CUSTOMER_REGISTERED.getRoutingKey());
    }

    @Bean
    public Binding bindCourierRegisteredToCourierUser() {
        return BindingBuilder.bind(courierUserEventQueue())
                .to(userExchange())
                .with(EventsConstants.COURIER_REGISTERED.getRoutingKey());
    }

    // Bindings for Inventory Events
    
    // Bindings for Inventory Events to Analytics Queue
    @Bean
    public Binding bindProductCreatedToAnalyticsInventory() {
        return BindingBuilder.bind(analyticsInventoryEventQueue())
                .to(inventoryExchange())
                .with(EventsConstants.PRODUCT_CREATED.getRoutingKey());
    }

    @Bean
    public Binding bindInventoryUpdatedToAnalyticsInventory() {
        return BindingBuilder.bind(analyticsInventoryEventQueue())
                .to(inventoryExchange())
                .with(EventsConstants.INVENTORY_UPDATED.getRoutingKey());
    }

    // Bindings for Inventory Events to Warehouse Queue
    @Bean
    public Binding bindOrderStockReservationRequestedToWarehouseInventory() {
        return BindingBuilder.bind(warehouseInventoryEventQueue())
                .to(inventoryExchange())
                .with(EventsConstants.ORDER_STOCK_RESERVATION_REQUESTED.getRoutingKey());
    }

    // Bindings for Inventory Events to Order Queue
    @Bean
    public Binding bindWarehouseStockReservedToOrderInventory() {
        return BindingBuilder.bind(orderInventoryEventQueue())
                .to(inventoryExchange())
                .with(EventsConstants.WAREHOUSE_STOCK_RESERVED.getRoutingKey());
    }

    @Bean
    public Binding bindWarehouseOrderFulfillmentFailedToOrderInventory() {
        return BindingBuilder.bind(orderInventoryEventQueue())
                .to(inventoryExchange())
                .with(EventsConstants.WAREHOUSE_ORDER_FULFILLMENT_FAILED.getRoutingKey());
    }

    // Bindings for Order Events

    // Bindings for Order Events to Analytics Queue
    @Bean
    public Binding bindOrderEventsToAnalyticsExcludingPackaged() {
        return BindingBuilder.bind(analyticsOrderEventQueue())
                .to(orderExchange())
                .with(EventsConstants.ORDER_PLACED.getRoutingKey());
    }

    @Bean
    public Binding bindOrderCancelledToAnalyticsOrder() {
        return BindingBuilder.bind(analyticsOrderEventQueue())
                .to(orderExchange())
                .with(EventsConstants.ORDER_CANCELLED.getRoutingKey());
    }

    @Bean
    public Binding bindOrderAssignedToCourierToAnalyticsOrder() {
        return BindingBuilder.bind(analyticsOrderEventQueue())
                .to(orderExchange())
                .with(EventsConstants.ORDER_ASSIGNED_TO_COURIER.getRoutingKey());
    }

    @Bean
    public Binding bindOrderOutForDeliveryToAnalyticsOrder() {
        return BindingBuilder.bind(analyticsOrderEventQueue())
                .to(orderExchange())
                .with(EventsConstants.ORDER_OUT_FOR_DELIVERY.getRoutingKey());
    }

    @Bean
    public Binding bindOrderDeliveredToAnalyticsOrder() {
        return BindingBuilder.bind(analyticsOrderEventQueue())
                .to(orderExchange())
                .with(EventsConstants.ORDER_DELIVERED.getRoutingKey());
    }

    @Bean
    public Binding bindOrderDeliveryFailedToAnalyticsOrder() {
        return BindingBuilder.bind(analyticsOrderEventQueue())
                .to(orderExchange())
                .with(EventsConstants.ORDER_DELIVERY_FAILED.getRoutingKey());
    }

    // Bindings for Order Events to Order Queue
    @Bean
    public Binding bindCartCheckedoutToOrderOrder() {
        return BindingBuilder.bind(orderOrderEventQueue())
                .to(orderExchange())
                .with(EventsConstants.CART_CHECKEDOUT.getRoutingKey());
    }

    @Bean
    public Binding bindOrderPackagedToOrderOrder() {
        return BindingBuilder.bind(orderOrderEventQueue())
                .to(orderExchange())
                .with(EventsConstants.ORDER_PACKAGED.getRoutingKey());
    }

    @Bean
    public Binding bindOrderAssignedToCourierToOrderOrder() {
        return BindingBuilder.bind(orderOrderEventQueue())
                .to(orderExchange())
                .with(EventsConstants.ORDER_ASSIGNED_TO_COURIER.getRoutingKey());
    }

    @Bean
    public Binding bindOrderOutForDeliveryToOrderOrder() {
        return BindingBuilder.bind(orderOrderEventQueue())
                .to(orderExchange())
                .with(EventsConstants.ORDER_OUT_FOR_DELIVERY.getRoutingKey());
    }

    @Bean
    public Binding bindOrderDeliveredToOrderOrder() {
        return BindingBuilder.bind(orderOrderEventQueue())
                .to(orderExchange())
                .with(EventsConstants.ORDER_DELIVERED.getRoutingKey());
    }

    // Bindings for Order Events to Warehouse Queue
    @Bean
    public Binding bindOrderPlacedToWarehouseOrder() {
        return BindingBuilder.bind(warehouseOrderEventQueue())
                .to(orderExchange())
                .with(EventsConstants.ORDER_PLACED.getRoutingKey());
    }

    @Bean
    public Binding bindOrderDeliveryFailedToWarehouseOrder() {
        return BindingBuilder.bind(warehouseOrderEventQueue())
                .to(orderExchange())
                .with(EventsConstants.ORDER_DELIVERY_FAILED.getRoutingKey());
    }

    // Bindings for Order Events to Courier Queue
    @Bean
    public Binding bindOrderAssignedToCourierToCourierOrder() {
        return BindingBuilder.bind(courierOrderEventQueue())
                .to(orderExchange())
                .with(EventsConstants.ORDER_ASSIGNED_TO_COURIER.getRoutingKey());
    }

    @Bean
    public MessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory, MessageConverter messageConverter) {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(messageConverter);
        return rabbitTemplate;
    }

    @Bean
    public AmqpAdmin amqpAdmin(ConnectionFactory connectionFactory) {
        return new RabbitAdmin(connectionFactory);
    }
}