package org.example;

import lombok.Getter;
import org.example.model.Rental;
import org.example.model.User;
import org.example.model.Vehicle;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

public class HibernateConfig {
    @Getter
    private static final SessionFactory sessionFactory;

    static {
        try {
            Configuration configuration = new Configuration();

            configuration.setProperty("hibernate.connection.driver_class", "org.postgresql.Driver");
            configuration.setProperty("hibernate.connection.url",System.getenv("DB_URL"));

            configuration.setProperty("hibernate.dialect","org.hibernate.dialect.PostgreSQLDialect");
            configuration.setProperty("hibernate.show_sql","true");
            configuration.setProperty("hibernate.format_sql","true");
            configuration.setProperty("hibernate.hbm2ddl.auto","validate");

            configuration.addAnnotatedClass(User.class);
            configuration.addAnnotatedClass(Vehicle.class);
            configuration.addAnnotatedClass(Rental.class);

            sessionFactory = configuration.buildSessionFactory();
        } catch (Exception e) {
            throw new ExceptionInInitializerError("Initialize Hibernate ERROR: " + e);
        }
    }
    private HibernateConfig() {

    }
}
