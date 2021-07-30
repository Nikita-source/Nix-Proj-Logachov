import dblogic.JdbcService;
import dblogic.JpaService;

import java.time.LocalDateTime;

public class FinancialManagementApp {
    public static void main(String[] args) {
        new JpaService().start();
        new JdbcService().writeToCsv(1L, LocalDateTime.now().minusDays(1), LocalDateTime.now());
    }
}
