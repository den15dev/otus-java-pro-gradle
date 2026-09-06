package ru.otus.java.pro;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.Type;
import java.math.RoundingMode;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class App {
    public static void main(String[] args) throws IOException {
        String dataPath = "hw17-json-handler/data";
        Path inputPath = Path.of(dataPath + "/products.json");
        Path outputPath = Path.of(dataPath + "/products-summary.txt");

        String json = Files.readString(inputPath);

        Gson gson = new Gson();

        Type productListType = new TypeToken<List<Product>>() {}.getType();
        List<Product> products = gson.fromJson(json, productListType);

        ProductAggregator aggregator = new ProductAggregator();
        ProductSummary summary = aggregator.getSummary(products);

        String result = """
            Всего товаров: %d
            Стоимость: %s руб.
            Общий вес: %d г.
            """.formatted(
            summary.count(),
            summary.totalPrice()
                    .setScale(2, RoundingMode.HALF_UP)
                    .toPlainString(),
            summary.totalWeight()
        );

        Files.writeString(outputPath, result);
    }
}