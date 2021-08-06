package primenumbers;

import java.util.List;
import java.util.concurrent.Callable;

public class PrimeNumberThread implements Callable<Integer> {
    List<Integer> list;

    public PrimeNumberThread(List<Integer> list) {
        this.list = list;
    }

    @Override
    public Integer call() {
        int count = 0;
        for (Integer i : list) {
            boolean isPrimeNumber = true;
            if (i == 1) {
                isPrimeNumber = false;
            }
            for (int j = 2; j < i; j++) {
                if (i % j == 0) {
                    isPrimeNumber = false;
                    break;
                }
            }
            if (isPrimeNumber) {
                count++;
            }
        }
        System.out.println(Thread.currentThread().getName() + " - prime numbers count = " + count);
        return count;
    }
}
