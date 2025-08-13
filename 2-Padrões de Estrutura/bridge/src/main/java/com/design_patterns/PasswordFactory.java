package com.design_patterns;

import java.security.InvalidParameterException;

public class PasswordFactory {
    public static Password create(String password, PasswordType passwordType) {
        if (PasswordType.PLAINTEXT.equals(passwordType))
            return new PasswordPlainText(password);
        if (PasswordType.SHA1.equals(passwordType))
            return new PasswordSHA1(password);
        throw new InvalidParameterException("type not supported " + passwordType);
    }
}
