package com.abstract_factory;

import java.util.regex.Pattern;

import lombok.EqualsAndHashCode;

@EqualsAndHashCode
public class CardNumber {
    private static final Pattern CARD_PATTERN = Pattern.compile("^\\d{16}$");
    private final String value;

    public CardNumber(String value) {
        if (value == null || !CARD_PATTERN.matcher(value).matches()) {
            throw new IllegalArgumentException("Número do cartão deve conter exatamente 16 dígitos");
        }
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public String getMasked() {
        return "**** **** **** " + value.substring(12);
    }

}
