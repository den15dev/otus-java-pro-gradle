package ru.otus.java.pro.exceptions;

public class CannotWithdrawException extends RuntimeException {
    public CannotWithdrawException(int amount) {
        super("Невозможно выдать сумму: " + amount);
    }
}
