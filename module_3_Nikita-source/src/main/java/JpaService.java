import models.*;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import javax.persistence.EntityManager;

public class JpaService {
    public void start() {
        Configuration configuration = new Configuration().configure();
        try (SessionFactory sessionFactory = configuration.buildSessionFactory()) {
            EntityManager entityManager = sessionFactory.createEntityManager();
            createCategories(entityManager);
            createEntities(entityManager);
        }
    }

    private void createEntities(EntityManager entityManager) {
        try {
            entityManager.getTransaction().begin();

            User user = new User();
            user.setName("User_1");
            user.setEmail("User_1_mail");
            user.setPhoneNumber("User_1_phone");

            Account account = new Account();
            account.setBalance(1000.0);
            account.setUser(user);
            user.getAccounts().add(account);

            OperationCategory operationCategory = entityManager.find(OperationCategory.class, 1L);

            Operation operation = new Operation();
            operation.setOperationCategory(operationCategory);
            operation.setValue(100.0);
            account.addOperation(operation);

            entityManager.persist(user);
            entityManager.persist(account);
            entityManager.persist(operationCategory);
            entityManager.persist(operation);

            operationCategory = entityManager.find(OperationCategory.class, 1L);

            operation = new Operation();
            operation.setOperationCategory(operationCategory);
            operation.setValue(-200.0);
            account.addOperation(operation);

            entityManager.persist(account);
            entityManager.persist(operationCategory);
            entityManager.persist(operation);

            entityManager.getTransaction().commit();
        } catch (Exception e) {
            entityManager.getTransaction().rollback();
            throw new RuntimeException(e);
        }
    }

    private void createCategories(EntityManager entityManager) {
        try {
            entityManager.getTransaction().begin();

            OperationCategory operationCategory = new OperationCategory();
            operationCategory.setName("default transaction");
            entityManager.persist(operationCategory);

            operationCategory = new OperationCategory();
            operationCategory.setName("money transfer to...");
            operationCategory.setCategory(Category.EXPENSE);
            entityManager.persist(operationCategory);

            operationCategory = new OperationCategory();
            operationCategory.setName("money transfer from...");
            operationCategory.setCategory(Category.INCOME);
            entityManager.persist(operationCategory);

            operationCategory = new OperationCategory();
            operationCategory.setName("salary");
            operationCategory.setCategory(Category.INCOME);
            entityManager.persist(operationCategory);

            operationCategory = new OperationCategory();
            operationCategory.setName("stipend");
            operationCategory.setCategory(Category.INCOME);
            entityManager.persist(operationCategory);

            operationCategory = new OperationCategory();
            operationCategory.setName("grant");
            operationCategory.setCategory(Category.INCOME);
            entityManager.persist(operationCategory);

            operationCategory = new OperationCategory();
            operationCategory.setName("payment");
            operationCategory.setCategory(Category.EXPENSE);
            entityManager.persist(operationCategory);

            operationCategory = new OperationCategory();
            operationCategory.setName("refill");
            operationCategory.setCategory(Category.INCOME);
            entityManager.persist(operationCategory);

            operationCategory = new OperationCategory();
            operationCategory.setName("cash withdrawal");
            operationCategory.setCategory(Category.EXPENSE);
            entityManager.persist(operationCategory);

            entityManager.getTransaction().commit();
        } catch (Exception e) {
            entityManager.getTransaction().rollback();
            throw new RuntimeException(e);
        }
    }
}
