package ru.otus.java.pro.hw06.framework;

import java.lang.reflect.Method;
import java.util.List;

record TestClassInfo(
    Class<?> testClass,
    List<Method> beforeMethods,
    List<Method> testMethods,
    List<Method> afterMethods
) {
}
