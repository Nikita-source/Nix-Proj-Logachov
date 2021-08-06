package reversehello;

public class PrintHelloThread implements Runnable {

    private int index;
    private int count;

    public PrintHelloThread(int index, int count) {
        this.index = index;
        this.count = count;
    }

    @Override
    public void run() {
        /*while (true) {
            if (index == count) break;
        }*/
        /*try {
            Thread.sleep(index * 30L);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }*/
        if (index != count) {
            try {
                Thread.currentThread().wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("Hello from " + Thread.currentThread().getName());
        }
    }
}
