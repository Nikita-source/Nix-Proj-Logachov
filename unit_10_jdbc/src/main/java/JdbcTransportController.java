import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

public class JdbcTransportController {


    public static void solutionOfTheTransportProblem() {
        Properties props = loadProperties();
        String url = props.getProperty("url");
        try (Connection connection = DriverManager.getConnection(url, props)) {
            connection.setAutoCommit(false);

            List<Integer> solutions = new ArrayList<>();
            TransportData transportData = getTransportDataFromDb(connection);

            Dijkstra dijkstra = new Dijkstra(transportData.getCitiesCount(), transportData.getRoutes());

            for (int i = 0; i < transportData.getProblems().size(); i++) {
                solutions.add(dijkstra.dijkstra(transportData.getProblems().get(i).get(1) - 1, transportData.getProblems().get(i).get(2) - 1));
                insertTransportDataFromDb(connection, transportData.getProblems().get(i).get(0), solutions.get(i));
            }

        } catch (SQLException e) {
            System.out.println("Connection ERROR");
            throw new RuntimeException(e);
        }
    }

    private static TransportData getTransportDataFromDb(Connection connection) throws SQLException {
        int citiesCount = 0;
        int[][] routes;
        List<List<Integer>> problems = new ArrayList<>();

        try (Statement getCount = connection.createStatement()) {
            ResultSet resultSet = getCount.executeQuery("SELECT COUNT(*) FROM  locations");
            if (resultSet.next()) {
                citiesCount = resultSet.getInt(1);
            }
        }
        routes = new int[citiesCount][citiesCount];
        for (int i = 0; i < citiesCount; i++) {
            for (int j = 0; j < citiesCount; j++) {
                if (i == j) {
                    routes[i][j] = 0;
                } else {
                    routes[i][j] = 300000;
                }
            }
        }

        try (Statement getProblems = connection.createStatement()) {
            ResultSet resultSet = getProblems.executeQuery("SELECT * FROM problems");
            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                int toId = resultSet.getInt("to_id");
                int fromId = resultSet.getInt("from_id");
                problems.add(Arrays.asList(id, toId, fromId));
            }
        }

        try (Statement getRoutes = connection.createStatement()) {
            ResultSet resultSet = getRoutes.executeQuery("SELECT * FROM routes");
            while (resultSet.next()) {
                routes[resultSet.getInt("from_id") - 1][resultSet.getInt("to_id") - 1] = resultSet.getInt("cost");
            }
        }
        return new TransportData(citiesCount, routes, problems);
    }

    private static void insertTransportDataFromDb(Connection connection, int problemId, int cost) throws SQLException {
        try (PreparedStatement insertProblemIdAndCost = connection.prepareStatement(
                "insert into solutions (problem_id, cost) values (?, ?) ON CONFLICT DO NOTHING")) {
            insertProblemIdAndCost.setInt(1, problemId);
            insertProblemIdAndCost.setInt(2, cost);
            insertProblemIdAndCost.executeUpdate();
            connection.commit();
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
