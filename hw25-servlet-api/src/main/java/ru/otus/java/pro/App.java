package ru.otus.java.pro;

import org.eclipse.jetty.ee10.servlet.ServletContextHandler;
import org.eclipse.jetty.ee10.servlet.ServletHolder;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
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
import ru.otus.java.pro.crm.servlet.AdminServlet;
import ru.otus.java.pro.crm.servlet.LoginServlet;

public class App {
    private static final int PORT = 8080;
    public static final String HIBERNATE_CFG_FILE = "hibernate.cfg.xml";

    public static void main(String[] args) throws Exception {
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

        Server server = new Server();
        ServerConnector connector = new ServerConnector(server);
        connector.setPort(PORT);
        server.addConnector(connector);

        ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
        context.setContextPath("/");

        context.addServlet(new ServletHolder(new LoginServlet(serviceClient)), "/");
        context.addServlet(new ServletHolder(new AdminServlet(serviceClient)), "/admin");

        server.setHandler(context);

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            try {
                server.stop();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }));

        server.start();
        System.out.printf("%nСервер Jetty запущен: http://localhost:%d/%n", PORT);
        System.out.println("Остановить: Ctrl+C");
        server.join();
    }
}
