package ru.otus.java.pro.hw06.framework;

import ru.otus.java.pro.hw06.annotation.After;
import ru.otus.java.pro.hw06.annotation.Before;
import ru.otus.java.pro.hw06.annotation.Test;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

public final class TestRunner {
    private TestRunner() {
    }


    public static void run(String testClassName) {
        try {
            Class<?> testClass = Class.forName(testClassName);

            TestClassInfo testClassInfo = inspectTestClass(testClass);
            TestStatistics statistics = runTests(testClassInfo);

            printStatistics(statistics);
        } catch (ClassNotFoundException exception) {
            throw new IllegalArgumentException(
                "Класс с тестами не найден: " + testClassName,
                exception
            );
        }
    }


    private static TestClassInfo inspectTestClass(Class<?> testClass) {
        List<Method> methods = Arrays.stream(testClass.getDeclaredMethods())
                .sorted(Comparator.comparing(Method::getName))
                .toList();

        validateMethods(methods);

        List<Method> beforeMethods = findAnnotatedMethods(methods, Before.class);
        List<Method> testMethods = findAnnotatedMethods(methods, Test.class);
        List<Method> afterMethods = findAnnotatedMethods(methods, After.class);

        return new TestClassInfo(
            testClass,
            beforeMethods,
            testMethods,
            afterMethods
        );
    }


    private static List<Method> findAnnotatedMethods(
            List<Method> methods,
            Class<? extends Annotation> annotationClass
    ) {
        return methods.stream()
                .filter(method -> method.isAnnotationPresent(annotationClass))
                .toList();
    }


    private static void validateMethods(List<Method> methods) {
        for (Method method : methods) {
            if (!isFrameworkMethod(method)) {
                continue;
            }

            validateFrameworkMethod(method);
        }
    }


    private static boolean isFrameworkMethod(Method method) {
        return method.isAnnotationPresent(Before.class)
                || method.isAnnotationPresent(Test.class)
                || method.isAnnotationPresent(After.class);
    }


    private static void validateFrameworkMethod(Method method) {
        int annotationCount = 0;

        if (method.isAnnotationPresent(Before.class)) {
            annotationCount++;
        }

        if (method.isAnnotationPresent(Test.class)) {
            annotationCount++;
        }

        if (method.isAnnotationPresent(After.class)) {
            annotationCount++;
        }

        if (annotationCount > 1) {
            throw new IllegalArgumentException(
                "Метод не может одновременно иметь несколько тестовых аннотаций: "
                        + method.getName()
            );
        }

        if (Modifier.isStatic(method.getModifiers())) {
            throw new IllegalArgumentException(
                "Тестовый метод не должен быть статическим: " + method.getName()
            );
        }

        if (method.getParameterCount() != 0) {
            throw new IllegalArgumentException(
                "Тестовый метод не должен иметь параметры: " + method.getName()
            );
        }

        if (method.getReturnType() != void.class) {
            throw new IllegalArgumentException(
                "Тестовый метод должен возвращать void: " + method.getName()
            );
        }
    }


    private static TestStatistics runTests(TestClassInfo testClassInfo) {
        int successful = 0;
        int failed = 0;

        for (Method testMethod : testClassInfo.testMethods()) {
            TestExecutionResult result = runSingleTest(
                testClassInfo,
                testMethod
            );

            printTestResult(result);

            if (result.isSuccessful()) {
                successful++;
            } else {
                failed++;
            }
        }

        return new TestStatistics(
            testClassInfo.testMethods().size(),
            successful,
            failed
        );
    }


    private static TestExecutionResult runSingleTest(
        TestClassInfo testClassInfo,
        Method testMethod
    ) {
        List<Throwable> errors = new ArrayList<>();

        Object testInstance;

        try {
            testInstance = createTestInstance(testClassInfo.testClass());
        } catch (Throwable throwable) {
            errors.add(throwable);

            return new TestExecutionResult(
                testMethod.getName(),
                List.copyOf(errors)
            );
        }

        boolean beforeSuccessful = runBeforeMethods(
            testInstance,
            testClassInfo.beforeMethods(),
            errors
        );

        if (beforeSuccessful) {
            invokeAndCollectError(testInstance, testMethod, errors);
        }

        runAfterMethods(
            testInstance,
            testClassInfo.afterMethods(),
            errors
        );

        return new TestExecutionResult(
            testMethod.getName(),
            List.copyOf(errors)
        );
    }


    private static Object createTestInstance(Class<?> testClass)
            throws ReflectiveOperationException {
        Constructor<?> constructor = testClass.getDeclaredConstructor();
        constructor.setAccessible(true);

        return constructor.newInstance();
    }


    private static boolean runBeforeMethods(
        Object testInstance,
        List<Method> beforeMethods,
        List<Throwable> errors
    ) {
        for (Method beforeMethod : beforeMethods) {
            Throwable error = invokeMethod(testInstance, beforeMethod);

            if (error != null) {
                errors.add(error);
                return false;
            }
        }

        return true;
    }


    private static void runAfterMethods(
        Object testInstance,
        List<Method> afterMethods,
        List<Throwable> errors
    ) {
        for (Method afterMethod : afterMethods) {
            invokeAndCollectError(testInstance, afterMethod, errors);
        }
    }


    private static void invokeAndCollectError(
        Object testInstance,
        Method method,
        List<Throwable> errors
    ) {
        Throwable error = invokeMethod(testInstance, method);

        if (error != null) {
            errors.add(error);
        }
    }


    private static Throwable invokeMethod(
        Object testInstance,
        Method method
    ) {
        try {
            method.setAccessible(true);
            method.invoke(testInstance);

            return null;
        } catch (InvocationTargetException exception) {
            return exception.getCause();
        } catch (ReflectiveOperationException | RuntimeException exception) {
            return exception;
        }
    }


    private static void printTestResult(TestExecutionResult result) {
        if (result.isSuccessful()) {
            System.out.printf(
                "[SUCCESS] Тест %s успешно пройден%n%n",
                result.testName()
            );

            return;
        }

        System.out.printf(
            "[FAILED] Тест %s завершился с ошибкой%n",
            result.testName()
        );

        for (Throwable error : result.errors()) {
            System.out.printf(
                "  %s: %s%n",
                error.getClass().getSimpleName(),
                error.getMessage()
            );
        }

        System.out.println();
    }


    private static void printStatistics(TestStatistics statistics) {
        System.out.println("=".repeat(40));
        System.out.println("Результаты тестирования");
        System.out.println("=".repeat(40));
        System.out.println("Всего тестов: " + statistics.total());
        System.out.println("Успешно выполнено: " + statistics.successful());
        System.out.println("Завершилось ошибкой: " + statistics.failed());
    }
}