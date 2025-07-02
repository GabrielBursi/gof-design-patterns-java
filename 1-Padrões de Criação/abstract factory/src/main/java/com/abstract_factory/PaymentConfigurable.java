package com.abstract_factory;

import java.util.Map;

public interface PaymentConfigurable {
    void setPaymentDetails(Map<String, Object> details);

    int getMaxInstallments();

    double getTransactionFee();
}
