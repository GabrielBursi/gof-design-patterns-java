package com.adapter;

import java.util.List;

public class Main {
    public static void main(String[] args) {
        HttpClient httpClient = new HttpClientJavaAdapter();
        CatalogGateway catalogGateway = new CatalogGatewayHttp(httpClient);
        CalculateCheckout calculateCheckout = new CalculateCheckout(catalogGateway);

        List<Item> items = List.of(
            new Item(1, 20.50, 2),
            new Item(2, 9.99, 1)
        );

        Double total = calculateCheckout.execute(items);
        System.out.println("Total: " + total);
    }
}