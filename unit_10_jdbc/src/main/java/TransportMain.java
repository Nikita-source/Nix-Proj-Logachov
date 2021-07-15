import store.JdbcService;

public class TransportMain {
    public static void main(String[] args) {
        JdbcService jdbcService = new JdbcService();
        jdbcService.solutionOfTheTransportProblem();
    }
}
