package ru.otus.java.pro;

public class TestLogging implements TestLoggingInterface {
    @Override
    @Log
    public void calculation(int param) {
    }

    @Override
    public void calculation(int param1, int param2) {
    }

    @Override
    @Log
    public void calculation(int param1, int param2, String param3) {
    }
}
