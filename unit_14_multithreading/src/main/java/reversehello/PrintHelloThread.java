package reversehello;

public class PrintHelloThread extends Thread {

    @Override
    public void run() {
        System.out.println("Hello from " + currentThread().getName());
    }
}
