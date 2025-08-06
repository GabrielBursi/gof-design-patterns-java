package com.adapter;

import com.fasterxml.jackson.databind.ObjectMapper;

public class CatalogGatewayHttp implements CatalogGateway {

    private final HttpClient httpClient;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public CatalogGatewayHttp(HttpClient httpClient) {
        this.httpClient = httpClient;
    }

    @Override
    public Product getProduct(Integer productId) {
        String response = httpClient.get("http://localhost:3001/products/" + productId);
        try {
            return objectMapper.readValue(response, Product.class);
        } catch (Exception e) {
            throw new RuntimeException("Failed to parse product: " + e.getMessage());
        }
    }
}
