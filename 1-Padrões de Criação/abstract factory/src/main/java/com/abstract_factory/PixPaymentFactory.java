package com.abstract_factory;

public class PixPaymentFactory implements PaymentFactory {
    @Override
    public PaymentProcessor createPaymentProcessor() {
        return new PixPayment();
    }

    @Override
    public PaymentValidator createPaymentValidator() {
        return new PixPayment();
    }

    @Override
    public PaymentInfo createPaymentInfo() {
        return new PixPayment();
    }
}
