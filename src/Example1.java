import java.util.Random;

/*===========================================================
  Updated
  Hans Vandierendonck September 2014
===========================================================*/
class Example1 {
    static final int MAX_NUMBER = 10;
    public static void main (String[] args) {

        System.out.println("Start of program.");
        PCM pcm = new PCM();
        Producer producer  = new Producer(pcm,0);
        Consumer consumer  = new Consumer(pcm,0);
        Consumer consumer2  = new Consumer(pcm,1);

        producer.start();
        consumer.start();
        consumer2.start();
    }
}

class Producer extends Thread {
    private int number = 0;
    private PCM pcm;
    private int id;
    Random rnd = new Random();

    public Producer(PCM pcm, int id) {
        this.pcm = pcm;
        this.id = id;
    }

    public void run() {
        do {
            int i = produce();
            pcm.put(i);
        } while (number  != Example1.MAX_NUMBER);
        System.out.println("P: finished");
    }

    public int produce() {
        number++;
        Time.delay(rnd.nextInt(120));
        return number;
    }
}

class Consumer extends Thread {
    private int number = 0;
    private PCM pcm;
    private int id;
    Random rnd = new Random();

    public Consumer(PCM pcm, int id) {
        this.pcm = pcm;
        this.id = id;
    }

    public void run() {
        do {
            int i = pcm.get(id);
            consume(i);
        } while (number  != Example1.MAX_NUMBER);
        System.out.println("C" + id + ": finished");
    }

    public void consume(int i) {
        number++;
        Time.delay(rnd.nextInt(120));
    }
}

class PCM {
  private int N = 4;
  private int[] buffer = new int [N];
  private int tail = 0, head = 0, count = 0;
  Random rnd = new Random();

  // Called 'append' in notes
  public synchronized void put (int i) {
      // While buffer is full
      while (count == N){
          try {
              // Wait
              wait();
          } catch (InterruptedException e) {
              System.out.println("Interrupted Exception thrown: PCM.put() line 77");
          }
      }
      // Add to end of buffer
      buffer[tail] = i;
      // Update tail i.e. end buffer position
      tail = (tail + 1) % N;
      // Update count i.e. no. of values in buffer
      count++;
      // Wake up all threads waiting on the monitor
      // Called 'signal' in notes
      notifyAll();
      Time.delay(rnd.nextInt(120));
      System.out.println("P: " + i + " put in buffer");
  }

  // Called 'take' in notes
  public synchronized int get(int id) {
      int i;
      // While buffer is empty
      while (count == 0) {
          try {
              // Wait
              wait();
          } catch (InterruptedException e) {
              System.out.println("Interrupted Exception thrown: PCM.put() line 101");
          }
      }
      // Get value at start of buffer
      i = buffer[head];
      // Update head i.e. buffer start position
      head = (head + 1) % N;
      // Update count i.e. no. of values in buffer
      count--;
      // Called 'signal in notes
      notifyAll();
      Time.delay(rnd.nextInt(120));
      System.out.println("C" + id + ": " + i + " read from buffer");
      return i;
  }
}
