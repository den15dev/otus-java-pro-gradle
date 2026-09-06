package ru.otus.java.pro.atm;

import ru.otus.java.pro.banknotes.Banknote;

import java.util.Collection;
import java.util.List;

public interface Atm {
    void deposit(Collection<Banknote> banknotes);

    List<Banknote> withdraw(int amount);

    int getBalance();
}
