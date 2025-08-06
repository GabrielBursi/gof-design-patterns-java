package com.adapter;

import java.util.List;

public class Order {
    List<Item> items;

	public Order () {
		items = List.of();
	}

    void addProduct (ProductDTO product, Integer quantity) {
		this.items.add(new Item(product.productId(), product.price(), quantity));
	}

	Double getTotal () {
		var total = 0.0;
        for (Item item : items) {
            total += item.getTotal();
        }
		return total;
	}
}
