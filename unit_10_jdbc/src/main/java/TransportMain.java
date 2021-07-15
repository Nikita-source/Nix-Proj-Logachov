import store.JdbcService;
import store.JdbcTransportAccess;

public class TransportMain {
    public static void main(String[] args) {
        JdbcService jdbcService = new JdbcService();
        jdbcService.solutionOfTheTransportProblem();
    }
}
