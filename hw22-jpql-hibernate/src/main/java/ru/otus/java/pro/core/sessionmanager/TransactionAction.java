package ru.otus.java.pro.core.sessionmanager;

import java.util.function.Function;
import org.hibernate.Session;

public interface TransactionAction<T> extends Function<Session, T> {}
