package ru.otus.java.pro.hw06;

import ru.otus.java.pro.hw06.framework.TestRunner;
import ru.otus.java.pro.hw06.test.CalculatorTest;

public class App {
    public static void main(String[] args) {
        TestRunner.run(CalculatorTest.class.getName());
    }
}
