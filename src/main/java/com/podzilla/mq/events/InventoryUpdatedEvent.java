package com.podzilla.mq.events;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
public class InventoryUpdatedEvent extends BaseEvent {

    private List<ProductSnapshot> productSnapshots;
}
