package com.design_patterns;

import lombok.Getter;

@Getter
public class Passenger extends Account {

    private final String cardHolder;
    private final String cardNumber;
    private final String expDate;
    private final String cvv;

    public Passenger(
        String name,
        String email,
        String document,
        String cardHolder,
        String cardNumber,
        String expDate,
        String cvv,
        String password,
        PasswordType passwordType
    ) {
        super(name, email, document, password, passwordType);
        if (cvv.length() != 3) {
            throw new IllegalArgumentException("Invalid cvv");
        }
        this.cardHolder = cardHolder;
        this.cardNumber = cardNumber;
        this.expDate = expDate;
        this.cvv = cvv;
    }

    public Passenger(
        String name,
        String email,
        String document,
        String cardHolder,
        String cardNumber,
        String expDate,
        String cvv,
        String password
    ) {
        this(name, email, document, cardHolder, cardNumber, expDate, cvv, password, PasswordType.SHA1);
    }
}
