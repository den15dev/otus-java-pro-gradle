package ru.otus.java.pro;

import ru.otus.java.pro.atm.Atm;
import ru.otus.java.pro.atm.AtmSimple;
import ru.otus.java.pro.banknotes.Banknote;
import ru.otus.java.pro.banknotes.Denomination;
import ru.otus.java.pro.calculation.GreedyWithdrawalStrategy;
import ru.otus.java.pro.calculation.WithdrawalStrategy;

import java.util.List;

public class Main {
    public static void main(String[] args) {
        WithdrawalStrategy strategy = new GreedyWithdrawalStrategy();
        Atm atm = new AtmSimple(strategy);

        atm.deposit(List.of(
                new Banknote(Denomination.RUB_5000),
                new Banknote(Denomination.RUB_1000),
                new Banknote(Denomination.RUB_1000),
                new Banknote(Denomination.RUB_500)
        ));

        System.out.println("В банкомате сейчас: " + atm.getBalance());

        int requested = 6500;
        System.out.println("Запрошено: " + requested);
        List<Banknote> money = atm.withdraw(requested);

        System.out.println("Выдача:");
        for (Banknote b : money) {
            System.out.println(b.denomination().getValue());
        }
    }
}