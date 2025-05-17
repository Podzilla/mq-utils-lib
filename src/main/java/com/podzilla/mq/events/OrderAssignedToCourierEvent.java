package com.podzilla.mq.events;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
public class OrderAssignedToCourierEvent extends BaseEvent {

    private String orderId;
    private String courierId;
    private BigDecimal totalAmount;
    private double orderLatitude;
    private double orderLongitude;
    private String signature;
    private ConfirmationType confirmationType;
}
