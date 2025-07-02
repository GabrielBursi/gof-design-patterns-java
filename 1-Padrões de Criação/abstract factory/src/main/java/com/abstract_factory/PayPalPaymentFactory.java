package com.abstract_factory;

public class PayPalPaymentFactory implements PaymentFactory {
    @Override
    public PaymentProcessor createPaymentProcessor() {
        return new PayPalPayment();
    }

    @Override
    public PaymentValidator createPaymentValidator() {
        return new PayPalPayment();
    }

    @Override
    public PaymentInfo createPaymentInfo() {
        return new PayPalPayment();
    }
}
