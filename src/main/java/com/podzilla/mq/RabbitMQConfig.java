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

    @Bean
    public Queue analyticsUserEventQueue() {
        return new Queue("analytics_user_event_queue", true, false, false);
    }

    @Bean
    public Queue analyticsOrderEventQueue() {
        return new Queue("analytics_order_event_queue", true, false, false);
    }

    @Bean
    public Queue analyticsInventoryEventQueue() {
        return new Queue("analytics_inventory_event_queue", true, false, false);
    }

    @Bean
    public Queue orderOrderEventQueue() {
        return new Queue("order_order_event_queue", true, false, false);
    }

    @Bean
    public Queue warehouseOrderEventQueue() {
        return new Queue("warehouse_order_event_queue", true, false, false);
    }

    @Bean
    public Queue courierOrderEventQueue() {
        return new Queue("courier_order_event_queue", true, false, false);
    }

    @Bean
    public Binding bindCourierRegisteredToAnalyticsUser() {
        return BindingBuilder.bind(analyticsUserEventQueue())
                .to(userExchange())
                .with("courier.registered");
    }

    @Bean
    public Binding bindCustomerRegisteredToAnalyticsUser() {
        return BindingBuilder.bind(analyticsUserEventQueue())
                .to(userExchange())
                .with("customer.registered");
    }

    @Bean
    public Binding bindProductCreatedToAnalyticsInventory() {
        return BindingBuilder.bind(analyticsInventoryEventQueue())
                .to(inventoryExchange())
                .with("product.created");
    }

    @Bean
    public Binding bindInventoryUpdatedToAnalyticsInventory() {
        return BindingBuilder.bind(analyticsInventoryEventQueue())
                .to(inventoryExchange())
                .with("inventory.updated");
    }

    @Bean
    public Binding bindAnalyticsOrderEvents() {
        return BindingBuilder.bind(analyticsOrderEventQueue())
                .to(orderExchange())
                .with("cart.checkedout");
    }

    @Bean
    public Binding bindAllOrderEventsToAnalytics() {
        return BindingBuilder.bind(analyticsOrderEventQueue())
                .to(orderExchange())
                .with("order.*");
    }

    @Bean
    public Binding bindCartCheckedoutToOrderOrder() {
        return BindingBuilder.bind(orderOrderEventQueue())
                .to(orderExchange())
                .with("cart.checkedout");
    }

    @Bean
    public Binding bindOrderPackagedToOrderOrder() {
        return BindingBuilder.bind(orderOrderEventQueue())
                .to(orderExchange())
                .with("order.packaged");
    }

    @Bean
    public Binding bindOrderAssignedToCourierToOrderOrder() {
        return BindingBuilder.bind(orderOrderEventQueue())
                .to(orderExchange())
                .with("order.assigned_to_courier");
    }

    @Bean
    public Binding bindOrderShippedToOrderOrder() {
        return BindingBuilder.bind(orderOrderEventQueue())
                .to(orderExchange())
                .with("order.shipped");
    }

    @Bean
    public Binding bindOrderDeliveredToOrderOrder() {
        return BindingBuilder.bind(orderOrderEventQueue())
                .to(orderExchange())
                .with("order.delivered");
    }

    @Bean
    public Binding bindOrderFailedToOrderOrder() {
        return BindingBuilder.bind(orderOrderEventQueue())
                .to(orderExchange())
                .with("order.failed");
    }

    @Bean
    public Binding bindOrderPlacedToWarehouseOrder() {
        return BindingBuilder.bind(warehouseOrderEventQueue())
                .to(orderExchange())
                .with("order.placed");
    }

    @Bean
    public Binding bindOrderCancelledToWarehouseOrder() {
        return BindingBuilder.bind(warehouseOrderEventQueue())
                .to(orderExchange())
                .with("order.cancelled");
    }

    @Bean
    public Binding bindOrderAssignedToCourierToWarehouseOrder() {
        return BindingBuilder.bind(warehouseOrderEventQueue())
                .to(orderExchange())
                .with("order.assigned_to_courier");
    }

    @Bean
    public Binding bindOrderPackagedToCourierOrder() {
        return BindingBuilder.bind(courierOrderEventQueue())
                .to(orderExchange())
                .with("order.packaged");
    }

    @Bean
    public Binding bindOrderAssignedToCourierToCourierOrder() {
        return BindingBuilder.bind(courierOrderEventQueue())
                .to(orderExchange())
                .with("order.assigned_to_courier");
    }

    @Bean
    public Binding bindOrderShippedToCourierOrder() {
        return BindingBuilder.bind(courierOrderEventQueue())
                .to(orderExchange())
                .with("order.shipped");
    }

    @Bean
    public Binding bindOrderDeliveredToCourierOrder() {
        return BindingBuilder.bind(courierOrderEventQueue())
                .to(orderExchange())
                .with("order.delivered");
    }

    @Bean
    public Binding bindOrderFailedToCourierOrder() {
        return BindingBuilder.bind(courierOrderEventQueue())
                .to(orderExchange())
                .with("order.failed");
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