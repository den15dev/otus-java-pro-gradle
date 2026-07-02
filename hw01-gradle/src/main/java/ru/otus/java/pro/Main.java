package ru.otus.java.pro;

import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        List<String> strings = new ArrayList<>();
        strings.add("One");
        strings.add("Two");
        strings.add("Three");

        System.out.println(strings);

        HelloOtus helloOtus = new HelloOtus();
        List<String> immStrings = helloOtus.copyToImmutable(strings);

        System.out.println(immStrings);
    }
}