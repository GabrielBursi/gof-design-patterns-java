package com.abstract_factory;

public interface ReceiptExporter {
    void generatePDF();

    void sendByEmail(String email);
}
