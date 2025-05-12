package com.podzilla.mq.events;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
public class ProductCreatedEvent extends BaseEvent {

    private String productId;
    private String name;
    private String category;
    private BigDecimal cost;
    private Integer lowStockThreshold;
}
