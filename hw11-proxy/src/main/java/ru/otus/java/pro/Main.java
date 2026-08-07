package ru.otus.java.pro;

public class Main {
    public static void main(String[] args) {
        TestLoggingInterface testLogging = Ioc.createTestLogging();

        testLogging.calculation(6);
        testLogging.calculation(6, 7);
        testLogging.calculation(6, 7, "test");
    }
}