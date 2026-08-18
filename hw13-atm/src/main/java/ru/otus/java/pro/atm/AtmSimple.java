package ru.otus.java.pro.atm;

import ru.otus.java.pro.banknotes.Banknote;
import ru.otus.java.pro.banknotes.Denomination;
import ru.otus.java.pro.calculation.WithdrawalStrategy;

import java.util.*;
import java.util.stream.Collectors;

public class AtmSimple implements Atm {
    private final Map<Denomination, CashCell> cells;
    private final WithdrawalStrategy withdrawalStrategy;

    public AtmSimple(WithdrawalStrategy withdrawalStrategy) {
        this.withdrawalStrategy = withdrawalStrategy;

        this.cells = Arrays.stream(Denomination.values())
                .collect(Collectors.toMap(
                        denomination -> denomination,
                        CashCell::new
                ));
    }

    @Override
    public void deposit(Collection<Banknote> banknotes) {
        for (Banknote banknote : banknotes) {
            cells.get(banknote.denomination()).put(banknote);
        }
    }

    @Override
    public int getBalance() {
        return cells.values().stream()
                .mapToInt(CashCell::getBalance)
                .sum();
    }

    @Override
    public List<Banknote> withdraw(int amount) {
        Map<Denomination, Integer> banknotesToTake =
                withdrawalStrategy.calculate(amount, cells);

        List<Banknote> result = new ArrayList<>();

        for (var entry : banknotesToTake.entrySet()) {
            CashCell cell = cells.get(entry.getKey());

            for (int i = 0; i < entry.getValue(); i++) {
                result.add(cell.take());
            }
        }

        return result;
    }
}
