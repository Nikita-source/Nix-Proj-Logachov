import primenumbers.PrimeNumberThread;
import reversehello.PrintHelloThread;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

public class MultithreadingMain {

    public static void main(String[] args) {
        helloThread();
        //primeNumbers();
    }

    private static void helloThread() {
        List<PrintHelloThread> helloThreadList = new ArrayList<>();
        for (int i = 0; i < 50; i++) {
            helloThreadList.add(new PrintHelloThread());
        }

        for (int i = 49; i > -1; i--) {
            helloThreadList.get(i).start();
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }


    private static void primeNumbers() {
        int countPrime = 0;

        List<Integer> numbers = new ArrayList<>();
        for (int i = 0; i < 201; i++) {
            numbers.add(i);
        }

        Callable<Integer> callable1 = new PrimeNumberThread(numbers.subList(0, numbers.size() / 2));
        FutureTask<Integer> primeNumberTasks1 = new FutureTask<>(callable1);
        new Thread(primeNumberTasks1).start();
        Callable<Integer> callable2 = new PrimeNumberThread(numbers.subList(numbers.size() / 2, numbers.size()));
        FutureTask<Integer> primeNumberTasks2 = new FutureTask<>(callable2);
        new Thread(primeNumberTasks2).start();

        try {
            countPrime += primeNumberTasks1.get();
            countPrime += primeNumberTasks2.get();
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        }

        System.out.println("total amount = " + countPrime);
    }
}
