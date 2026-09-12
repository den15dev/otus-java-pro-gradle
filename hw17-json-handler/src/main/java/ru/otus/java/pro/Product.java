package ru.otus.java.pro;

import java.math.BigDecimal;

public record Product(
    String name,
    BigDecimal price,
    int weight
) {
}