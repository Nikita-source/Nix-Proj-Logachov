package ui;

import dblogic.controllers.JpaController;
import dblogic.services.JdbcService;
import dblogic.services.JpaService;
import models.*;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.EntityManager;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.time.Instant;
import java.util.List;

public class UiService {
    private final BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
    private static final Logger LOGGER = LoggerFactory.getLogger("UiServiceLOGS");

    public void uiMain() throws IOException {
        JpaController jpaController = new JpaController();
        new JpaService().start(jpaController);
        System.out.println("Initial data was created ");
        String str;
        Configuration configuration = new Configuration().configure();
        try (SessionFactory sessionFactory = configuration.buildSessionFactory()) {
            EntityManager entityManager = sessionFactory.createEntityManager();
            do {
                System.out.println("1 Create User - \"1\"");
                System.out.println("2 Create Account - \"2\"");
                System.out.println("3 Create Operation \"3\"");
                System.out.println("4 Create OperationCategory - \"4\"");
                System.out.println("5 Create extract from operations to CSV - \"5\"");
                System.out.println("0 Exit - \"0\"");
                str = reader.readLine();
                switch (str) {
                    case "1":
                        LOGGER.info("User create");
                        createUser(jpaController, entityManager);
                        break;
                    case "2":
                        LOGGER.info("Account create");
                        createAccount(jpaController, entityManager);
                        break;
                    case "3":
                        LOGGER.info("Operation create");
                        createOperation(jpaController, entityManager);
                        break;
                    case "4":
                        LOGGER.info("Operation Category create");
                        createOperationCategory(jpaController, entityManager);
                        break;
                    case "5":
                        LOGGER.info("Extract From Operations");
                        extractFromOperations(jpaController, entityManager);
                        break;
                }
            } while (!str.equals("0"));
        }
    }

    private void createUser(JpaController jpaController, EntityManager entityManager) throws IOException {
        System.out.println("Please enter user name");
        String name = reader.readLine();
        System.out.println("Please enter user email");
        String email = reader.readLine();
        System.out.println("Please enter user phone number");
        String phoneNumber = reader.readLine();
        jpaController.createUser(entityManager, name, email, phoneNumber);
    }

    private void createAccount(JpaController jpaController, EntityManager entityManager) throws IOException {
        System.out.println("Please enter account balance");
        Double balance = Double.valueOf(reader.readLine());
        System.out.println("Please enter user email for whom the account will be created ");
        String email = reader.readLine();
        jpaController.createAccount(entityManager, balance, email);
    }

    private void createOperation(JpaController jpaController, EntityManager entityManager) throws IOException {
        System.out.println("Please enter user email for whom the operation will be created ");
        String email = reader.readLine();
        User user = jpaController.getUserByEmail(entityManager, email);
        System.out.println("Select user account");
        for (int i = 0; i < user.getAccounts().size(); i++) {
            System.out.println(i + 1 + ") account ID = " + user.getAccounts().get(i).getId() + " balance = " + user.getAccounts().get(i).getBalance());
        }
        int index = Integer.parseInt(reader.readLine());
        Long accountId = user.getAccounts().get(index - 1).getId();

        double value;
        do {
            System.out.println("Please enter operation value");
            value = Double.parseDouble(reader.readLine());
            if (value == 0) {
                LOGGER.error("The value is  0");
                System.out.println("Value cannot be 0. Please try again");
            } else {
                break;
            }
        } while (true);

        List<OperationCategory> operationCategories;
        operationCategories = jpaController.getAllCategories(entityManager);
        do {
            System.out.println("Select category name");
            for (int i = 0; i < operationCategories.size(); i++) {
                System.out.println(i + 1 + ") category name = " + operationCategories.get(i).getName() + " category type = " + operationCategories.get(i).getCategory());
            }
            index = Integer.parseInt(reader.readLine());
            if (value > 0 && operationCategories.get(index - 1).getCategory() == Category.EXPENSE
                    || value < 0 && operationCategories.get(index - 1).getCategory() == Category.INCOME) {
                LOGGER.error("OperationCategory does not match the value ");
                System.out.println("You have selected the wrong category. Please try again");
            } else {
                break;
            }
        } while (true);
        String operationCategoryName = operationCategories.get(index - 1).getName();
        jpaController.createOperation(entityManager, operationCategoryName, value, accountId, Instant.now());
    }

    private void createOperationCategory(JpaController jpaController, EntityManager entityManager) throws IOException {
        System.out.println("Please enter OperationCategoryName");
        String name = reader.readLine();
        System.out.println("Select operation category");
        System.out.println("1 - INCOME");
        System.out.println("2 - EXPENSE");
        Category category = null;
        if (reader.readLine().equals("1")) {
            category = Category.INCOME;
        } else if (reader.readLine().equals("2")) {
            category = Category.EXPENSE;
        }
        jpaController.createOperationCategory(entityManager, name, category);
    }

    private void extractFromOperations(JpaController jpaController, EntityManager entityManager) throws IOException {
        List<User> users;
        users = jpaController.getAllUsers(entityManager);
        System.out.println("Select user");
        for (int i = 0; i < users.size(); i++) {
            System.out.println(i + 1 + ") User name = " + users.get(i).getName() + " User email = " + users.get(i).getEmail() + " User phone = " + users.get(i).getPhoneNumber());
        }
        int index = Integer.parseInt(reader.readLine());
        User user = users.get(index - 1);

        System.out.println("Select user account");
        for (int i = 0; i < user.getAccounts().size(); i++) {
            System.out.println(i + 1 + ") account ID = " + user.getAccounts().get(i).getId() + " balance = " + user.getAccounts().get(i).getBalance());
        }
        index = Integer.parseInt(reader.readLine());
        long accountId = user.getAccounts().get(index - 1).getId();

        System.out.println("Enter date from (example - 2021-07-29T14:00:00Z)");
        String dateFrom;
        try {
            dateFrom = reader.readLine();
            Instant instantFrom = Instant.parse(dateFrom);

            System.out.println("Enter date to (example - 2021-07-31T14:00:00Z)");
            String dateTo = reader.readLine();
            Instant instantTo = Instant.parse(dateTo);

            new JdbcService().writeToCsv(accountId, instantFrom, instantTo);
        } catch (IOException e) {
            LOGGER.error("Error with export " + e.getMessage());
            throw new RuntimeException("Error with export");
        }
    }
}