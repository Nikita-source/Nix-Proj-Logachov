import models.*;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import javax.persistence.EntityManager;

public class JpaService {
    public void start() {
        Configuration configuration = new Configuration().configure();
        try (SessionFactory sessionFactory = configuration.buildSessionFactory()) {
            EntityManager entityManager = sessionFactory.createEntityManager();
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

            OperationCategory operationCategory = new OperationCategory();
            operationCategory.setName("remittance");

            Operation operation = new Operation();
            operation.setOperationCategory(operationCategory);
            operation.setValue(100.0);
            account.addOperation(operation);

            entityManager.persist(user);
            entityManager.persist(account);
            entityManager.persist(operationCategory);
            entityManager.persist(operation);

            entityManager.getTransaction().commit();
        } catch (Exception e) {
            entityManager.getTransaction().rollback();
            throw new RuntimeException(e);
        }
    }
}
