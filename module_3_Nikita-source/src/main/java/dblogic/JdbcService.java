package dblogic;

import com.opencsv.CSVWriter;
import models.OperationInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.sql.*;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;

public class JdbcService {
    private static final Logger LOGGER_INFO = LoggerFactory.getLogger("info");
    private static final Logger LOGGER_ERROR = LoggerFactory.getLogger("error");

    public void writeToCsv(Long accountId, LocalDateTime fromTime, LocalDateTime toTime) {
        LOGGER_INFO.info("Exporting account data to the csv file, accountID:" + accountId);
        List<String[]> csvData = new ArrayList<>();
        if (!new File("operations.csv").exists()) {
            csvData.add(operationsHeaderCsv());
        }
        try (CSVWriter writer = new CSVWriter(new FileWriter("operations.csv", true))) {
            Connection connection = new DbConnection().getDbConnection();
            List<OperationInfo> operations = getOperations(accountId, fromTime, toTime, connection);
            for (OperationInfo op : operations) {
                csvData.add(operationToStringArray(op));
            }
            writer.writeAll(csvData);
            LOGGER_INFO.info("Data export completed, accountID:" + accountId);
        } catch (IOException ex) {
            LOGGER_ERROR.error("Can't write to csv" + ex.getMessage());
        } catch (SQLException ex) {
            LOGGER_ERROR.error("Can't connect to db" + ex.getMessage());
        }
    }

    private List<OperationInfo> getOperations(Long accountId, LocalDateTime fromTime, LocalDateTime toTime, Connection connection) {
        List<OperationInfo> operations = new ArrayList<>();

        Long userId = getUserIdByAccountId(connection, accountId);
        String userName = getUserNameById(connection, userId);

        try (PreparedStatement statement = connection.prepareStatement(
                "SELECT * FROM operations WHERE account_id = ? AND operation_time BETWEEN ? AND ?")) {
            statement.setLong(1, accountId);
            statement.setTimestamp(2, Timestamp.from(fromTime.toInstant(ZoneOffset.UTC)));
            statement.setTimestamp(3, Timestamp.from(toTime.toInstant(ZoneOffset.UTC)));
            ResultSet res = statement.executeQuery();

            while (res.next()) {
                OperationInfo operation = new OperationInfo();
                operation.setUser(userName);
                operation.setAccount(accountId);
                operation.setOperationId(res.getLong(1));
                operation.setMoney(res.getDouble(3));
                operation.setOperationTime(res.getTimestamp(2).toInstant());
                operations.add(operation);
            }
        } catch (SQLException ex) {
            LOGGER_ERROR.error("Operations read ERROR:" + ex.getMessage());
            throw new RuntimeException(ex);
        }
        return operations;
    }

    private String[] operationsHeaderCsv() {
        String[] header = new String[5];
        header[0] = "User";
        header[1] = "Account";
        header[2] = "Operation ID";
        header[3] = "Money";
        header[4] = "Operation Time";
        return header;
    }

    private Long getUserIdByAccountId(Connection connection, Long accountId) {
        try (PreparedStatement statement = connection.prepareStatement(
                "SELECT * FROM accounts WHERE id = ? ")) {
            statement.setLong(1, accountId);
            ResultSet res = statement.executeQuery();
            res.next();
            return res.getLong(3);
        } catch (SQLException ex) {
            LOGGER_ERROR.error("Account read ERROR:" + ex.getMessage());
            throw new RuntimeException(ex);
        }
    }

    private String getUserNameById(Connection connection, Long userId) {
        try (PreparedStatement statement = connection.prepareStatement(
                "SELECT * FROM users WHERE id = ? ")) {
            statement.setLong(1, userId);
            ResultSet res = statement.executeQuery();
            res.next();
            return res.getString(3);
        } catch (SQLException ex) {
            LOGGER_ERROR.error("User read ERROR:" + ex.getMessage());
            throw new RuntimeException(ex);
        }
    }

    private String[] operationToStringArray(OperationInfo operation) {
        String[] strings = new String[5];
        strings[0] = String.valueOf(operation.getUser());
        strings[1] = String.valueOf(operation.getAccount());
        strings[2] = String.valueOf(operation.getOperationId());
        strings[3] = String.valueOf(operation.getMoney());
        strings[4] = operation.getOperationTime().toString();
        return strings;
    }
}
