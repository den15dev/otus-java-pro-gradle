package ru.otus.java.pro;

import com.google.common.collect.ImmutableList;

import java.util.List;

public class HelloOtus {
    public List<String> copyToImmutable(List<String> strings) {
        return ImmutableList.copyOf(strings).reverse();
    }
}
