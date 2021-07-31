import ui.UiService;

import java.io.IOException;


public class FinancialManagementApp {
    public static void main(String[] args) {
        try {
            new UiService().uiMain();
        } catch (IOException e) {
            throw new RuntimeException("UI Error");
        }
    }
}
