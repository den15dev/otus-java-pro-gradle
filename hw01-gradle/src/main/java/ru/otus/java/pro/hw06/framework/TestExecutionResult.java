package ru.otus.java.pro.hw06.framework;

import java.util.List;

record TestExecutionResult(
    String testName,
    List<Throwable> errors
) {
    boolean isSuccessful() {
        return errors.isEmpty();
    }
}
