package ReadersWriters;

import java.util.Random;

public class Reader extends Thread {
    ReadWriteSafe readWriteSafe = new ReadWriteSafe();

    @Override
    public void run() {
        while (true) {
            try {
                readWriteSafe.acquireRead();
            } catch (InterruptedException e) {
                System.out.println("Error running acquireRead.");
            }
            System.out.println("Reader is reading.");
            readWriteSafe.releaseRead();
            System.out.println("Reader finished reading.");
        }
    }
}
