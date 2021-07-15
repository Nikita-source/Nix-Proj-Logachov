package dataclasses;

import java.util.ArrayList;
import java.util.List;

public class TransportData {

    private int citiesCount;
    private int[][] routes;
    private List<List<Integer>> problems;

    public int getCitiesCount() {
        return citiesCount;
    }

    public int[][] getRoutes() {
        return routes;
    }

    public List<List<Integer>> getProblems() {
        return problems;
    }

    public TransportData(int citiesCount) {
        this.citiesCount = citiesCount;
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
        problems = new ArrayList<>();
    }
}
