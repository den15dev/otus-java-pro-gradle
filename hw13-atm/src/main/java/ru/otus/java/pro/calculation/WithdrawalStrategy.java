package ru.otus.java.pro.calculation;

import ru.otus.java.pro.atm.CashCell;
import ru.otus.java.pro.banknotes.Denomination;

import java.util.Map;

public interface WithdrawalStrategy {
    Map<Denomination, Integer> calculate(
        int amount,
        Map<Denomination, CashCell> cells
    );
}
