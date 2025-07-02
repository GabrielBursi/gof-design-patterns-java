package com.abstract_factory;

public class Main {
    public static void main(String[] args) {
        var app = new Application(new CreditCardPaymentFactory());
        app.processPayment(10);
        app.showPaymentInfo();
    }
}