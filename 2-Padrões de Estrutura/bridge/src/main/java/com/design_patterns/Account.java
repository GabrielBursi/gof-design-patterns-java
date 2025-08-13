package com.design_patterns;

import lombok.Getter;

@Getter
public abstract class Account {

    private final String name;
    private final String email;
    private final String document;
    private final PasswordType passwordType;
    private final Password password;

    public Account(String name, String email, String document, String password, PasswordType passwordType) {
        if (!name.matches(".+ .+")) {
            throw new IllegalArgumentException("Invalid name");
        }
        if (!email.matches(".+@.+\\..+")) {
            throw new IllegalArgumentException("Invalid email");
        }
        if (document.length() != 11) {
            throw new IllegalArgumentException("Invalid document");
        }
        this.name = name;
        this.email = email;
        this.document = document;
        this.passwordType = passwordType;
        this.password = PasswordFactory.create(password, passwordType);
    }

    public boolean passwordMatches(String password) {
        return this.password.matches(password);
    }
}

