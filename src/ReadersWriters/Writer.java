package ReadersWriters;

public class Writer extends Thread {
    ReadWriteSafe readWriteSafe = new ReadWriteSafe();

    @Override
    public void run() {
        while (true) {
            try {
                readWriteSafe.acquireWrite();
            } catch (InterruptedException e) {
                System.out.println("Error running acquireWrite.");
            }
            System.out.println("Writer is writing");
            readWriteSafe.releaseWrite();
            System.out.println("Writer finished writing");
        }
    }
}
