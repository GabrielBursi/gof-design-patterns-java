package com.adapter;

import java.util.ArrayList;


public class ProductRepositoryInMemory implements ProductRepository {
    private ArrayList<Product> products;

    public ProductRepositoryInMemory() {
        products.add(new Product(1, "Produto 1", 20.50));
        products.add(new Product(2, "Produto 2", 9.99));
        products.add(new Product(3, "Produto 3", 100.00));
    }

    @Override
    public Product getById(Integer productId) {
        var product = this.products.stream().filter(p -> p.productId.equals(productId)).findFirst();
		if (!product.isPresent()) throw new Error("Product not found");
		return product.get();
    }

}
