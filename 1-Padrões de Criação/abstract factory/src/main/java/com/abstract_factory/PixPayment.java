package com.abstract_factory;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PixPayment implements PaymentProcessor, PaymentValidator, PaymentInfo {

    private String pixKey;
    private double amount;
    private PaymentStatus status = PaymentStatus.PENDING;
    private String transactionId;
    private Date transactionDate;

    @Override
    public void processPayment(double amount) {
        this.amount = amount;
        this.transactionDate = new Date();
        this.transactionId = "PIX-" + UUID.randomUUID().toString().substring(0, 8);
        this.status = PaymentStatus.APPROVED;
        System.out.println("PIX realizado para chave: " + pixKey);
    }

    @Override
    public boolean validatePayment() {
        return pixKey != null && !pixKey.trim().isEmpty() && amount > 0;
    }

    @Override
    public PaymentStatus getStatus() {
        return status;
    }

    @Override
    public double getAmount() {
        return amount;
    }

    @Override
    public String getPaymentMethod() {
        return "PIX";
    }

    @Override
    public Date getTransactionDate() {
        return transactionDate;
    }

    @Override
    public String generateTransactionId() {
        return transactionId;
    }

    @Override
    public boolean isPaymentValid() {
        return validatePayment();
    }

    @Override
    public List<String> getValidationErrors() {
        List<String> errors = new ArrayList<>();
        if (pixKey == null || pixKey.isEmpty()) {
            errors.add("Chave PIX é obrigatória");
        }
        if (amount <= 0) {
            errors.add("Valor deve ser maior que zero");
        }
        return errors;
    }
}
