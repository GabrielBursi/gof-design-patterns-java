package com.abstract_factory;

import java.util.Date;

public interface PaymentInfo {
    double getAmount();

    String getPaymentMethod();

    Date getTransactionDate();

    String generateTransactionId();
}
