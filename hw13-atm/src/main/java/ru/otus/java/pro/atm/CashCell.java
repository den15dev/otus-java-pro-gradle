package ru.otus.java.pro.atm;

import ru.otus.java.pro.banknotes.Banknote;
import ru.otus.java.pro.banknotes.Denomination;

import java.util.ArrayList;
import java.util.List;

public class CashCell {
    private final Denomination denomination;
    private final List<Banknote> banknotes = new ArrayList<>();

    public CashCell(Denomination denomination) {
        this.denomination = denomination;
    }

    public void put(Banknote banknote) {
        if (banknote.denomination() != denomination) {
            throw new IllegalArgumentException(
                    "Недопустимый номинал банкноты: " + banknote.denomination()
            );
        }

        banknotes.add(banknote);
    }

    public Banknote take() {
        if (banknotes.isEmpty()) {
            throw new IllegalStateException("Ячейка пуста");
        }

        return banknotes.removeLast();
    }

    public int getCount() {
        return banknotes.size();
    }

    public int getBalance() {
        return denomination.getValue() * banknotes.size();
    }

    public Denomination getDenomination() {
        return denomination;
    }
}
