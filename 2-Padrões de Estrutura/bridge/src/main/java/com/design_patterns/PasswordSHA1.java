package com.design_patterns;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PasswordSHA1 implements Password {
    public String value;

    public PasswordSHA1(String password) {
        value = hashSHA1(password);
    }

    @Override
    public boolean matches(String password) {
        return this.value.equals(password);
    }

    @Override
    public String getValue() {
        return value;
    }

    private static String hashSHA1(String input) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-1");
            byte[] hashedBytes = md.digest(input.getBytes(StandardCharsets.UTF_8));
            return bytesToHex(hashedBytes);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Algoritmo SHA-1 não encontrado", e);
        }
    }

    private static String bytesToHex(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (byte b : bytes) {
            sb.append(String.format("%02x", b));
        }
        return sb.toString();
    }

}
