package dblogic;

import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class DbConnection {

    public Connection getDbConnection() throws SQLException {
        Properties props = loadProperties();
        Connection connection = DriverManager.getConnection(props.getProperty("url"), props);
        return connection;
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
