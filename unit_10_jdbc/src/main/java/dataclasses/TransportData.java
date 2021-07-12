package dataclasses;

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

    public TransportData(int citiesCount, int[][] routes, List<List<Integer>> problems) {
        this.citiesCount = citiesCount;
        this.routes = routes;
        this.problems = problems;
    }
}
