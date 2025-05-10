package com.podzilla.mq;

import org.springframework.amqp.core.*; // Contains Exchange, Queue, Binding, DirectExchange
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Spring configuration class for defining RabbitMQ topology (Exchanges, Queues,
 * Bindings).
 * This class should be part of your shared messaging library. When included in
 * a Spring Boot
 * application, it will automatically declare the defined RabbitMQ entities on
 * startup.
 *
 * IMPORTANT:
 * This programmatic topology definition is highly convenient for local
 * development,
 * integration testing, and quick demos. However, for production deployments,
 * it's
 * generally recommended to manage your RabbitMQ topology externally using:
 * - Infrastructure as Code (e.g., Terraform, Ansible)
 * - Kubernetes Operators (for RabbitMQ deployed in Kubernetes)
 * - RabbitMQ's management API or CLI tools
 * This ensures that infrastructure resources are consistent and managed
 * independently of application deployments.
 * You would typically remove or conditionally enable/disable this config for
 * production.
 */
@Configuration
public class RabbitMQConfig {

    // --- 1. Define Exchanges ---
    // Using DirectExchange as it's common for routing events by a specific key.
    @Bean
    public DirectExchange userExchange() {
        // Args: name, durable, autoDelete
        return new DirectExchange("user_exchange", true, false);
    }

    @Bean
    public DirectExchange inventoryExchange() {
        return new DirectExchange("inventory_exchange", true, false);
    }

    @Bean
    public DirectExchange orderExchange() {
        return new DirectExchange("order_exchange", true, false);
    }

    // --- 2. Define Queues ---
    // Queues are defined as durable (survive broker restart), not exclusive (can be
    // accessed by multiple connections),
    // and not auto-delete (don't delete when last consumer disconnects).
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

    // Queues for specific services that might subscribe to events
    @Bean
    public Queue orderOrderEventQueue() { // Example: for an 'Order' service's internal events
        return new Queue(EventsConstants.ORDER_ORDER_EVENT_QUEUE, true, false, false);
    }

    @Bean
    public Queue warehouseOrderEventQueue() { // Example: for a 'Warehouse' service
        return new Queue(EventsConstants.WAREHOUSE_ORDER_EVENT_QUEUE, true, false, false);
    }

    @Bean
    public Queue courierOrderEventQueue() { // Example: for a 'Courier' service
        return new Queue(EventsConstants.COURIER_ORDER_EVENT_QUEUE, true, false, false);
    }

    // --- 3. Define Bindings ---
    // Bindings connect a Queue to an Exchange using a Routing Key.
    // Based on common event-driven architecture patterns, events are routed to:
    // - An analytics queue (if applicable)
    // - The queue(s) of the service(s) that need to react to the event.
    // I've made assumptions based on typical microservice interactions.

    // User Exchange Bindings
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

    // Inventory Exchange Bindings
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

    // Order Exchange Bindings
    // Cart Checked Out (subscribed by Order Service, Analytics Service)
    @Bean
    public Binding bindCartCheckedoutToOrderOrder() {
        return BindingBuilder.bind(orderOrderEventQueue())
                .to(orderExchange())
                .with(EventsConstants.CART_CHECKEDOUT.getRoutingKey());
    }

    @Bean
    public Binding bindCartCheckedoutToAnalyticsOrder() {
        return BindingBuilder.bind(analyticsOrderEventQueue())
                .to(orderExchange())
                .with(EventsConstants.CART_CHECKEDOUT.getRoutingKey());
    }

    // Order Placed (subscribed by Order Service, Warehouse Service, Analytics
    // Service)
    @Bean
    public Binding bindOrderPlacedToOrderOrder() {
        return BindingBuilder.bind(orderOrderEventQueue())
                .to(orderExchange())
                .with(EventsConstants.ORDER_PLACED.getRoutingKey());
    }

    @Bean
    public Binding bindOrderPlacedToWarehouseOrder() {
        return BindingBuilder.bind(warehouseOrderEventQueue())
                .to(orderExchange())
                .with(EventsConstants.ORDER_PLACED.getRoutingKey());
    }

    @Bean
    public Binding bindOrderPlacedToAnalyticsOrder() {
        return BindingBuilder.bind(analyticsOrderEventQueue())
                .to(orderExchange())
                .with(EventsConstants.ORDER_PLACED.getRoutingKey());
    }

    // Order Cancelled (subscribed by Order Service, Analytics Service)
    @Bean
    public Binding bindOrderCancelledToOrderOrder() {
        return BindingBuilder.bind(orderOrderEventQueue())
                .to(orderExchange())
                .with(EventsConstants.ORDER_CANCELLED.getRoutingKey());
    }

    @Bean
    public Binding bindOrderCancelledToAnalyticsOrder() {
        return BindingBuilder.bind(analyticsOrderEventQueue())
                .to(orderExchange())
                .with(EventsConstants.ORDER_CANCELLED.getRoutingKey());
    }

    // Order Packaged (subscribed by Order Service, Warehouse Service, Courier
    // Service)
    @Bean
    public Binding bindOrderPackagedToOrderOrder() {
        return BindingBuilder.bind(orderOrderEventQueue())
                .to(orderExchange())
                .with(EventsConstants.ORDER_PACKAGED.getRoutingKey());
    }

    @Bean
    public Binding bindOrderPackagedToWarehouseOrder() {
        return BindingBuilder.bind(warehouseOrderEventQueue())
                .to(orderExchange())
                .with(EventsConstants.ORDER_PACKAGED.getRoutingKey());
    }

    @Bean
    public Binding bindOrderPackagedToCourierOrder() {
        return BindingBuilder.bind(courierOrderEventQueue())
                .to(orderExchange())
                .with(EventsConstants.ORDER_PACKAGED.getRoutingKey());
    }

    // Order Assigned To Courier (subscribed by Order Service, Warehouse Service,
    // Courier Service, Analytics)
    @Bean
    public Binding bindOrderAssignedToCourierToOrderOrder() {
        return BindingBuilder.bind(orderOrderEventQueue())
                .to(orderExchange())
                .with(EventsConstants.ORDER_ASSIGNED_TO_COURIER.getRoutingKey());
    }

    @Bean
    public Binding bindOrderAssignedToCourierToWarehouseOrder() {
        return BindingBuilder.bind(warehouseOrderEventQueue())
                .to(orderExchange())
                .with(EventsConstants.ORDER_ASSIGNED_TO_COURIER.getRoutingKey());
    }

    @Bean
    public Binding bindOrderAssignedToCourierToCourierOrder() {
        return BindingBuilder.bind(courierOrderEventQueue())
                .to(orderExchange())
                .with(EventsConstants.ORDER_ASSIGNED_TO_COURIER.getRoutingKey());
    }

    @Bean
    public Binding bindOrderAssignedToCourierToAnalyticsOrder() {
        return BindingBuilder.bind(analyticsOrderEventQueue())
                .to(orderExchange())
                .with(EventsConstants.ORDER_ASSIGNED_TO_COURIER.getRoutingKey());
    }

    // Order Shipped (subscribed by Order Service, Courier Service, Analytics
    // Service)
    @Bean
    public Binding bindOrderShippedToOrderOrder() {
        return BindingBuilder.bind(orderOrderEventQueue())
                .to(orderExchange())
                .with(EventsConstants.ORDER_SHIPPED.getRoutingKey());
    }

    @Bean
    public Binding bindOrderShippedToCourierOrder() {
        return BindingBuilder.bind(courierOrderEventQueue())
                .to(orderExchange())
                .with(EventsConstants.ORDER_SHIPPED.getRoutingKey());
    }

    @Bean
    public Binding bindOrderShippedToAnalyticsOrder() {
        return BindingBuilder.bind(analyticsOrderEventQueue())
                .to(orderExchange())
                .with(EventsConstants.ORDER_SHIPPED.getRoutingKey());
    }

    // Order Delivered (subscribed by Order Service, Courier Service, Analytics
    // Service)
    @Bean
    public Binding bindOrderDeliveredToOrderOrder() {
        return BindingBuilder.bind(orderOrderEventQueue())
                .to(orderExchange())
                .with(EventsConstants.ORDER_DELIVERED.getRoutingKey());
    }

    @Bean
    public Binding bindOrderDeliveredToCourierOrder() {
        return BindingBuilder.bind(courierOrderEventQueue())
                .to(orderExchange())
                .with(EventsConstants.ORDER_DELIVERED.getRoutingKey());
    }

    @Bean
    public Binding bindOrderDeliveredToAnalyticsOrder() {
        return BindingBuilder.bind(analyticsOrderEventQueue())
                .to(orderExchange())
                .with(EventsConstants.ORDER_DELIVERED.getRoutingKey());
    }

    // Order Failed (subscribed by Order Service, Courier Service, Analytics
    // Service)
    @Bean
    public Binding bindOrderFailedToOrderOrder() {
        return BindingBuilder.bind(orderOrderEventQueue())
                .to(orderExchange())
                .with(EventsConstants.ORDER_FAILED.getRoutingKey());
    }

    @Bean
    public Binding bindOrderFailedToCourierOrder() {
        return BindingBuilder.bind(courierOrderEventQueue())
                .to(orderExchange())
                .with(EventsConstants.ORDER_FAILED.getRoutingKey());
    }

    @Bean
    public Binding bindOrderFailedToAnalyticsOrder() {
        return BindingBuilder.bind(analyticsOrderEventQueue())
                .to(orderExchange())
                .with(EventsConstants.ORDER_FAILED.getRoutingKey());
    }

    // --- 4. Configure Message Converter ---
    /**
     * Defines the MessageConverter to be used by RabbitTemplate for
     * serialization/deserialization.
     * Jackson2JsonMessageConverter is standard for converting Java objects to JSON
     * and vice-versa.
     *
     * @return A Jackson2JsonMessageConverter instance.
     */
    @Bean
    public MessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    // --- 5. Configure RabbitTemplate ---
    /**
     * Configures the RabbitTemplate with the custom MessageConverter.
     * This RabbitTemplate bean is automatically injected into your EventPublisher.
     * Spring Boot automatically provides the ConnectionFactory.
     *
     * @param connectionFactory The RabbitMQ ConnectionFactory provided by Spring
     *                          Boot.
     * @param messageConverter  The Jackson2JsonMessageConverter bean defined above.
     * @return A configured RabbitTemplate.
     */
    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory, MessageConverter messageConverter) {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(messageConverter);
        // Optional: Configure other RabbitTemplate properties like publisher
        // confirms/returns
        // rabbitTemplate.setConfirmCallback(...);
        // rabbitTemplate.setReturnsCallback(...);
        return rabbitTemplate;
    }

    // --- 6. Enable Automatic Topology Declaration ---
    /**
     * Creates a RabbitAdmin bean. This bean is crucial for automatically
     * declaring exchanges, queues, and bindings that are defined as Spring @Bean
     * methods
     * in this configuration class. When your application starts, RabbitAdmin
     * ensures this topology exists in the RabbitMQ broker.
     *
     * @param connectionFactory The RabbitMQ ConnectionFactory.
     * @return A RabbitAdmin instance.
     */
    @Bean
    public AmqpAdmin amqpAdmin(ConnectionFactory connectionFactory) {
        return new RabbitAdmin(connectionFactory);
    }
}