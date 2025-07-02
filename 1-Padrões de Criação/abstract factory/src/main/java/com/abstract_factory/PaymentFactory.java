package com.abstract_factory;

public interface PaymentFactory {
    PaymentProcessor createPaymentProcessor();

    PaymentValidator createPaymentValidator();

    PaymentInfo createPaymentInfo();
}
