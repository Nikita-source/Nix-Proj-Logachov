package dblogic.services;

import dblogic.controllers.JpaController;
import models.*;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.EntityManager;
import java.time.Instant;
import java.time.temporal.ChronoUnit;

public class JpaService {
    private static final Logger LOGGER = LoggerFactory.getLogger("JpaServiceLOGS");

    public void start(JpaController controller) {
        Configuration configuration = new Configuration().configure();
        try (SessionFactory sessionFactory = configuration.buildSessionFactory()) {
            EntityManager entityManager = sessionFactory.createEntityManager();
            LOGGER.info("Created initial data");
            createOperationCategories(entityManager, controller);
            createEntities(entityManager, controller);
            LOGGER.info("Initial data was created");
        }
    }

    private void createEntities(EntityManager entityManager, JpaController controller) {
        LOGGER.info("Create initial users");
        controller.createUser(entityManager, "User_1", "User_1_mail", "User_1_phone");
        User user = controller.getUserByEmail(entityManager, "User_1_mail");

        LOGGER.info("Create initial accounts");
        controller.createAccount(entityManager, 1000.0, "User_1_mail");
        controller.createAccount(entityManager, 500.0, "User_1_mail");

        LOGGER.info("Create initial operations");
        controller.createOperation(entityManager, "transfer TO your account", 100.0, user.getAccounts().get(0).getId(), Instant.now().minus(2, ChronoUnit.DAYS));
        controller.createOperation(entityManager, "transfer FROM your account", -200.0, user.getAccounts().get(0).getId(), Instant.now().minus(1, ChronoUnit.DAYS));
        controller.createOperation(entityManager, "salary", 300.0, user.getAccounts().get(0).getId(), Instant.now().minus(1, ChronoUnit.HOURS));
        controller.createOperation(entityManager, "cash withdrawal", -400.0, user.getAccounts().get(0).getId(), Instant.now());

        controller.createOperation(entityManager, "stipend", 100.0, user.getAccounts().get(1).getId(), Instant.now().minus(3, ChronoUnit.DAYS));
        controller.createOperation(entityManager, "grant", 200.0, user.getAccounts().get(1).getId(), Instant.now().minus(2, ChronoUnit.DAYS));
        controller.createOperation(entityManager, "payment", -500.0, user.getAccounts().get(1).getId(), Instant.now().minus(1, ChronoUnit.DAYS));
        controller.createOperation(entityManager, "refill", 400.0, user.getAccounts().get(1).getId(), Instant.now().minus(1, ChronoUnit.HOURS));
    }

    private void createOperationCategories(EntityManager entityManager, JpaController controller) {
        LOGGER.info("Create initial categories");
        controller.createOperationCategory(entityManager, "transfer TO your account", Category.INCOME);
        controller.createOperationCategory(entityManager, "transfer FROM your account", Category.EXPENSE);
        controller.createOperationCategory(entityManager, "salary", Category.INCOME);
        controller.createOperationCategory(entityManager, "stipend", Category.INCOME);
        controller.createOperationCategory(entityManager, "grant", Category.INCOME);
        controller.createOperationCategory(entityManager, "payment", Category.EXPENSE);
        controller.createOperationCategory(entityManager, "refill", Category.INCOME);
        controller.createOperationCategory(entityManager, "cash withdrawal", Category.EXPENSE);
    }
}
