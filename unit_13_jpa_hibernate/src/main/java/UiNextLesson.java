import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class UiNextLesson {

    private BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

    public void nextLessonUi() {
        System.out.println("Enter id of student");
        new DbService().start(inputId());
    }

    private Long inputId() {
        try {
            return Long.valueOf(reader.readLine());
        } catch (IOException e) {
            throw new RuntimeException("Wrong input");
        }
    }
}
