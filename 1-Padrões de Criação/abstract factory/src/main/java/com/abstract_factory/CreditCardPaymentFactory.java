package com.abstract_factory;

public class CreditCardPaymentFactory implements PaymentFactory {
    @Override
    public PaymentProcessor createPaymentProcessor() {
        return new CreditCardPayment("4111111111111111", "João Silva", "12/30", "123", 500.0);
    }

    @Override
    public PaymentValidator createPaymentValidator() {
        return new CreditCardPayment("4111111111111111", "João Silva", "12/30", "123", 500.0);
    }

    @Override
    public PaymentInfo createPaymentInfo() {
        return new CreditCardPayment("4111111111111111", "João Silva", "12/30", "123", 500.0);
    }
}
