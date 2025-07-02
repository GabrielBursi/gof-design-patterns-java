package com.abstract_factory;
public interface PaymentProcessor {
    void processPayment(double amount);

    PaymentStatus getStatus();
}
