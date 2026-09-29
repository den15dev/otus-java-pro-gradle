package ru.otus.java.pro.demo;

import org.hibernate.cfg.Configuration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.java.pro.core.repository.DataTemplateHibernate;
import ru.otus.java.pro.core.repository.HibernateUtils;
import ru.otus.java.pro.core.sessionmanager.TransactionManagerHibernate;
import ru.otus.java.pro.crm.dbmigrations.MigrationsExecutorFlyway;
import ru.otus.java.pro.crm.model.Address;
import ru.otus.java.pro.crm.model.Client;
import ru.otus.java.pro.crm.model.Phone;
import ru.otus.java.pro.crm.service.DbServiceClientImpl;

import java.util.List;

public class DbServiceDemo {

    private static final Logger log = LoggerFactory.getLogger(DbServiceDemo.class);

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
        ///
        var clientTemplate = new DataTemplateHibernate<>(Client.class);
        ///
        var dbServiceClient = new DbServiceClientImpl(transactionManager, clientTemplate);

        var client1 = new Client(
            "dbServiceFirst",
            new Address("ул. Ленина, 10"),
            List.of(
                new Phone("+7 999 111-22-33"),
                new Phone("+7 999 444-55-66")
            )
        );
        var savedClient1 = dbServiceClient.saveClient(client1);

        var client2 = new Client(
            "dbServiceSecond",
            new Address("пр-кт Мира, 20"),
            List.of(
                new Phone("+7 988 222-1-44"),
                new Phone("+7 988 555-44-77")
            )
        );
        var savedClient2 = dbServiceClient.saveClient(client2);

        var client2Selected = dbServiceClient
                .getClient(savedClient2.getId())
                .orElseThrow(
                    () -> new RuntimeException("Client not found, id:" + savedClient2.getId())
                );
        log.info("client2Selected:{}", client2Selected);

        ///
        dbServiceClient.saveClient(new Client(client2Selected.getId(), "dbServiceSecondUpdated"));
        var clientUpdated = dbServiceClient
                .getClient(client2Selected.getId())
                .orElseThrow(
                    () -> new RuntimeException("Client not found, id:" + client2Selected.getId())
                );
        log.info("clientUpdated:{}", clientUpdated);

        log.info("All clients");
        dbServiceClient.findAll().forEach(client -> log.info("client:{}", client));
    }
}
