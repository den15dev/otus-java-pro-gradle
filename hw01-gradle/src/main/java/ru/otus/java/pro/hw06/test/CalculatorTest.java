package ru.otus.java.pro.hw06.test;

import ru.otus.java.pro.hw06.annotation.After;
import ru.otus.java.pro.hw06.annotation.Before;
import ru.otus.java.pro.hw06.annotation.Test;

public class CalculatorTest {
    private int firstNumber;
    private int secondNumber;

    @Before
    void setUpNumbers() {
        System.out.println("Выполняется setUpNumbers()");
        firstNumber = 10;
        secondNumber = 5;
    }

    @Before
    void secondBeforeMethod() {
        System.out.println("Выполняется secondBeforeMethod()");
    }

    @Test
    void additionTest() {
        System.out.println("Выполняется additionTest()");

        int actual = firstNumber + secondNumber;
        int expected = 15;

        if (actual != expected) {
            throw new AssertionError(
                    "Ожидалось: " + expected + ", получено: " + actual
            );
        }
    }

    @Test
    void subtractionTest() {
        System.out.println("Выполняется subtractionTest()");

        int actual = firstNumber - secondNumber;
        int expected = 5;

        if (actual != expected) {
            throw new AssertionError(
                    "Ожидалось: " + expected + ", получено: " + actual
            );
        }
    }

    @Test
    void failedTest() {
        System.out.println("Выполняется failedTest()");

        int actual = firstNumber * secondNumber;
        int expected = 100;

        if (actual != expected) {
            throw new AssertionError(
                    "Ожидалось: " + expected + ", получено: " + actual
            );
        }
    }

    @After
    void cleanUp() {
        System.out.println("Выполняется cleanUp()");
        firstNumber = 0;
        secondNumber = 0;
    }
}