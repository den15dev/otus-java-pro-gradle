package ru.otus.java.pro;

import java.math.BigDecimal;

public record ProductSummary(
    int count,
    BigDecimal totalPrice,
    int totalWeight
) {}
