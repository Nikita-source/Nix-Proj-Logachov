import com.opencsv.CSVWriter;
import models.Operation;
import models.OperationInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.sql.*;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class JdbcService {
    private static final Logger LOGGER_INFO = LoggerFactory.getLogger("info");
    private static final Logger LOGGER_WARN = LoggerFactory.getLogger("warn");
    private static final Logger LOGGER_ERROR = LoggerFactory.getLogger("error");

    public void writeToCsv(Long accountId, LocalDateTime fromTime, LocalDateTime toTime) {
        LOGGER_INFO.info("Start write operation to csv file, accountID:" + accountId);
        List<String[]> csvData = new ArrayList<>();
        if (!new File("operations.csv").exists()) {
            csvData.add(operationsHeaderCsv());
        }
        try (CSVWriter writer = new CSVWriter(new FileWriter("operations.csv", true))) {
            List<OperationInfo> operations = getOperations(accountId, fromTime, toTime);
            for (OperationInfo op : operations) {
                csvData.add(operationToStringArray(op));
            }
            writer.writeAll(csvData);
            LOGGER_INFO.info("End write operation to csv file, accountID:" + accountId);
        } catch (IOException ex) {
            LOGGER_ERROR.error("Can't write csv" + ex.getMessage());
        }
    }

    private List<OperationInfo> getOperations(Long accountId, LocalDateTime fromTime, LocalDateTime toTime) {
        List<OperationInfo> operations = new ArrayList<>();
        Properties props = loadProperties();
        try (Connection connection = DriverManager.getConnection(props.getProperty("url"), props)) {
            connection.setAutoCommit(false);
            Long userId = getUserIdByAccountId(connection, accountId);
            String userName = getUserNameById(connection, userId);

            try (PreparedStatement statement = connection.prepareStatement(
                    "SELECT * FROM operations WHERE account_id = ? AND timestamp BETWEEN ? AND ?")) {
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
            }
        } catch (SQLException ex) {
            LOGGER_ERROR.error("Can't read operations from database:" + ex.getMessage());
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

    private Long getUserIdByAccountId(Connection connection, Long accountId) throws SQLException {
        try (PreparedStatement statement = connection.prepareStatement(
                "SELECT * FROM accounts WHERE id = ? ")) {
            statement.setLong(1, accountId);
            ResultSet res = statement.executeQuery();
            return res.getLong(3);
        }
    }

    private String getUserNameById(Connection connection, Long userId) throws SQLException {
        try (PreparedStatement statement = connection.prepareStatement(
                "SELECT * FROM users WHERE id = ? ")) {
            statement.setLong(1, userId);
            ResultSet res = statement.executeQuery();
            return res.getString(3);
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

    private Properties loadProperties() {
        Properties props = new Properties();
        try (InputStream input = JpaService.class.getResourceAsStream("/jdbc.properties")) {
            props.load(input);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
        return props;
    }
}
