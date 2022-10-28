package ReadersWriters;

public interface ReadWrite {

    void acquireRead() throws InterruptedException;

    void releaseRead();

    void acquireWrite() throws InterruptedException;

    void releaseWrite();
}
