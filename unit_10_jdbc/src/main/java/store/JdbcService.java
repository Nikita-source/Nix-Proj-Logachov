package store;

import compute.Dijkstra;
import dataclasses.TransportData;

import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;


public class JdbcService {
    public void solutionOfTheTransportProblem() {
        Properties props = loadProperties();
        String url = props.getProperty("url");
        try (Connection connection = DriverManager.getConnection(url, props)) {
            connection.setAutoCommit(false);

            JdbcTransportAccess transportAccess = new JdbcTransportAccess();
            TransportData transportData = transportAccess.getTransportData(connection);
            List<Integer> solutions = new ArrayList<>();

            Dijkstra dijkstra = new Dijkstra(transportData.getCitiesCount(), transportData.getRoutes());

            for (int i = 0; i < transportData.getProblems().size(); i++) {
                solutions.add(dijkstra.dijkstra(transportData.getProblems().get(i).get(1) - 1, transportData.getProblems().get(i).get(2) - 1));
                transportAccess.insertSolutions(connection, transportData.getProblems().get(i).get(0), solutions.get(i));
            }

        } catch (SQLException e) {
            System.out.println("Connection ERROR");
            throw new RuntimeException(e);
        }
    }

    private static Properties loadProperties() {
        Properties props = new Properties();
        try (InputStream input = JdbcTransportAccess.class.getResourceAsStream("/jdbc.properties")) {
            props.load(input);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
        return props;
    }
}
