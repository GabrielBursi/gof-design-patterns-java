package com.design_patterns;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class PasswordPlainText implements Password {
    public String value;

    @Override
    public boolean matches(String password) {
        return value.equals(password);
    }

    @Override
    public String getValue() {
        return value;
    }

}
