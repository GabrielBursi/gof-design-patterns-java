package com.design_patterns;

public class Main {
    public static void main(String[] args) {
        Driver driver = new Driver("Java java", "java@email.com", "12345678901", "BAC1234", "javascript");
        Passenger passenger = new Passenger(
                "typescript js",
                "ts@email.com",
                "12345678901",
                "1234",
                "1234",
                "12",
                "123",
                "123456");

        System.out.println(passenger.passwordMatches(passenger.getPassword().getValue()));
        System.out.println(driver.passwordMatches(driver.getPassword().getValue()));
    }
}