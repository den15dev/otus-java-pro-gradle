package ru.otus.java.pro;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.HashSet;
import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Ioc {
    private static final Logger logger = LoggerFactory.getLogger(Ioc.class);

    private Ioc() {
    }

    public static TestLoggingInterface createTestLogging() {
        TestLoggingInterface testLogging = new TestLogging();

        InvocationHandler handler = new LoggingInvocationHandler(testLogging);

        return (TestLoggingInterface) Proxy.newProxyInstance(
                Ioc.class.getClassLoader(),
                new Class<?>[]{TestLoggingInterface.class},
                handler
        );
    }

    static class LoggingInvocationHandler implements InvocationHandler {
        private final TestLoggingInterface testLogging;
        private final Set<Method> methodsForLogging = new HashSet<>();

        LoggingInvocationHandler(TestLoggingInterface testLogging) {
            this.testLogging = testLogging;

            for (Method interfaceMethod : TestLoggingInterface.class.getMethods()) {
                try {
                    Method implementationMethod = testLogging.getClass().getMethod(
                        interfaceMethod.getName(),
                        interfaceMethod.getParameterTypes()
                    );

                    if (implementationMethod.isAnnotationPresent(Log.class)) {
                        methodsForLogging.add(interfaceMethod);
                    }

                } catch (NoSuchMethodException e) {
                    throw new RuntimeException(e);
                }
            }
        }

        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            if (methodsForLogging.contains(method)) {
                logMethodCall(method, args);
            }

            return method.invoke(testLogging, args);
        }

        private void logMethodCall(Method method, Object[] args) {
            StringBuilder log = new StringBuilder("executed method: ")
                    .append(method.getName());

            for (int i = 0; i < args.length; i++) {
                log.append(", param");

                if (args.length > 1) {
                    log.append(i + 1);
                }

                log.append(": ").append(args[i]);
            }

            logger.info(log.toString());
        }
    }
}
