package ru.otus.java.pro.crm.seeder;

import org.hibernate.cfg.Configuration;
import ru.otus.java.pro.core.repository.DataTemplateHibernate;
import ru.otus.java.pro.core.repository.HibernateUtils;
import ru.otus.java.pro.core.sessionmanager.TransactionManagerHibernate;
import ru.otus.java.pro.crm.dbmigrations.MigrationsExecutorFlyway;
import ru.otus.java.pro.crm.model.Address;
import ru.otus.java.pro.crm.model.Client;
import ru.otus.java.pro.crm.model.Phone;
import ru.otus.java.pro.crm.service.DBServiceClient;
import ru.otus.java.pro.crm.service.DbServiceClientImpl;

import java.util.List;

public class DBSeeder {
    public static final String HIBERNATE_CFG_FILE = "hibernate.cfg.xml";

    public static void main(String[] args) {
        var configuration = new Configuration().configure(HIBERNATE_CFG_FILE);

        var dbUrl = configuration.getProperty("hibernate.connection.url");
        var dbUserName = configuration.getProperty("hibernate.connection.username");
        var dbPassword = configuration.getProperty("hibernate.connection.password");

        new MigrationsExecutorFlyway(dbUrl, dbUserName, dbPassword).executeMigrations();

        var sessionFactory = HibernateUtils.buildSessionFactory(
                configuration,
                Client.class,
                Address.class,
                Phone.class
        );

        var transactionManager = new TransactionManagerHibernate(sessionFactory);
        var clientTemplate = new DataTemplateHibernate<>(Client.class);
        DBServiceClient serviceClient = new DbServiceClientImpl(transactionManager, clientTemplate);

        serviceClient.saveClient(new Client(
                "john@mail.com",
                "secret1",
                "user",
                "John",
                new Address("ул. Широкая, 10"),
                List.of(
                        new Phone("+4 930 111-22-33"),
                        new Phone("+4 930 444-55-66")
                )
        ));

        serviceClient.saveClient(new Client(
                "roger@mail.com",
                "secret2",
                "user",
                "Roger",
                new Address("пр-кт Мира, 20"),
                List.of(
                        new Phone("+4 930 222-1-44"),
                        new Phone("+4 930 555-44-77")
                )
        ));

        serviceClient.saveClient(new Client(
                "denis@mail.ru",
                "secret3",
                "admin",
                "Denis",
                new Address("ул. Большая, 30"),
                List.of(
                        new Phone("+4 930 333-1-44"),
                        new Phone("+4 930 888-44-77")
                )
        ));
    }
}
