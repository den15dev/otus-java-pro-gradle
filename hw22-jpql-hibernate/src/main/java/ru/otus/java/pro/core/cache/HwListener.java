package ru.otus.java.pro.core.cache;

public interface HwListener<K, V> {
    void notify(K key, V value, String action);
}
