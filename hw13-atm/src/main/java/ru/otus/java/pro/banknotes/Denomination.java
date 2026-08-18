package ru.otus.java.pro.banknotes;

public enum Denomination {
    RUB_100(100),
    RUB_200(200),
    RUB_500(500),
    RUB_1000(1000),
    RUB_2000(2000),
    RUB_5000(5000);

    private final int value;

    Denomination(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
