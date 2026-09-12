package ru.otus.java.pro;

import java.math.BigDecimal;
import java.util.List;

public class ProductAggregator {
    public ProductSummary getSummary(List<Product> products) {
        BigDecimal totalPrice = BigDecimal.ZERO;
        int totalWeight = 0;

        for (Product product : products) {
            totalPrice = totalPrice.add(product.price());
            totalWeight += product.weight();
        }

        return new ProductSummary(
            products.size(),
            totalPrice,
            totalWeight
        );
    }
}
