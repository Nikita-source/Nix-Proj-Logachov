package store;

import dataclasses.TransportData;

import java.sql.*;
import java.util.Arrays;


public class JdbcTransportAccess {

    TransportData transportData;

    public TransportData getTransportData(Connection connection) throws SQLException {
        getTransportDataFromDb(connection);
        return transportData;
    }

    public void insertSolutions(Connection connection, int problemId, int cost) throws SQLException {
        insertTransportDataToDb(connection, problemId, cost);
    }

    private void getTransportDataFromDb(Connection connection) throws SQLException {
        getCitiesCount(connection);
        readProblems(connection);
        readRoutes(connection);
    }

    private void getCitiesCount(Connection connection) throws SQLException {
        int count = 0;
        try (Statement getCount = connection.createStatement()) {
            ResultSet resultSet = getCount.executeQuery("SELECT COUNT(*) FROM  locations");
            if (resultSet.next()) {
                count = resultSet.getInt(1);
            }
        }
        transportData = new TransportData(count);
    }

    private void readProblems(Connection connection) throws SQLException {
        try (Statement getProblems = connection.createStatement()) {
            ResultSet resultSet = getProblems.executeQuery(
                    "SELECT id, from_id, to_id" +
                            " FROM problems" +
                            " left join solutions" +
                            " on problems.id=solutions.problem_id" +
                            " where solutions.problem_id is null");
            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                int toId = resultSet.getInt("to_id");
                int fromId = resultSet.getInt("from_id");
                transportData.getProblems().add(Arrays.asList(id, toId, fromId));
            }
        }
    }

    private void readRoutes(Connection connection) throws SQLException {
        try (Statement getRoutes = connection.createStatement()) {
            ResultSet resultSet = getRoutes.executeQuery("SELECT * FROM routes");
            while (resultSet.next()) {
                transportData.getRoutes()[resultSet.getInt("from_id") - 1][resultSet.getInt("to_id") - 1] = resultSet.getInt("cost");
            }
        }
    }

    private void insertTransportDataToDb(Connection connection, int problemId, int cost) throws SQLException {
        try (PreparedStatement insertProblemIdAndCost = connection.prepareStatement(
                "insert into solutions (problem_id, cost) values (?, ?) ON CONFLICT DO NOTHING")) {
            insertProblemIdAndCost.setInt(1, problemId);
            insertProblemIdAndCost.setInt(2, cost);
            insertProblemIdAndCost.executeUpdate();
            connection.commit();
        }
    }
}
