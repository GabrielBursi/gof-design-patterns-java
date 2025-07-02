package com.abstract_factory;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PayPalPayment implements PaymentProcessor, PaymentValidator,
        PaymentInfo, PaymentRefundable, PaymentConfigurable {

    private String email;
    private double amount;
    private PaymentStatus status = PaymentStatus.PENDING;
    private String transactionId;
    private Date transactionDate;
    private Map<String, Object> details = new HashMap<>();

    @Override
    public void processPayment(double amount) {
        this.amount = amount;
        this.transactionDate = new Date();
        this.transactionId = "PP-" + UUID.randomUUID().toString().substring(0, 8);
        this.status = PaymentStatus.PROCESSING;

        this.status = PaymentStatus.APPROVED;
        System.out.println("Pagamento PayPal realizado para: " + email);
    }

    @Override
    public boolean validatePayment() {
        return email != null && email.contains("@") && amount > 0;
    }

    @Override
    public int getMaxInstallments() {
        return 4;
    }

    @Override
    public double getTransactionFee() {
        return amount * 0.04;
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
        return "PayPal";
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
        if (email == null || !email.contains("@")) {
            errors.add("Email inválido");
        }
        if (amount <= 0) {
            errors.add("Valor deve ser maior que zero");
        }
        return errors;
    }

    @Override
    public void cancelPayment() {
        this.status = PaymentStatus.CANCELLED;
    }

    @Override
    public void refundPayment() {
        this.status = PaymentStatus.REFUNDED;
    }

    @Override
    public void setPaymentDetails(Map<String, Object> details) {
        this.details = details;
    }
}
