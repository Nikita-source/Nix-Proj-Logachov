package dblogic.connection;

import dblogic.services.JpaService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class DbConnection {
    private static final Logger LOGGER = LoggerFactory.getLogger("DbConnectionLOGS");

    public Connection getDbConnection() {
        LOGGER.info("Creation to DB");
        Properties props = loadProperties();
        Connection connection;
        try {
            connection = DriverManager.getConnection(props.getProperty("url"), props);
        } catch (SQLException e) {
            LOGGER.error("Db Connection ERROR:" + e.getMessage());
            throw new RuntimeException("Db Connection Error");
        }
        return connection;
    }

    private Properties loadProperties() {
        Properties props = new Properties();
        LOGGER.info("Read Properties");
        try (InputStream input = JpaService.class.getResourceAsStream("/jdbc.properties")) {
            props.load(input);
        } catch (IOException e) {
            LOGGER.error("Read Properties ERROR:" + e.getMessage());
            throw new UncheckedIOException(e);
        }
        return props;
    }
}
