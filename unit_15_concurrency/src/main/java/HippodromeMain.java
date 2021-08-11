import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

public class HippodromeMain {
    private static final int horseCount = 10;

    public static void main(String[] args) {
        horseRace();
    }

    private static void horseRace() {
        ExecutorService executor = Executors.newFixedThreadPool(horseCount);

        List<Future<?>> futureList = new ArrayList<>();
        for (int i = 0; i < horseCount; i++) {
            futureList.add(executor.submit(new HorseThread(i + 1)));
        }

        for (Future<?> future : futureList) {
            try {
                future.get();
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
        }
        executor.shutdown();

        System.out.println(new HorseThread().getHorsesQueue());
    }

}
