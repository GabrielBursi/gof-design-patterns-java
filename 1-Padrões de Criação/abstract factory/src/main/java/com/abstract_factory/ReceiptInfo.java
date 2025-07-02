package com.abstract_factory;

import java.util.Date;

public interface ReceiptInfo {
    String getReceiptId();

    String getFormattedReceipt();

    Date getIssuedDate();

    double getAmount();

    String getPaymentMethod();

    String getTransactionId();

    String getMerchantInfo();
}
