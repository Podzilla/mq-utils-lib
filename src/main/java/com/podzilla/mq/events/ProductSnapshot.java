package com.podzilla.mq.events;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductSnapshot implements Serializable {

    private static final long serialVersionUID = 1L;

    private String productId;
    private Integer newQuantity;
}
