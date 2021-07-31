package dblogic.controllers;

import models.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.EntityManager;
import java.time.Instant;
import java.util.List;

public class JpaController {
    private static final Logger LOGGER = LoggerFactory.getLogger("JpaControllerLOGS");

    public void createUser(EntityManager entityManager, String name, String email, String phoneNumber) {
        LOGGER.info("Create - User:" + name + " " + email + " " + phoneNumber);
        try {
            entityManager.getTransaction().begin();

            User user = new User();
            user.setName(name);
            user.setEmail(email);
            user.setPhoneNumber(phoneNumber);
            entityManager.persist(user);

            entityManager.getTransaction().commit();
            LOGGER.info("Creation completed - User:" + name + " " + email + " " + phoneNumber);
        } catch (Exception e) {
            entityManager.getTransaction().rollback();
            LOGGER.error("User Create ERROR:" + e.getMessage());
            throw new RuntimeException(e);
        }
    }

    public void createAccount(EntityManager entityManager, Double balance, String userEmail) {
        LOGGER.info("Create - Account");
        try {
            entityManager.getTransaction().begin();

            Account account = new Account();
            account.setBalance(balance);
            User user = getUserByEmail(entityManager, userEmail);
            account.setUser(user);
            user.getAccounts().add(account);
            entityManager.persist(account);

            entityManager.getTransaction().commit();
            LOGGER.info("Creation completed - Account:" + user.getName() + " " + balance);
        } catch (Exception e) {
            entityManager.getTransaction().rollback();
            LOGGER.error("Account Create ERROR:" + e.getMessage());
            throw new RuntimeException(e);
        }
    }

    public void createOperation(EntityManager entityManager, String operationCategoryName, Double value, Long accountId, Instant operationTime) {
        LOGGER.info("Create - Operation:" + operationCategoryName + " " + value + " " + operationTime);
        try {
            entityManager.getTransaction().begin();

            Operation operation = new Operation();
            OperationCategory operationCategory = getCategoryByName(entityManager, operationCategoryName);
            operation.setOperationCategory(operationCategory);
            operation.setValue(value);
            entityManager.find(Account.class, accountId).addOperation(operation);
            operation.setOperationTime(operationTime);
            entityManager.persist(operation);

            entityManager.getTransaction().commit();
            LOGGER.info("Creation completed - Operation:" + operationCategoryName + " " + value + " " + operationTime);
        } catch (Exception e) {
            entityManager.getTransaction().rollback();
            LOGGER.error("Operation Create ERROR:" + e.getMessage());
            throw new RuntimeException(e);
        }
    }

    public void createOperationCategory(EntityManager entityManager, String categoryName, Category category) {
        LOGGER.info("Create - Category:" + categoryName + " " + category);
        try {
            entityManager.getTransaction().begin();

            OperationCategory operationCategory = new OperationCategory();
            operationCategory.setName(categoryName);
            operationCategory.setCategory(category);
            entityManager.persist(operationCategory);

            entityManager.getTransaction().commit();
            LOGGER.info("Creation completed - Category:" + categoryName + " " + category);
        } catch (Exception e) {
            entityManager.getTransaction().rollback();
            LOGGER.error("Category Create ERROR:" + e.getMessage());
            throw new RuntimeException(e);
        }
    }

    public User getUserByEmail(EntityManager entityManager, String userEmail) {
        return (User) entityManager.createQuery("SELECT u FROM User u where u.email = :userEmail")
                .setParameter("userEmail", userEmail).getSingleResult();
    }

    public OperationCategory getCategoryByName(EntityManager entityManager, String operationCategoryName) {
        return (OperationCategory) entityManager.createQuery("SELECT o FROM OperationCategory o where o.name = :operationCategoryName")
                .setParameter("operationCategoryName", operationCategoryName).getSingleResult();
    }

    public List<OperationCategory> getAllCategories(EntityManager entityManager) {
        return entityManager.createQuery("SELECT o FROM OperationCategory o", OperationCategory.class).getResultList();
    }

    public List<User> getAllUsers(EntityManager entityManager) {
        return entityManager.createQuery("SELECT o FROM User o", User.class).getResultList();
    }
}
