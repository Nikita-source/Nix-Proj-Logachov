import serviceclasses.UiService;

import java.io.IOException;

public class IoNioMain {
    public static void main(String[] args) {
        try {
            ui.mainInterface();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
