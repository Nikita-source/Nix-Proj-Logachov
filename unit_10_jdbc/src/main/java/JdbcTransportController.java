import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class JdbcTransportController {


    public static void getTransportDataFromDb() {
        Properties props = loadProperties();
        String url = props.getProperty("url");
        int citiesCount = 0;
        int[][] routes;
        List<Integer> problemsToId = new ArrayList<>();
        List<Integer> problemsFromId = new ArrayList<>();
        try (Connection connection = DriverManager.getConnection(url, props)) {
            connection.setAutoCommit(false);

            try (Statement getCount = connection.createStatement()) {
                ResultSet resultSet = getCount.executeQuery("SELECT COUNT(*) FROM  locations");
                if (resultSet.next()) {
                    citiesCount = resultSet.getInt(1);
                }
            }
            routes = new int[citiesCount][citiesCount];

            try (Statement getProblems = connection.createStatement()) {
                ResultSet resultSet = getProblems.executeQuery("SELECT * FROM problems");
                while (resultSet.next()) {
                    problemsToId.add(resultSet.getInt("to_id"));
                    problemsFromId.add(resultSet.getInt("from_id"));
                }
            }

            try (Statement getRoutes = connection.createStatement()) {
                ResultSet resultSet = getRoutes.executeQuery("SELECT * FROM routes");
                while (resultSet.next()) {
                    routes[resultSet.getInt("from_id") - 1][resultSet.getInt("to_id") - 1] = resultSet.getInt("cost");
                }
            }

        } catch (SQLException e) {
            System.out.println("Connection ERROR");
            throw new RuntimeException(e);
        }
    }

    private static Properties loadProperties() {
        Properties props = new Properties();
        try (InputStream input = JdbcTransportController.class.getResourceAsStream("/jdbc.properties")) {
            props.load(input);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
        return props;
    }
}
