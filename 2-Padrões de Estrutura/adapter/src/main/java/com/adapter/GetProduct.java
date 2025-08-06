package com.adapter;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class GetProduct {
    final ProductRepository productRepository;

    Product execute (Integer productId) {
        return productRepository.getById(productId);
    }
}
