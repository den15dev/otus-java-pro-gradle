package ru.otus.java.pro.calculation;

import ru.otus.java.pro.atm.CashCell;
import ru.otus.java.pro.banknotes.Denomination;
import ru.otus.java.pro.exceptions.CannotWithdrawException;

import java.util.Arrays;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.Map;

public class GreedyWithdrawalStrategy implements WithdrawalStrategy {
    @Override
    public Map<Denomination, Integer> calculate(
        int amount,
        Map<Denomination, CashCell> cells
    ) {
        Map<Denomination, Integer> result = new LinkedHashMap<>();

        int remaining = amount;

        var denominations = Arrays.stream(Denomination.values())
                .sorted(Comparator.comparingInt(Denomination::getValue).reversed())
                .toList();

        for (Denomination denomination : denominations) {
            CashCell cell = cells.get(denomination);

            int required = remaining / denomination.getValue();
            int available = cell.getCount();

            int count = Math.min(required, available);

            if (count > 0) {
                result.put(denomination, count);
                remaining -= denomination.getValue() * count;
            }
        }

        if (remaining != 0) {
            throw new CannotWithdrawException(amount);
        }

        return result;
    }
}
