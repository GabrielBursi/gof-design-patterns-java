package com.abstract_factory;

public class Application {
    private PaymentProcessor processor;
    private PaymentInfo info;
    private PaymentValidator validator;

    public Application(PaymentFactory factory) {
        this.processor = factory.createPaymentProcessor();
        this.info = factory.createPaymentInfo();
        this.validator = factory.createPaymentValidator();
    }

    public void processPayment(double amount) {
        System.out.println("\n=== Processando Pagamento ===");
        System.out.println("Método: " + info.getPaymentMethod());

        processor.processPayment(amount);

        System.out.println("Status: " + processor.getStatus());
        System.out.println("ID: " + info.generateTransactionId());

        if (!validator.getValidationErrors().isEmpty()) {
            System.out.println("Erros: " + validator.getValidationErrors());
        }
    }

    public void showPaymentInfo() {
        System.out.println("\n=== Informações do Pagamento ===");
        System.out.println("Método: " + info.getPaymentMethod());
        System.out.println("Valor: R$ " + info.getAmount());
        System.out.println("Status: " + processor.getStatus());
        System.out.println("Data: " + info.getTransactionDate());

        if (processor instanceof CreditCardPayment) {
            CreditCardPayment cc = (CreditCardPayment) processor;
            System.out.println("Parcelas máximas: " + cc.getMaxInstallments());
            System.out.println("Taxa: R$ " + String.format("%.2f", cc.getTransactionFee()));
        }
    }

}
