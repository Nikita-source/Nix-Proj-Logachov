import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

public class HorseThread implements Runnable {

    private static final Queue<String> horsesQueue = new ConcurrentLinkedQueue<>();
    private int index;

    public HorseThread() {
    }

    public HorseThread(int index) {
        this.index = index;
    }

    public Queue<String> getHorsesQueue() {
        return horsesQueue;
    }

    @Override
    public void run() {
        int currentDistance = 0;
        while (currentDistance < 1000) {
            currentDistance += 100 + (int) (Math.random() * 200);
            try {
                Thread.sleep(400 + (int) (Math.random() * 500));
            } catch (InterruptedException e) {
                throw new RuntimeException();
            }
        }
        horsesQueue.add("horse " + index);
    }
}
