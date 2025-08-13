package com.design_patterns;

import lombok.Getter;

@Getter
public class Driver extends Account {

    private final String carPlate;

    public Driver(String name, String email, String document, String carPlate, String password, PasswordType passwordType) {
        super(name, email, document, password, passwordType);
        if (!carPlate.matches("[A-Z]{3}[0-9]{4}")) {
            throw new IllegalArgumentException("Invalid car plate");
        }
        this.carPlate = carPlate;
    }
    
    public Driver(String name, String email, String document, String carPlate, String password) {
        this(name, email, document, carPlate, password, PasswordType.PLAINTEXT);
    }
}
