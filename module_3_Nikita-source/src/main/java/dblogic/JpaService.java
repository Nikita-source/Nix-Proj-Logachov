package dblogic;

import models.*;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import javax.persistence.EntityManager;
import java.time.Instant;
import java.time.temporal.ChronoUnit;

public class JpaService {
    public void start() {
        Configuration configuration = new Configuration().configure();
        try (SessionFactory sessionFactory = configuration.buildSessionFactory()) {
            EntityManager entityManager = sessionFactory.createEntityManager();
            createOperationCategories(entityManager);
            createEntities(entityManager);
        }
    }

    private void createEntities(EntityManager entityManager) {
        createUser(entityManager, "User_1", "User_1_mail", "User_1_phone");
        createAccount(entityManager, 1000.0, "User_1_mail");
        createOperation(entityManager, "transfer TO your account", 100.0, 1L, Instant.now().minus(2, ChronoUnit.DAYS));
        createOperation(entityManager, "transfer FROM your account", -200.0, 1L, Instant.now().minus(1, ChronoUnit.DAYS));
        createOperation(entityManager, "transfer TO your account", 300.0, 1L, Instant.now().minus(1, ChronoUnit.HOURS));
        createOperation(entityManager, "transfer FROM your account", -400.0, 1L, Instant.now());
    }

    private void createOperationCategories(EntityManager entityManager) {
        createOperationCategory(entityManager, "transfer TO your account", Category.INCOME);
        createOperationCategory(entityManager, "transfer FROM your account", Category.EXPENSE);
        createOperationCategory(entityManager, "salary", Category.INCOME);
        createOperationCategory(entityManager, "stipend", Category.INCOME);
        createOperationCategory(entityManager, "grant", Category.INCOME);
        createOperationCategory(entityManager, "payment", Category.EXPENSE);
        createOperationCategory(entityManager, "refill", Category.INCOME);
        createOperationCategory(entityManager, "cash withdrawal", Category.EXPENSE);
    }

    private void createUser(EntityManager entityManager, String name, String email, String phoneNumber) {
        try {
            entityManager.getTransaction().begin();

            User user = new User();
            user.setName(name);
            user.setEmail(email);
            user.setPhoneNumber(phoneNumber);
            entityManager.persist(user);

            entityManager.getTransaction().commit();
        } catch (Exception e) {
            entityManager.getTransaction().rollback();
            throw new RuntimeException(e);
        }
    }

    private void createAccount(EntityManager entityManager, Double balance, String userEmail) {
        try {
            entityManager.getTransaction().begin();

            Account account = new Account();
            account.setBalance(balance);
            User user = (User) entityManager.createQuery("SELECT u FROM User u where u.email = :userEmail")
                    .setParameter("userEmail", userEmail).getSingleResult();
            account.setUser(user);
            user.getAccounts().add(account);
            entityManager.persist(account);

            entityManager.getTransaction().commit();
        } catch (Exception e) {
            entityManager.getTransaction().rollback();
            throw new RuntimeException(e);
        }
    }

    private void createOperation(EntityManager entityManager, String operationCategoryName, Double value, Long accountId, Instant operationTime) {
        try {
            entityManager.getTransaction().begin();

            Operation operation = new Operation();
            OperationCategory operationCategory = (OperationCategory) entityManager.createQuery("SELECT o FROM OperationCategory o where o.name = :operationCategoryName")
                    .setParameter("operationCategoryName", operationCategoryName).getSingleResult();
            operation.setOperationCategory(operationCategory);
            operation.setValue(value);
            entityManager.find(Account.class, accountId).addOperation(operation);
            operation.setOperationTime(operationTime);
            entityManager.persist(operation);

            entityManager.getTransaction().commit();
        } catch (Exception e) {
            entityManager.getTransaction().rollback();
            throw new RuntimeException(e);
        }
    }

    private void createOperationCategory(EntityManager entityManager, String categoryName, Category category) {
        try {
            entityManager.getTransaction().begin();

            OperationCategory operationCategory = new OperationCategory();
            operationCategory.setName(categoryName);
            operationCategory.setCategory(category);
            entityManager.persist(operationCategory);

            entityManager.getTransaction().commit();
        } catch (Exception e) {
            entityManager.getTransaction().rollback();
            throw new RuntimeException(e);
        }
    }
}
