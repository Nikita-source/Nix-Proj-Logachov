import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.*;

public class HippodromeMain {
    private static final int horseCount = 10;

    public static void main(String[] args) {
        inputHorse();
    }

    private static Queue<String> horseRace() {
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

        return new HorseThread().getHorsesQueue();
    }

    private static void inputHorse() {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

        System.out.println("Choose your horse");
        int horseNumber;
        try {
            horseNumber = Integer.parseInt(reader.readLine());
            if (horseNumber > horseCount || horseNumber < 1) {
                throw new RuntimeException("Input Error");
            }
        } catch (IOException | NumberFormatException e) {
            throw new RuntimeException("Input Error");
        }
        Queue<String> horseQueue = horseRace();

        if (horseQueue.element().equals("horse ".concat(String.valueOf(horseNumber)))) {
            System.out.println("You won!!!");
        } else {
            System.out.println("You lose");
        }
    }
}