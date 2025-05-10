package com.podzilla.mq.events;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
public class OrderPlacedEvent extends BaseEvent {

    private String orderId;
    private String customerId;
    private BigDecimal totalAmount;
    private List<OrderItem> items;
}