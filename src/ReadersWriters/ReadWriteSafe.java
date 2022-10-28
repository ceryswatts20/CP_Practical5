package ReadersWriters;

import java.util.Random;

public class ReadWriteSafe implements ReadWrite {

    private int readers = 0;
    private boolean writing = false;
    // No. of waiting Writers
    private int waitingW = 0;
    private Random rnd = new Random();

    public synchronized void acquireRead() throws InterruptedException {
        // While writing is true or there are/is a waiting writers
        while (writing || waitingW > 0)
            wait();
        // Increments variable before rest of expression is evaluated
        ++readers;
        Time.delay(rnd.nextInt(120));
    }

    public synchronized void releaseRead() {
        // Decrements variable before rest of expression is evaluated
        --readers;
        Time.delay(rnd.nextInt(120));
        // If there are no more readers
        if (readers == 0)
            // Unblock a single writer
            notify();
    }

    public synchronized void acquireWrite() throws InterruptedException {
        // Increments variable before rest of expression is evaluated
        // i.e. a = 5, b = 2, b * a++ = 10, b + ++a = 12
        ++waitingW;
        // While there are readers or writing is false, wait
        while (readers > 0 || writing)
            wait();
        // Decrements variable before rest of expression is evaluated
        --waitingW;
        // Set writing to true
        writing = true;
        Time.delay(rnd.nextInt(120));
    }

    public synchronized void releaseWrite() {
        // Set writing to false
        writing = false;
        Time.delay(rnd.nextInt(120));
        // Unblock all readers
        notifyAll();
    }
}
