package com.abstract_factory;

import java.util.List;

public interface PaymentValidator {
    boolean validatePayment();

    boolean isPaymentValid();

    List<String> getValidationErrors();
}
