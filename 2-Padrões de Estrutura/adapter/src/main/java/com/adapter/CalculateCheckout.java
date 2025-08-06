package com.adapter;

import java.util.List;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class CalculateCheckout {
    final CatalogGateway catalogGateway;

    Double execute (List<Item> items) {
        var order = new Order();
		for (var item : items) {
			var product = this.catalogGateway.getProduct(item.productId);
            var productDTO = new ProductDTO(product.productId, product.description, product.price);
			order.addProduct(productDTO, item.quantity);
		}
		var total = order.getTotal();
		return total;
    }
}
