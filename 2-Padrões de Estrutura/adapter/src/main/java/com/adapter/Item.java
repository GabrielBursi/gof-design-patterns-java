package com.adapter;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class Item {
    final Integer productId;
    final Double price;
    final Integer quantity;

    public Double getTotal () {
        return this.price * this.quantity;
    }
}
